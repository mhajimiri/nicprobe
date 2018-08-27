package com.mohaymen.registry.nicprobe.backend.bl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 10000)
    public void deleteFinishedFiles() {

        File dir = new File("/home/zamani/dump");
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            if(System.currentTimeMillis()-file.lastModified()>= 60000){
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
