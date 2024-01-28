package com.github.biggerball.mockrecorder.storage.entity;

import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Data
public class ReadDbRecord {
    private String traceId;
    private String className;
    private String interfaces;
    private String methodName;
    private ParamInfo paramInfo;
    private String createTime;

    public ReadDbRecord(InvokeRecord paramRecord) {
        this.traceId = paramRecord.getTraceId();

        String className = paramRecord.getClassName();
        int index = className.indexOf("|");
        if (index > 0) {
            this.className = className.substring(0, index);
            this.interfaces = className.substring(index + 1);
        } else {
            this.className = className;
        }
        this.methodName = paramRecord.getMethodName();
        this.paramInfo = paramRecord.getParamInfo();
        this.createTime = DateFormatUtils.format(new Date(paramRecord.getCreateTime()), "yyyy-MM-dd HH:mm:ss");
    }
}
