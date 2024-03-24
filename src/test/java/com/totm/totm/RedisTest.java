package com.totm.totm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class RedisTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void 레디스_연결() {
        redisTemplate.opsForList().rightPush("key", "value");
    }

    @Test
    public void TTL_테스트() {
        redisTemplate.opsForValue().set("key", "value");
    }
}
