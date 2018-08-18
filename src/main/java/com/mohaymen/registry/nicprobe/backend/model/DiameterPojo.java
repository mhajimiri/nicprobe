package com.mohaymen.registry.nicprobe.backend.model;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DiameterPojo implements Serializable{
    private String timestamp;
    private DiameterLayers layers;
}
