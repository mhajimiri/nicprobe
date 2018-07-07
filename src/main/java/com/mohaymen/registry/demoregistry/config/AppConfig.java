package com.mohaymen.registry.demoregistry.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@ComponentScan("com.mohaymen.registry.demoregistry")
@PropertySource(value = {"classpath:application.properties"})
@EnableRedisRepositories(
        basePackages = "com.mohaymen.registry.demoregistry.backend.elk"
)
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    @Primary
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(400);
        pool.setMaxPoolSize(800);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

        @Bean
        @Primary
        public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
            ThreadPoolTaskScheduler threadPoolTaskScheduler
                    = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.setPoolSize(10);
            threadPoolTaskScheduler.setThreadNamePrefix(
                    "ThreadPoolTaskScheduler");
            return threadPoolTaskScheduler;
        }

    @Bean
    @Primary
    public Gson objectMapper() {
        return new GsonBuilder().serializeNulls().create();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {{
            setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder
                    .create()
                    .setConnectionManager(new PoolingHttpClientConnectionManager() {{
                        setDefaultMaxPerRoute(400);
                        setMaxTotal(800);
                    }}).build()));
        }};
    }

}
