package de.hska.lkit.demo.web;

/**
 * Created by patrickkoenig on 29.05.16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.print.DocFlavor;
import java.util.UUID;

@Repository
public class Redisexample {
    // inject the actual template


    private RedisAtomicLong userid;
    private RedisTemplate<String, User> redisTemplate;
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    public Redisexample(RedisTemplate<String, User> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.userid = new RedisAtomicLong("userid", stringRedisTemplate.getConnectionFactory());
    }


    public User createToby() {

        User toby = new User (1,"Lena","TobyIstDerSuesseste");

        String key = "user"+toby.getUsername();
        stringRedisTemplate.opsForHash().put(key, "id", "2");
//        stringRedisTemplate.opsForHash().put(key, "firstName", toby.getId());
        stringRedisTemplate.opsForHash().put(key, "lastName", toby.getUsername());
        stringRedisTemplate.opsForHash().put(key, "username", toby.getUsername());
        stringRedisTemplate.opsForHash().put(key, "password", toby.getPassword());
        stringRedisTemplate.opsForSet().add("user", key);
        System.out.println("jojo");
        return toby; }

    public User create(User user) {



        String key = "user"+user.getUsername();
        stringRedisTemplate.opsForHash().put(key, "id", UUID.randomUUID().toString());
        stringRedisTemplate.opsForHash().put(key, "firstName", user.getId());
        stringRedisTemplate.opsForHash().put(key, "lastName", user.getUsername());
        stringRedisTemplate.opsForHash().put(key, "username", user.getUsername());
        stringRedisTemplate.opsForHash().put(key, "password", user.getPassword());
        stringRedisTemplate.opsForSet().add("user", key);
        return user; }

    }