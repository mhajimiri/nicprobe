package com.mohaymen.registry.demoregistry.backend.network;


import com.google.gson.Gson;
import com.mohaymen.registry.demoregistry.backend.elk.DiameterModel;
import com.mohaymen.registry.demoregistry.backend.elk.DiameterRepository;
import com.mohaymen.registry.demoregistry.backend.elk.MyPojo;
import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DiameterOutputStream extends LogOutputStream {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private DiameterRepository diameterRepository;

    private Runnable r0;
    private Gson gson;

    private static final Logger logger = LogManager.getLogger(DiameterOutputStream.class);

    @Override
    protected void processLine(String line, int level) {
        if (!line.isEmpty() && line.contains("#") && !line.startsWith("#")) {
            convertToJsonAndSend(line);
        }
    }


    public void convertToJsonAndSend(String line) {
        //
        r0 = new Runnable() {
            @Override
            public void run() {
                //
                try {
                    send(convert(line));
                } catch (Exception e) {
                    logger.error("Oo0PSs!", e);
                }
            }
        };
        //
        taskExecutor.execute(r0);
        //

    }

    public MyPojo convert(String line) {
        gson = new Gson();
        MyPojo myPojo = new MyPojo();
        ///
        ArrayList<DiameterModel> requests = new ArrayList<>();
        String[] part = line.split("#");

        String[] hopByhopIds = part[0].split(",");
        for (String hopByhopId : hopByhopIds) {
            DiameterModel diameterModel = new DiameterModel();
            diameterModel.setHopByhopId(hopByhopId);
            requests.add(diameterModel);
        }

        String[] sessionIds = part[2].split(",");
        for (int i = 0; i < sessionIds.length; i++) {
            requests.get(i).setSessionId(sessionIds[i]);
        }

        String[] endToEndIds = part[1].split(",");
        for (int i = 0; i < endToEndIds.length; i++) {
            requests.get(i).setEndToEndId(endToEndIds[i]);
            requests.get(i).setTime(part[3].replace("\"", ""));
            myPojo = new MyPojo();
            myPojo.setShort_message(gson.toJson(requests.get(i)));
            send(myPojo);
        }
        return myPojo;
    }


    public void send(MyPojo myPojo) {
        try {
        DiameterModel diameterJson = gson.fromJson(myPojo.getShort_message(), DiameterModel.class);
        if(diameterJson !=null && !diameterJson.getSessionId().isEmpty()) {
            DiameterModel diameterModel = new DiameterModel();
            diameterModel.setSessionId(diameterJson.getSessionId());
            diameterModel.setEndToEndId(diameterJson.getEndToEndId());
            diameterModel.setHopByhopId(diameterJson.getHopByhopId());
            diameterModel.setTime(diameterJson.getTime());
            diameterRepository.save(diameterModel);
            logger.info("Diameter uniqueId:" + diameterJson.getHopByhopId() + "#"
                    + diameterJson.getEndToEndId() + "#" + diameterJson.getSessionId());
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
