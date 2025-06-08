package com.example.eodcsvimport.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;


    @Autowired
    private Job eodCsvImportJob1;

    @Autowired
    private Job eodCsvImportJob2;

    @GetMapping("/run1")
    public String runBatch1() throws Exception {
        try {
            log.info("****** EOD CSV JOB 1 - Preparing");

            // 创建 Job 参数（防止 Spring Batch 认为任务已执行过而跳过）
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("runTime", System.currentTimeMillis()) // 让 Job 每次运行都有唯一参数
                    .toJobParameters();

            log.info("****** EOD CSV JOB 1 - Starting");
            // 执行 Job
            jobLauncher.run(eodCsvImportJob1, jobParameters);
            log.info("****** EOD CSV JOB 1 - Done");
        } catch (Exception e) {
            log.error("****** EOD CSV JOB 1 - Error : ", e);
        }
        return "EOD CSV JOB 1 DONE";
    }
    @GetMapping("/run2")
    public String runBatch2() throws Exception {
        try {
            log.info("****** EOD CSV JOB 2 - Preparing");

            // 创建 Job 参数（防止 Spring Batch 认为任务已执行过而跳过）
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("runTime", System.currentTimeMillis()) // 让 Job 每次运行都有唯一参数
                    .toJobParameters();

            log.info("****** EOD CSV JOB 2 - Starting");
            // 执行 Job
            jobLauncher.run(eodCsvImportJob2, jobParameters);
            log.info("****** EOD CSV JOB 2 - Done");
        } catch (Exception e) {
            log.error("****** EOD CSV JOB 2 - Error : ", e);
        }
        return "EOD CSV JOB 2 DONE";
    }
}
