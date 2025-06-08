package com.example.eodcsvimport.config;

import com.example.eodcsvimport.tasklet.EodCsvImportTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    EodCsvImportTasklet tasklet;

    @Bean
    public Job eodCsvImportJob1() {
        log.info("eodCsvImportJob1");
        return new JobBuilder("eodCsvImportJob1", jobRepository)
                .start(new StepBuilder("csvImportStep1", jobRepository)
                        .tasklet(tasklet, transactionManager)
                        .build())
                .build();
    }

    @Bean
    public Job eodCsvImportJob2() {
        log.info("eodCsvImportJob2");
        return new JobBuilder("eodCsvImportJob2", jobRepository)
                .start(new StepBuilder("csvImportStep2", jobRepository)
                        .tasklet(tasklet, transactionManager)
                        .build())
                .build();
    }
}
