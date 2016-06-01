package de.hska.lkit.demo.web;

/**
 * Created by Tobias Kerst on 01.06.16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {


    private Persistency persistency;

    @Autowired
    public PostController(Persistency persistency) {
        this.persistency = persistency;
    }


    @RequestMapping(value = "/create-post", method = RequestMethod.GET)
    public String showUserTimeline(HttpSession session, Model model) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:./login";
        }

        String username = session.getAttribute("username").toString();
        return "create-post";
    }
}
