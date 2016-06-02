package de.hska.lkit.demo.web.controller;

import de.hska.lkit.demo.web.model.timeline.Timeline;
import de.hska.lkit.demo.web.model.user.User;
import de.hska.lkit.demo.web.persistency.Persistency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrickkoenig on 02.06.16.
 */
@Controller
public class SearchController {


    private Persistency persistency;

    @Autowired
    public SearchController(Persistency persistency) {
        this.persistency = persistency;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showUserTimeline(@RequestParam(value = "searchterm") String searchterm, @ModelAttribute Timeline
            timeline, Model model, HttpSession session) {

        if (session == null || session.getAttribute("username") == null) {
            return "redirect:../login";
        }

        List<String> foundUserNames = persistency.searchUsers(searchterm);
        List<User> foundUser = new ArrayList<>();

        for (String username : foundUserNames) {
            User tempUser = new User();
            tempUser.setFollower(persistency.getFollowerIds(username));
            tempUser.setUsername(username);
            foundUser.add(tempUser);
        }
        model.addAttribute("foundUsers", foundUser);
        model.addAttribute("searchString", searchterm);
        return "search";
    }
}
