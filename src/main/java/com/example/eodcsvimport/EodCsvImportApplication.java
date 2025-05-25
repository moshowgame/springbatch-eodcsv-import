package com.example.eodcsvimport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

@SpringBootApplication(scanBasePackages = "com.example.eodcsvimport")
@EnableBatchProcessing
@MapperScan("com.example.eodcsvimport.mapper")
public class EodCsvImportApplication {

    public static void main(String[] args) {
        SpringApplication.run(EodCsvImportApplication.class, args);
    }
}
