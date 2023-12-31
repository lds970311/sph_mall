package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.evan.mall.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void testLock() throws InterruptedException {
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        String uuid = UUID.randomUUID().toString();
        Boolean lock = valueOperations.setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
        if (lock) {
            // 查询redis中的num值
            String value = this.redisTemplate.opsForValue().get("num");
            // 没有该值return
            if (StringUtils.isBlank(value)) {
                return;
            }
            // 有值就转成成int
            int num = Integer.parseInt(value);
            // 把redis中的num值+1
            this.redisTemplate.opsForValue().set("num", String.valueOf(++num));
            //lua脚本实现锁
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                    "then return redis.call('del', KEYS[1]) " +
                    "else return 0 " +
                    "end";
            this.redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);
            /*if (uuid.equals(valueOperations.get("lock"))) {
                // 释放锁
                this.redisTemplate.delete("lock");
            }*/
        } else {
            Thread.sleep(100);
            testLock();
        }
    }
}
