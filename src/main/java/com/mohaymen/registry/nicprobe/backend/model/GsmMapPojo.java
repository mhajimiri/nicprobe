package com.mohaymen.registry.nicprobe.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GsmMapPojo implements Serializable{
    private String timestamp;
    private GsmMapLayers layers;
}
