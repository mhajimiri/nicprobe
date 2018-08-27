package com.mohaymen.registry.nicprobe.backend.bl;

import com.mohaymen.registry.nicprobe.backend.model.FileNames;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 10000)
    public void deleteFinishedFiles() {

        if (FileNames.getList().size() > 0) {
            Iterator it = FileNames.getList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (FileNames.getList().get(entry.getKey()).isA() && FileNames.getList().get(entry.getKey()).isB()) {
                    File file=new File("/home/zamani/dump/"+entry.getKey());
                    if (file.exists()) {
                        try {
                            FileUtils.forceDelete(file);
                            it.remove();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
