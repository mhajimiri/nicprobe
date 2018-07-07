package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class GsmMapLayers implements Serializable{

    private ArrayList<String> tcap_invokeID;

    private ArrayList<String> tcap_tid;

    private ArrayList<String> frame_time_epoch;

    private ArrayList<String> sccp_called_digits;

    private ArrayList<String> sccp_calling_digits;

    private ArrayList<String> ip_src;

    private ArrayList<String> ip_dst;
}
