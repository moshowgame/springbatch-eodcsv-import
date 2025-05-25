package com.example.eodcsvimport.config;

import com.example.eodcsvimport.tasklet.EodCsvImportTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    @Autowired
    EodCsvImportTasklet tasklet;

    @Bean
    public Job eodCsvImportJob() {
        log.info("eodCsvImportJob");
        return new JobBuilder("eodCsvImportJob", jobRepository)
                .start(csvImportStep())
                .build();
    }

    @Bean
    public Step csvImportStep() {
        log.info("csvImportStep");
        return new StepBuilder("csvImportStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
