package com.mohaymen.registry.nicprobe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graylog2.gelfclient.*;
import org.graylog2.gelfclient.transport.GelfTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetSocketAddress;

@Configuration
@EnableScheduling
@ComponentScan("com.mohaymen.registry.nicprobe")
@PropertySource(value = {"classpath:application.properties"})
@EnableRedisRepositories(
        basePackages = "com.mohaymen.registry.nicprobe.backend.dao"
)
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(6000);
        pool.setMaxPoolSize(10000);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public Gson objectMapper() {
        return new GsonBuilder().serializeNulls().create();
    }

    @Bean
    public GelfTransport transport() {
        GelfConfiguration config = new GelfConfiguration(new InetSocketAddress("172.16.1.164", 12202))
                .transport(GelfTransports.UDP)
                .queueSize(7000)
                .connectTimeout(5000)
                .reconnectDelay(1000)
                .tcpNoDelay(true)
                .sendBufferSize(1024)
                .compression(Compression.GZIP);

        GelfTransport transport = GelfTransports.create(config);
        return transport;
    }

    @Bean
    public GelfMessageBuilder gelfMessageBuilder(GelfTransport transport){

        final GelfMessageBuilder builder = new GelfMessageBuilder("", "MCI")
                .level(GelfMessageLevel.INFO);
        return builder;
    }
}
