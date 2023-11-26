package com.evan.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SuppressWarnings("all")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.evan.mall")
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.evan.mall")
@EnableSwagger2
public class WebPcApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebPcApplication.class, args);
    }
}
