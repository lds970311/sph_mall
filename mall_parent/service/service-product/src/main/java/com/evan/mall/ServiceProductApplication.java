package com.evan.mall;

import com.evan.mall.common.constant.RedisConst;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class ServiceProductApplication implements CommandLineRunner {
    @Autowired
    private RedissonClient redissonClient;

    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class, args);
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        RBloomFilter<Object> rbloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
        // 初始化布隆过滤器，预计统计元素数量为100000，期望误差率为0.001
        rbloomFilter.tryInit(100000, 0.001);
        for (Long i = 20l; i < 34l; i++) {
            rbloomFilter.add(i);
        }

    }
}
