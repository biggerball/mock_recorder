package com.github.biggerball.mockrecorder.agent.entity;

import lombok.Data;

@Data
public class InvokeRecord {
    private String traceId;
    private String className;
    private String interfaces;
    private String methodName;
    private ParamInfo paramInfo;
    private Long createTime;

    public InvokeRecord(String traceId, String className, String methodName, Long createTime) {
        this.traceId = traceId;
        this.className = className;
        this.methodName = methodName;
        this.paramInfo = new ParamInfo();
        this.createTime = createTime;
    }

    public void addReturnValue(String type, String value) {
        this.paramInfo.setReturnValue(new Param(type, value));
    }

    public void addException(String type, String value) {
        this.paramInfo.setException(new Param(type, value));
    }

    public void addParameter(String type, String value) {
        this.paramInfo.getRequestParams().add(new Param(type, value));
    }
}