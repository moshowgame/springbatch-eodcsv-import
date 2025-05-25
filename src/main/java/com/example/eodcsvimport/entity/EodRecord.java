package com.example.eodcsvimport.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("eod_record")
public class EodRecord {

    @TableId
    private String accountNumber;  // 主键，可根据业务情况调整

    private BigDecimal amount;

    private LocalDate balanceDate;
}
