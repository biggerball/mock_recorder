package com.github.biggerball.storage.entity;

import com.github.biggerball.entity.ParamInfo;
import com.github.biggerball.entity.ParamRecord;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Data
public class ReadDbRecord {
    private String traceId;
    private String className;
    private String methodName;
    private ParamInfo paramInfo;
    private String createTime;

    public ReadDbRecord(ParamRecord paramRecord) {
        this.traceId = paramRecord.getTraceId();
        this.className = paramRecord.getClassName();
        this.methodName = paramRecord.getMethodName();
        this.paramInfo = paramRecord.getParamInfo();
        this.createTime = DateFormatUtils.format(new Date(paramRecord.getCreateTime()), "yyyy-MM-dd HH:mm:ss");
    }
}
