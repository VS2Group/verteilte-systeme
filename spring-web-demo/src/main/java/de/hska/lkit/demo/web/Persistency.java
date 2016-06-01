package de.hska.lkit.demo.web;

/**
 * Created by patrickkoenig on 29.05.16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class Persistency {

    private RedisAtomicLong userid;

    private StringRedisTemplate stringRedisTemplate;

    private HashOperations<String, String, String> redisStringHashOps;
    private final ZSetOperations<String, String> redisStringSortedSetOps;
    private SetOperations<String, String> redisStringSetOps;
    private ListOperations<String, String> redisStringListOps;
    private ValueOperations<String, String> redisStringValueOps;


    @Autowired
    public Persistency(StringRedisTemplate stringRedisTemplate) {
        this.redisStringHashOps = stringRedisTemplate.opsForHash();
        this.redisStringSetOps = stringRedisTemplate.opsForSet();
        this.redisStringSortedSetOps = stringRedisTemplate.opsForZSet();
        this.redisStringListOps = stringRedisTemplate.opsForList();
        this.redisStringValueOps = stringRedisTemplate.opsForValue();
        this.userid = new RedisAtomicLong("userid", stringRedisTemplate.getConnectionFactory());
    }

    public boolean userExists(User user) {
        if (user == null) {
            throw new NullPointerException("User object cannot be null");
        }

        List<String> users = getAllUsers();
        return users.contains(user.getUsername());
    }

    private List<String> getAllUsers() {
        Set<String> users =redisStringSetOps.members("allusers");
        List<String> usernames = new ArrayList<>();
        for(String userelement : users) {
            String[] elementParts = userelement.split(":");
            usernames.add(elementParts[1]);
        }

        return usernames;
    }

    public boolean createUser(User user) {
        String key = "user:" + user.getUsername();
        redisStringHashOps.put(key, "id", String.valueOf(userid.getAndIncrement()));
        redisStringHashOps.put(key, "username", user.getUsername());
        redisStringHashOps.put(key, "password", user.getPassword());

        redisStringSetOps.add("allusers", key);
        return false;
    }

    public User getUser(User user) {
        User savedUser = new User();
        if (userExists(user)) {
            // TODO: Create user object from the data in the redis database.
            return savedUser;
        } else {
            return null;
        }
    }
}