package com.mohaymen.registry.nicprobe.backend.model;

import java.util.HashMap;
import java.util.Map;

public class FileNames {
    public static Map<String,FileStatus> listFile;

    static {
        listFile=new HashMap<>();
    }
    public static Map<String,FileStatus> getList() {
        return listFile;
    }
}
