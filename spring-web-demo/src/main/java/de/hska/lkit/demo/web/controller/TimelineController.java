package de.hska.lkit.demo.web.controller;

/**
 * Created by patrickkoenig on 01.06.16.
 */

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

        if (isMyStream(username)) {
            profileUser.setUsername(session.getAttribute("username").toString());
            model.addAttribute("streamName", "My Stream");
        } else {
            profileUser.setUsername(username);
            model.addAttribute("streamName", "von " + username);
        }

        model.addAttribute("pageuser", profileUser.getUsername());
        model.addAttribute("profilepicture", "/images/profile-pictures/" + profileUser.getProfilePicture());
        model.addAttribute("timeline", timeline);
        return "my-stream";
    }

    private boolean isMyStream(@PathVariable("username") String username) {
        return (username.equals("my-stream"));
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public String showUserTimeline(@ModelAttribute Timeline timeline, HttpSession session,
                                   Model model) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:./login";
        }
        String username = session.getAttribute("username").toString();
        System.out.println("Username in session: {" + username + "}");

        model.addAttribute("timeline", timeline);
        return "all-posts";
    }


}
