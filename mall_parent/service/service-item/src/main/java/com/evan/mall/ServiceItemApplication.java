package com.evan.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SuppressWarnings("all")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.evan.mall")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.evan.mall")
public class ServiceItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceItemApplication.class, args);
    }
}
