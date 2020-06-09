package com.example.redis.config;

import com.example.redis.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public HashOperations<String, byte[], byte[]> hashOperations() {
        return redisTemplate.opsForHash();
    }

    @Autowired
    private  ObjectMapper objectMapper;

    @Bean
    public HashOperations<Object, String, Object> jacksonHashOperations() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate.opsForHash();
    }
}
