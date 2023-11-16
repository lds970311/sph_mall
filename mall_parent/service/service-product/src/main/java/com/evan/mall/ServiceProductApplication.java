package com.evan.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SuppressWarnings("all")
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.evan.mall.mapper")
@ComponentScan(basePackages = "com.evan.mall")
@EnableSwagger2
public class ServiceProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class, args);
    }
}
