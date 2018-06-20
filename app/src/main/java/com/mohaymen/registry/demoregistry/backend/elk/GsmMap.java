package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GsmMap implements Serializable {
    private String tid;
    private String time;
    private String invokeId;
    private String calledDigit;
    private String callingDigit;
}
