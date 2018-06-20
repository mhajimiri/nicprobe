package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Diameter implements Serializable {
    private String sessionId;
    private String time;
    private String hopByhopId;
    private String endToEndId;
}
