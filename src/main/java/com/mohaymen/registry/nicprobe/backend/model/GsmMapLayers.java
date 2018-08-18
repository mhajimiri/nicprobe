package com.mohaymen.registry.nicprobe.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class GsmMapLayers implements Serializable{
    private ArrayList<String> tcap_invokeID;
    private ArrayList<String> tcap_tid;
}
