package com.mohaymen.registry.demoregistry.backend.network;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MyDiameterHandler {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private DiameterOutputStream diameterOutputStream;
    @Autowired
    private ErrorOutputStream errorOutputStream;

    private static final Logger logger = LogManager.getLogger(MyDiameterHandler.class);

    public void run() {
        File dumpDirFile = new File("/home/zamani/dump");
        FileAlterationObserver observer = new FileAlterationObserver(dumpDirFile);
        FileAlterationMonitor monitor = new FileAlterationMonitor(7);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                Thread thread = new Thread() {
                    public void run() {
                        Executor diameterShell = new DefaultExecutor();
                        PumpStreamHandler diameterPump = new PumpStreamHandler(diameterOutputStream, errorOutputStream);
                        diameterShell.setStreamHandler(diameterPump);
                        CommandLine tsharkDiameter = new CommandLine("./script-diameter.sh");
                        tsharkDiameter.addArgument(file.getAbsolutePath());
                        try {
                            diameterShell.execute(tsharkDiameter);
//                            if(!file.getName().contains("checked")){
//                                if(file.exists()) {
//                                    FileUtils.moveFile(file, new File("/home/zamani/dump/checked/" +
//                                            file.getName().replace("pcap", "checked")));
//                                }
//                            }
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
