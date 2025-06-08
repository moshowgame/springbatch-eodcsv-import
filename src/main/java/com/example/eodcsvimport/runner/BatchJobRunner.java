//package com.example.eodcsvimport.runner;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//@Component
//@Log4j2
//@RequiredArgsConstructor
//public class BatchJobRunner implements CommandLineRunner {
//
//    private final JobLauncher jobLauncher;
//    private final Job eodCsvImportJob;
//
//    @Override
//    public void run(String... args) {
//        try {
//            log.info("Spring Boot 启动后执行 EOD CSV 批量导入 Job...");
//
//            log.info("创建 Job 参数 {}", Arrays.toString(args));
//            // 创建 Job 参数（防止 Spring Batch 认为任务已执行过而跳过）
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("runTime", System.currentTimeMillis()) // 让 Job 每次运行都有唯一参数
//                    .toJobParameters();
//
//            log.info("准备开始执行Job");
//            // 执行 Job
//            jobLauncher.run(eodCsvImportJob, jobParameters);
//
//            log.info("EOD CSV 批量导入任务执行完成！");
//        } catch (Exception e) {
//            log.error("执行 EOD CSV 任务时发生异常", e);
//        }
//    }
//}
