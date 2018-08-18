package com.mohaymen.registry.nicprobe.backend.network;


import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ErrorOutputStream extends LogOutputStream {
    private static final Logger logger = LogManager.getLogger(ErrorOutputStream.class);
    @Override
    protected void processLine(String s, int i) {
        logger.info("illegal character...");
    }
}
