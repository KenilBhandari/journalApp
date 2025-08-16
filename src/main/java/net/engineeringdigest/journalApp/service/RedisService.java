package net.engineeringdigest.journalApp.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> weatherResponseClass){
        try {

        Object redisResponse = redisTemplate.opsForValue().get(key);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(redisResponse.toString(), weatherResponseClass);
        } catch (Exception e) {
            log.error("Exception ", e);
            return null;
        }

    }

    public void set(String key, Object value, long ttl){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(value);

        redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception ", e);
        }

    }

}
