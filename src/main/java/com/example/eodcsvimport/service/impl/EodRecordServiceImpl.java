package com.example.eodcsvimport.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eodcsvimport.entity.EodRecord;
import com.example.eodcsvimport.mapper.EodRecordMapper;
import com.example.eodcsvimport.service.IEodRecordService;
import org.springframework.stereotype.Service;

@Service
public class EodRecordServiceImpl extends ServiceImpl<EodRecordMapper, EodRecord> implements IEodRecordService {

}
