package com.mohaymen.registry.demoregistry.backend.elk;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class DiameterLayers implements Serializable{

    @SerializedName("diameter_Session-Id")
    private ArrayList<String> diameter_Session_Id;

    private ArrayList<String> diameter_hopbyhopid;

    private ArrayList<String> diameter_endtoendid;

    private ArrayList<String> frame_time_epoch;

    private ArrayList<String> ip_src;

    private ArrayList<String> ip_dst;
}
