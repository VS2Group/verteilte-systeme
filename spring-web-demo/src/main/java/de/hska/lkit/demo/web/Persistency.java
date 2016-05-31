package de.hska.lkit.demo.web;

/**
 * Created by patrickkoenig on 29.05.16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class Persistency {
    // inject the actual template


    private RedisAtomicLong userid;
    private StringRedisTemplate stringRedisTemplate;

    private HashOperations<String, String, String> redisStringHashOps;
    private SetOperations<String, String> redisStringSetOps;
    private ListOperations<String, String> redisStringListOps;
    private ValueOperations<String, String> redisStringValueOps;


    @Autowired
    public Persistency(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisStringHashOps = stringRedisTemplate.opsForHash();
        this.redisStringListOps = stringRedisTemplate.opsForList();
        this.redisStringSetOps = stringRedisTemplate.opsForSet();
//        this.userid = new RedisAtomicLong("userid", stringRedisTemplate.getConnectionFactory());
    }


    public boolean createUser(User user) {
        String key = "user:" + user.getUsername();
        redisStringHashOps.put(key, "id", "12345");
        redisStringHashOps.put(key, "username", user.getUsername());
        redisStringHashOps.put(key, "password", user.getPassword());

        redisStringSetOps.add("user", key);
        return false;
    }

}