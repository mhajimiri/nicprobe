package com.mohaymen.registry.demoregistry.config;

import com.mohaymen.registry.demoregistry.backend.network.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;


import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ComponentScan("com.mohaymen.registry.demoregistry")
@PropertySource(value = {"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.mohaymen.registry.demoregistry.backend.elk",
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager"
)
@EnableScheduling
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    public ScheduledTasks scheduledTasks() {
        return new ScheduledTasks();
    }

    @Value("${threadCorePoolSize}")
    private int threadCorePoolSize;
    @Value("${threadMaxPoolSize}")
    private int threadMaxPoolSize;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(threadCorePoolSize);
        pool.setMaxPoolSize(threadMaxPoolSize);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public GsmMapOutputStream gsmMapOutputStream() {
        return new GsmMapOutputStream();
    }

    @Bean
    public DiameterOutputStream diameterOutputStream() {
        return new DiameterOutputStream();
    }

    @Bean
    public MyGsmMapHandler myGsmMapHandler() {
        return new MyGsmMapHandler();
    }

    @Bean
    public MyDiameterHandler myDiameterHandler() {
        return new MyDiameterHandler();
    }

    @Bean
    public ErrorOutputStream errorOutputStream() {
        return new ErrorOutputStream();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(
                new String[]{"com.mohaymen.registry"});

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        properties.put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
//        properties.put("hibernate.jdbc.batch_versioned_data", env.getProperty("hibernate.jdbc.batch_versioned_data"));
//        properties.put("hibernate.order_inserts", env.getProperty("hibernate.order_inserts"));
//        properties.put("hibernate.order_updates", env.getProperty("hibernate.order_updates"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource dataSource() {

//        DriverManagerDataSource dataSource
//                = new DriverManagerDataSource();
        org.apache.tomcat.jdbc.pool.DataSource dataSource=new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));

        //
        dataSource.setInitialSize(90);
        dataSource.setMaxActive(100);
//        dataSource.setMaxIdle(5);
//        dataSource.setMinIdle(3);

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManager().getObject());
        return transactionManager;
    }
}
