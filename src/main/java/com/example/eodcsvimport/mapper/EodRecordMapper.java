package com.example.eodcsvimport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.eodcsvimport.entity.EodRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EodRecordMapper extends BaseMapper<EodRecord> {
}
