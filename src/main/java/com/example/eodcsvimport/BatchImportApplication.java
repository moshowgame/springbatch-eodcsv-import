package com.example.eodcsvimport;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
@MapperScan("com.example.eodcsvimport.mapper")
@Slf4j
public class BatchImportApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchImportApplication.class, args);
        log.info("Spring Boot started");
    }
}
