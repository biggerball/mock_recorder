package com.github.biggerball.storage.mapper;

import com.github.biggerball.entity.ParamRecord;
import com.github.biggerball.storage.entity.ReadDbReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ParamRecordMapper {

    void insert(ParamRecord paramRecord);

    int createTable();

    List<ParamRecord> read(@Param("req") ReadDbReq req, @Param("from") Long fromTime, @Param("to") Long toTime);
}
