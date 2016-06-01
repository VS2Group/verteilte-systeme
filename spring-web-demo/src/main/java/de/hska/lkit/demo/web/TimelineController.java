package de.hska.lkit.demo.web;

/**
 * Created by patrickkoenig on 01.06.16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.html.HTMLAnchorElement;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class TimelineController {


    private Persistency persistency;

    @Autowired
    public TimelineController(Persistency persistency) {
        this.persistency = persistency;
    }


    @RequestMapping(value = "/timeline/{username}", method = RequestMethod.GET)
    public String showUserTimeline(@PathVariable("username") String username, @ModelAttribute Timeline timeline,
                                   Model model) {


        model.addAttribute("timeline", timeline);
        return "my-stream";
    }

    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public String showUserTimeline(@ModelAttribute Timeline timeline, HttpSession session,
                                   Model model) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:login";
        }
        String username = session.getAttribute("username").toString();
        System.out.println("Username in session: {" + username + "}");

        model.addAttribute("timeline", timeline);
        return "all-posts";
    }


}
