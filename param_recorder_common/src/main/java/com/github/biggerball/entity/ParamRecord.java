package com.github.biggerball.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ParamRecord {
    private String traceId;
    private String className;
    private String methodName;
    private ParamInfo paramInfo;
    private Long createTime;

    public ParamRecord(String traceId, String className, String methodName, Long createTime) {
        this.traceId = traceId;
        this.className = className;
        this.methodName = methodName;
        this.paramInfo = new ParamInfo();
        this.createTime = createTime;
    }

    public void addReturnValue(String type, String value) {
        this.paramInfo.setReturnValue(new Param(type, value));
    }
}
