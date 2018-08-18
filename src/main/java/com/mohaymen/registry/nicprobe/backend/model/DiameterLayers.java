package com.mohaymen.registry.nicprobe.backend.model;


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
    private ArrayList<String> ip_src;
    private ArrayList<String> ip_dst;
}
