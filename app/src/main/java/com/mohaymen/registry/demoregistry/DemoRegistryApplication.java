package com.mohaymen.registry.demoregistry;

import com.mohaymen.registry.demoregistry.backend.network.MyDiameterHandler;
import com.mohaymen.registry.demoregistry.backend.network.MyGsmMapHandler;
import com.mohaymen.registry.demoregistry.backend.network.ScheduledTasks;
import com.mohaymen.registry.demoregistry.config.AppConfig;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
public class DemoRegistryApplication {

    private static final Logger logger = LogManager.getLogger(DemoRegistryApplication.class);

    public static void main(String[] args) {
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tehran"));
            SpringApplication.run(DemoRegistryApplication.class, args);
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
            //
            Thread thread = new Thread() {
                public void run() {
                    Executor dumpcapShell = new DefaultExecutor();
                    CommandLine dumpcap = new CommandLine("./script-dumpcap.sh");
                    dumpcap.addArgument("10000");
                    dumpcap.addArgument("ens256");
                    try {
                        dumpcapShell.execute(dumpcap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            taskExecutor.execute(thread);
            //
            MyGsmMapHandler myGsmMapHandler = (MyGsmMapHandler) context.getBean("myGsmMapHandler");
            myGsmMapHandler.run();
//            MyDiameterHandler myDiameterHandler = (MyDiameterHandler) context.getBean("myDiameterHandler");
//            myDiameterHandler.run();

//            ScheduledTasks scheduledTasks= (ScheduledTasks) context.getBean("scheduledTasks");
//            scheduledTasks.reportCurrentCount();
        }catch (Exception e){
            logger.error("Oo0PSs!", e);
        }
    }
}
