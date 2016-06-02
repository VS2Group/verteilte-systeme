package de.hska.lkit.demo.web.persistency;

/**
 * Created by patrickkoenig on 29.05.16.
 */

import de.hska.lkit.demo.web.model.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;
import de.hska.lkit.demo.web.model.user.User;

import java.util.*;

@Repository
public class Persistency {

    private RedisAtomicLong userid;

    private RedisAtomicLong postid;

    private StringRedisTemplate stringRedisTemplate;

    private HashOperations<String, String, String> redisStringHashOps;
    private SetOperations<String, String> redisStringSetOps;
    private ListOperations<String, String> redisStringListOps;
    private ValueOperations<String, String> redisStringValueOps;
    private ZSetOperations<String, String> redisStringSortedSetOps;


    @Autowired
    public Persistency(StringRedisTemplate stringRedisTemplate) {
        this.redisStringHashOps = stringRedisTemplate.opsForHash();
        this.redisStringSetOps = stringRedisTemplate.opsForSet();
        this.redisStringSortedSetOps = stringRedisTemplate.opsForZSet();
        this.redisStringListOps = stringRedisTemplate.opsForList();
        this.redisStringValueOps = stringRedisTemplate.opsForValue();
        this.userid = new RedisAtomicLong("userid", stringRedisTemplate.getConnectionFactory());
        this.postid = new RedisAtomicLong("postid", stringRedisTemplate.getConnectionFactory());

    }

    public boolean userExists(User user) {
        if (user == null) {
            throw new NullPointerException("User object cannot be null");
        }

        List<String> users = getAllUsers();
        return users.contains(user.getUsername());
    }

    private List<String> getAllUsers() {
        Set<String> users = redisStringSetOps.members("allusers");
        List<String> usernames = new ArrayList<>();
        for (String userelement : users) {
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
        String key = "user:" + user.getUsername();

        if (userExists(user)) {
            savedUser.setId(Long.parseLong(redisStringHashOps.get(key, "id")));
            savedUser.setUsername(redisStringHashOps.get(key, "username"));
            savedUser.setPassword(redisStringHashOps.get(key, "password"));
            return savedUser;

        } else {
            return null;
        }
    }

    public User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        return getUser(user);
    }

    public List<Post> findMyStreamPosts(String username) {
        List<Post> myStreamPosts = new ArrayList<>();
        List<Post> allPosts = findGlobalPosts(0, -1);
        Set<String> following = getFollowingIds(username);

        for (Post postInAllPosts : allPosts) {
            if (following.contains(postInAllPosts.getUserName())) {
                myStreamPosts.add(postInAllPosts);
            }
        }

        return myStreamPosts;
    }

    public void createPost(Post post, String username) {

        // generate a unique id
        long realID = postid.incrementAndGet();
        String id = String.valueOf(realID);
        post.setId(id);


        String key = "posts:" + id;
        redisStringHashOps.put(key, "id", id);
        redisStringHashOps.put(key, "content", post.getContent());
        redisStringHashOps.put(key, "date", String.valueOf(post.getDate().getTime()));

        redisStringSortedSetOps.add("allposts", id, realID * -1);


        String userPostsKey = "user:" + username + ":posts";
        redisStringListOps.leftPush(userPostsKey, id);


        String postToUserKey = "posts:" + id + ":user";
        redisStringValueOps.append(postToUserKey, username);
    }


    public User findUserForPost(String postid) {

        String postToUserKey = "posts:" + postid + ":user";
        String username = redisStringValueOps.get(postToUserKey);
        return getUser(username);
    }

    public List<String> findPostIdsForUser(String username) {
        List<String> posts = new LinkedList<>();
        String userPostsKey = "user:" + username + ":posts";


        if (redisStringSetOps.isMember("allusers", "user:" + username) && redisStringListOps.size(userPostsKey) != 0) {
            posts.addAll(redisStringListOps.range(userPostsKey, 0, -1));
        }

        return posts;
    }

    public List<Post> findPostsForUser(String username) {
        List<String> postIds = findPostIdsForUser(username);
        List<Post> posts = new ArrayList<>();
        for (String postId : postIds) {
            Post postWithId = findPostWithId(Integer.parseInt(postId));
            if (postWithId != null) {
                User user = new User();
                user.setUsername(username);
                postWithId.setUserName(username);
                postWithId.setProfilePicturePath(user.getProfilePicture());
                posts.add(postWithId);
            }
        }
        return posts;
    }

    public List<Post> findGlobalPosts(long start, long end) {
        List<Post> posts = new LinkedList<>();

        Long postCount = redisStringSortedSetOps.zCard("allposts");

        Set<String> postIDs = redisStringSortedSetOps.range("allposts", start, end);

        for (String id : postIDs) {
            User user = findUserForPost(id);
            int intid = Integer.parseInt(id);
            Post post = findPostWithId(intid);
            post.setProfilePicturePath(user.getProfilePicture());
            post.setUserName(user.getUsername());
            posts.add(post);
        }
        return posts;
    }

    public Post findPostWithId(int id) {

        Post post = new Post();
        String key = "posts:" + id;

        post.setId(redisStringHashOps.get(key, "id"));
        post.setContent(redisStringHashOps.get(key, "content"));
        String date = redisStringHashOps.get(key, "date");
        if (date != null) {
            post.setDate(new Date(Long.valueOf(date)));
        }

        if (null == post.getContent() || post.getContent().isEmpty()) {
            return null;
        }

        return post;

    }

    public void follow(String usernameFollower, String usernameFollowing) {
        String followerKey = "user:" + usernameFollower + ":following";
        String followingKey = "user:" + usernameFollowing + ":follower";

        redisStringSetOps.add(followerKey, usernameFollowing);
        redisStringSetOps.add(followingKey, usernameFollower);
    }

    public Set<String> getFollowerIds(String username) {
        Set<String> follower = new HashSet<>();
        String followerKey = "user:" + username + ":follower";

        if (redisStringSetOps.isMember("allusers", "user:" + username) && redisStringSetOps.size(followerKey) != 0) {
            follower.addAll(redisStringSetOps.members(followerKey));
        }

        return follower;

    }

    public Set<String> getFollowingIds(String username) {
        Set<String> following = new HashSet<>();
        String followingKey = "user:" + username + ":following";
        if (redisStringSetOps.isMember("allusers", "user:" + username) && redisStringSetOps.size(followingKey) != 0) {
            following.addAll(redisStringSetOps.members(followingKey));

        }

        return following;

    }

    public List<String> searchUsers(String searchTerm) {

        Set<String> users = redisStringSetOps.members("allusers");
        List<String> usersFound = new ArrayList<>();

        for (String user : users) {
            String username = user.split(":")[1];

            if (searchTerm.endsWith("*")) {
                System.out.println("Fuzzy Search!");
                if (username.toLowerCase().startsWith(searchTerm.substring(0, searchTerm.length() - 1).toLowerCase())) {
                    usersFound.add(username);
                }
            } else {
                if (username.toLowerCase().equals(searchTerm.toLowerCase())) {
                    usersFound.add(username);
                }
            }

        }

        return usersFound;
    }


}