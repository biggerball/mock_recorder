package com.github.biggerball.mockrecorder.storage.mapper;

import com.github.biggerball.mockrecorder.storage.entity.InvokeRecord;
import com.github.biggerball.mockrecorder.storage.entity.ReadDbReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ParamRecordMapper {

    void insert(InvokeRecord paramRecord);

    int createTable();

    List<InvokeRecord> read(@Param("req") ReadDbReq req, @Param("from") Long fromTime, @Param("to") Long toTime);
}
