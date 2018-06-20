package com.mohaymen.registry.demoregistry.backend.network;

import com.google.gson.Gson;
import com.mohaymen.registry.demoregistry.backend.elk.*;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GsmMapOutputStream extends LogOutputStream {

    @Autowired
    private GsmMapRepository gsmMapRepository;

    private Gson gson;

    private static final Logger logger = LogManager.getLogger(GsmMapOutputStream.class);

    @Override
    protected void processLine(String line, int level) {

        if (StringUtils.isNotBlank(line) && StringUtils.isNotEmpty(line) &&
                StringUtils.contains(line, ",") && StringUtils.contains(line, "#") &&
                StringUtils.contains(line, ":")) {
            convertToJsonAndSend(line);
        }
    }


    public void convertToJsonAndSend(String line) {
        try {
            send(convert(line));
        } catch (Exception e) {
            logger.error("Oo0PSs!", e);
        }
    }

    public ArrayList<GsmMapModel> convert(String line) {
        ///
        ArrayList<GsmMapModel> list = new ArrayList<>();
        StrTokenizer tokens = new StrTokenizer(line, "#");
        List<String> parts = tokens.getTokenList();
        for (String part : parts) {
            if (parts.indexOf(part) == 0) {
                StrTokenizer invokeIDs = new StrTokenizer(part, ",");
                List<String> invokeIDParts = invokeIDs.getTokenList();
                for (String invokeId : invokeIDParts) {
                    GsmMapModel gsmMapModel = new GsmMapModel();
                    gsmMapModel.setInvokeId(invokeId);
                    list.add(gsmMapModel);
                }

                if (parts.indexOf(part) == 1) {
                    StrTokenizer tIDs = new StrTokenizer(part, ",");
                    List<String> tIDParts = tIDs.getTokenList();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setTid(tIDParts.get(i));
                    }
                }

                if (parts.indexOf(part) == 2) {
                    StrTokenizer calleds = new StrTokenizer(part, ",");
                    List<String> calledParts = calleds.getTokenList();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setCalledDigit(calledParts.get(i));
                    }
                }

                if (parts.indexOf(part) == 3) {
                    StrTokenizer callings = new StrTokenizer(part, ",");
                    List<String> callingParts = callings.getTokenList();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setCallingDigit(callingParts.get(i));
                    }
                }

                if (parts.indexOf(part) == 4) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setTime(part);
                    }
                }

            }

        }
        return list;
    }


    public void send(ArrayList<GsmMapModel> requests) {
        try {
            if (requests != null) {
                for (GsmMapModel request : requests) {
                    gsmMapRepository.save(request);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
