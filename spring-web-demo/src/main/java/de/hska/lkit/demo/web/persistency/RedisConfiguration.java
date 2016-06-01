package de.hska.lkit.demo.web.persistency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;
import de.hska.lkit.demo.web.model.user.User;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory jRedisConnectionFactory = new JedisConnectionFactory(new JedisPoolConfig());
        jRedisConnectionFactory.setHostName("localhost");
        jRedisConnectionFactory.setPort(6379);
        jRedisConnectionFactory.setPassword("");
        return jRedisConnectionFactory;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate getStringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(getConnectionFactory());
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, User> getRedisTemplate() {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<String, User>();
        redisTemplate.setConnectionFactory(getConnectionFactory());
        return redisTemplate;
    }

}