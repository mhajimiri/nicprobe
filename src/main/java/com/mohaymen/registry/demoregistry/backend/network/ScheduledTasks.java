package com.mohaymen.registry.demoregistry.backend.network;


import com.mohaymen.registry.demoregistry.backend.elk.CalcResponseTime;
import com.mohaymen.registry.demoregistry.backend.elk.DiameterRepository;
import com.mohaymen.registry.demoregistry.backend.elk.GsmMapRepository;
import com.mohaymen.registry.demoregistry.backend.elk.ResponseInfoRepository;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Component
public class ScheduledTasks {
    @Autowired
    private GsmMapRepository gsmMapRepository;
    @Autowired
    private DiameterRepository diameterRepository;

    private static final Logger logger = LogManager.getLogger(ScheduledTasks.class);

    @Scheduled(fixedRate = 60000)
    public void reportCurrentCount() {
        File file = new File("count.txt");
        try {
            long g=gsmMapRepository.count();
            long d=diameterRepository.count();

            String data = ++g + "," + d + ": "+ "-->" +
                    new Date() + "\n";
            FileUtils.writeStringToFile(file, data, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
