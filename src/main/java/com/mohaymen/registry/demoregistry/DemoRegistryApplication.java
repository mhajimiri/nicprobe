package com.mohaymen.registry.demoregistry;

import com.mohaymen.registry.demoregistry.backend.network.MyDiameterHandler;
import com.mohaymen.registry.demoregistry.backend.network.MyGsmMapHandler;
import org.apache.commons.exec.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
@Configuration
public class DemoRegistryApplication {

    private static final Logger logger = LogManager.getLogger(DemoRegistryApplication.class);

    public static void main(String[] args) {
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tehran"));
            SpringApplication.run(DemoRegistryApplication.class, args);
        }catch (Exception e){
            logger.error("Oo0PSs!", e);
        }
    }

    @Bean
    public CommandLineRunner t(MyGsmMapHandler myGsmMapHandler,MyDiameterHandler myDiameterHandler) {
        return ( arg ) -> {
            Thread thread = new Thread() {
                public void run() {
                    Executor dumpcapShell = new DefaultExecutor();
//                    ExecuteWatchdog watchdog=new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
                    CommandLine dumpcap = new CommandLine("./script-dumpcap.sh");
                    dumpcap.addArgument("4096");
                    dumpcap.addArgument("ens256");
                    dumpcap.addArgument("400");
                    try {
//                        dumpcapShell.setWatchdog(watchdog);
                        dumpcapShell.execute(dumpcap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            myGsmMapHandler.run();
            myDiameterHandler.run();
        };
    }
}
