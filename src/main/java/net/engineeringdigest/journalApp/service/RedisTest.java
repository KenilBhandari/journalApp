package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("state", "gujarat");
        Object emailID = redisTemplate.opsForValue().get("cloud");
        log.info("LOGGING"+ emailID);

    }

}
