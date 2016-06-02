package de.hska.lkit.demo.web.controller;

/**
 * Created by patrickkoenig on 01.06.16.
 */

import de.hska.lkit.demo.web.model.post.Post;
import de.hska.lkit.demo.web.model.user.User;
import de.hska.lkit.demo.web.persistency.Persistency;
import de.hska.lkit.demo.web.model.timeline.Timeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
public class TimelineController {


    private Persistency persistency;

    @Autowired
    public TimelineController(Persistency persistency) {
        this.persistency = persistency;
    }


    @RequestMapping(value = "/timeline/{username}", method = RequestMethod.GET)
    public String showUserTimeline(@PathVariable("username") String username, @ModelAttribute Timeline timeline,
                                   Model model, HttpSession session) {

        if (session == null || session.getAttribute("username") == null) {
            return "redirect:../login";
        }

        User profileUser = new User();

        List<Post> posts;
        String returnPage;
        if (isMyStream(username)) {
            profileUser.setUsername(session.getAttribute("username").toString());
            model.addAttribute("streamName", "Mein Stream");
            posts = persistency.findMyStreamPosts(profileUser.getUsername());
        } else {
            profileUser.setUsername(username);
            if (!persistency.userExists(profileUser)) {
                return "redirect:../timeline";
            }
            model.addAttribute("streamName", "von " + username);
            posts = persistency.findPostsForUser(profileUser.getUsername());
        }

        Set<String> following = persistency.getFollowingIds(profileUser.getUsername());
        Set<String> follower = persistency.getFollowerIds(profileUser.getUsername());

        boolean isFollowingProfileUser = follower.contains(session.getAttribute("username").toString());

        boolean canFollow = !isMyStream(username) && !isFollowingProfileUser && !session.getAttribute("username")
                .equals(username);

        model.addAttribute("posts", posts);
        model.addAttribute("canFollow", canFollow);
        model.addAttribute("canUnfollow", isFollowingProfileUser);
        model.addAttribute("pageuser", profileUser.getUsername());
        model.addAttribute("profilepicture", "/images/profile-pictures/" + profileUser.getProfilePicture());
        model.addAttribute("followerCounter", follower.size() + " Personen folgen " +
                profileUser.getUsername());
        model.addAttribute("followingCounter", following.size() + " Personen " +
                "folgt " + profileUser.getUsername());
        return "my-stream";
    }

    private boolean isMyStream(@PathVariable("username") String username) {
        return (username.equals("my-stream"));
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public String showTimeline(@ModelAttribute Timeline timeline, HttpSession session,
                               Model model) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:./login";
        }

        List<Post> posts = persistency.findGlobalPosts(0, -1);
        model.addAttribute("posts", posts);
        return "all-posts";
    }

    @RequestMapping(value = "/follow/{username}")
    public String followUser(@PathVariable("username") String username, @ModelAttribute Timeline timeline,
                             Model model, HttpSession session) {

        if (session == null || session.getAttribute("username") == null) {
            return "redirect:../login";
        }

        persistency.follow(session.getAttribute("username").toString(), username);

        return "redirect:../timeline/" + username;
    }

    @RequestMapping(value = "/unfollow/{username}")
    public String unfollowUser(@PathVariable("username") String username, Model model, HttpSession session) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:../login";
        }

        persistency.unfollow(session.getAttribute("username").toString(), username);

        return "redirect:../timeline/" + username;
    }

}
