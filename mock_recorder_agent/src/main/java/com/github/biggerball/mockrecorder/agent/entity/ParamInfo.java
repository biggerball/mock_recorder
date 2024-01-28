package com.github.biggerball.mockrecorder.agent.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParamInfo implements Serializable {

    private static final long serialVersionUID = 8744863230886819487L;
    private Param exception;
    private Param returnValue;
    private List<Param> requestParams;

    public ParamInfo() {
        this.requestParams = new ArrayList<>();
    }
}
