package com.github.biggerball.mockrecorder.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Param implements Serializable {
    private static final long serialVersionUID = -6366752146528673533L;
    private String type;
    private String value;


}
