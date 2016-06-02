package de.hska.lkit.demo.web.model.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by Tobias Kerst on 29.05.2016.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


public class User implements Serializable {

    private long id;
    private String username;
    private String password;
    private static final long serialVersionUID = 1L;
    private Set<String> following;
    private Set<String> follower;


    public User (long id, String username, String password) {

        this.id=id;
        this.username=username;
        this.password = password;
    }

    public User () {}
    public Set<String> getFollowing() {
        return following;
    }

    public void setFollowing(Set<String> following) {
        this.following = following;
    }

    public Set<String> getFollower() {
        return follower;
    }

    public void setFollower(Set<String> follower) {
        this.follower = follower;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return Integer.toString(Math.abs((this.getUsername().hashCode()) % 150) + 1) + ".png";
    }

    public String toString() {
        return "id: {" + id + "}, username: {" + username + "}, password: {" + password + "}";
    }



}
