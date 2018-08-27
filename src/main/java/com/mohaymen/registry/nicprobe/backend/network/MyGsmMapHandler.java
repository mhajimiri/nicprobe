package com.mohaymen.registry.nicprobe.backend.network;

import org.apache.commons.exec.*;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MyGsmMapHandler implements Runnable {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private GsmMapOutputStream gsmMapOutputStream;
    @Autowired
    private ErrorOutputStream errorOutputStream;

    private static final Logger logger = LogManager.getLogger(MyGsmMapHandler.class);

    @Override
    public void run() {
        File dumpDirFile = new File("/home/zamani/dump");
        FileAlterationObserver observer = new FileAlterationObserver(dumpDirFile);
        FileAlterationMonitor monitor = new FileAlterationMonitor(1);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                Thread thread = new Thread() {
                    public void run() {
                        Executor gsmMapShell = new DefaultExecutor();
                        PumpStreamHandler gsmMapPump = new PumpStreamHandler(gsmMapOutputStream, errorOutputStream);
                        gsmMapShell.setStreamHandler(gsmMapPump);
                        CommandLine tsharkGsmMap = new CommandLine("./script-gsm-map.sh");
                        tsharkGsmMap.addArgument(file.getAbsolutePath());
                        try {
                            gsmMapShell.execute(tsharkGsmMap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                taskExecutor.execute(thread);
            }
        };
        observer.addListener(listener);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
