package com.example.eodcsvimport.tasklet;

import com.example.eodcsvimport.entity.EodRecord;
import com.example.eodcsvimport.service.IEodRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class EodCsv2ImportTasklet implements Tasklet {
    @Autowired
    IEodRecordService eodRecordService;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Value("${app.csv.filepath}")
    private String csvFilePath;

    @Value("${app.csv.chunkSize}")
    private int chunkSize;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 整个应用导入逻辑使用 transactionTemplate.execute 保证在同一事务中执行
        transactionTemplate.execute(status -> {
            try {
                log.info("开始批量导入任务，先清空表数据……");
                // 清空表（注意：使用 MyBatis-Plus 提供的方法，传 null 表示清空全部记录）
                eodRecordService.remove(null);

                int successCount = 0;
                int failureCount = 0;
                List<EodRecord> batchList = new ArrayList<>(chunkSize);

                File csvFile = new File(csvFilePath);
                if (!csvFile.exists() || !csvFile.isFile()) {
                    log.error("CSV 文件不存在或无效：{}", csvFilePath);
                    throw new RuntimeException("CSV 文件不存在");
                }

                // 使用 BufferedReader 流式读取，防止内存溢出
                try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                    CSVFormat format = CSVFormat.DEFAULT
                            .withFirstRecordAsHeader()
                            .withTrim();
                    try (CSVParser parser = new CSVParser(reader, format)) {
                        for (CSVRecord record : parser) {
                            // 读取 CSV 每一行记录，并构造 EodRecord 对象
                            String accountNumber = record.get("accountNumber");
                            String amountStr = record.get("amount");
                            String balanceDateStr = record.get("balanceDate");

                            if (!StringUtils.hasText(accountNumber) ||
                                    !StringUtils.hasText(amountStr) ||
                                    !StringUtils.hasText(balanceDateStr)) {
                                log.warn("记录中存在空值，跳过：{}", record);
                                continue;
                            }

                            EodRecord eodRecord = new EodRecord();
                            eodRecord.setAccountNumber(accountNumber);
                            try {
                                eodRecord.setAmount(new BigDecimal(amountStr));
                            } catch (NumberFormatException e) {
                                log.error("金额格式错误，跳过记录：{}", record, e);
                                continue;
                            }

                            try {
                                eodRecord.setBalanceDate(LocalDate.parse(balanceDateStr));
                            } catch (DateTimeParseException e) {
                                log.error("日期格式错误，跳过记录：{}", record, e);
                                continue;
                            }

                            batchList.add(eodRecord);

                            if (batchList.size() >= chunkSize) {
                                // 批量写入数据
                                int[] counts = processBatch(batchList);
                                successCount += counts[0];
                                failureCount += counts[1];
                                batchList.clear();
                            }
                        }
                    }
                }

                // 处理最后不足一批的数据
                if (!batchList.isEmpty()) {
                    int[] counts = processBatch(batchList);
                    successCount += counts[0];
                    failureCount += counts[1];
                }

                log.info("批量导入完成：成功记录数 = {}，重试后失败记录数 = {}", successCount, failureCount);
            } catch (Exception e) {
                log.error("批处理任务发生异常，将回滚事务", e);
                throw new RuntimeException(e);
            }
            return null;
        });
        return RepeatStatus.FINISHED;
    }

    /**
     * 尝试批量插入：若失败则降级为逐条插入
     * @param records 本批次记录
     * @return int[0] = 成功数， int[1] = 失败数
     */
    private int[] processBatch(List<EodRecord> records) {
        int success = 0;
        int failure = 0;
        try {
            // 尝试批量插入
            boolean insertResult = eodRecordService.saveBatch(records, records.size());
            if (insertResult) {
                success += records.size();
            } else {
                throw new RuntimeException("saveBatch 返回 false");
            }
        } catch (Exception e) {
            log.error("批量插入失败，切换到单条插入，当前批次记录数：{}", records.size(), e);
            for (EodRecord record : records) {
                try {
                    if (eodRecordService.save(record)) {
                        success++;
                    } else {
                        failure++;
                        log.error("单条插入失败，记录：{}", record);
                    }
                } catch (Exception ex) {
                    failure++;
                    log.error("单条插入异常，记录：{}", record, ex);
                }
            }
        }
        return new int[]{success, failure};
    }
}
