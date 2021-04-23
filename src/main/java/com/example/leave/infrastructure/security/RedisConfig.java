package com.example.leave.infrastructure.security;

import com.example.leave.models.WorkTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, WorkTime> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, WorkTime> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}