package de.hska.lkit.demo.web.controller;

import de.hska.lkit.demo.web.model.timeline.Timeline;
import de.hska.lkit.demo.web.persistency.Persistency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
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

    @RequestMapping(value = "/search/{searchterm}", method = RequestMethod.GET)
    public String showUserTimeline(@PathVariable("searchterm") String searchterm, @ModelAttribute Timeline timeline,
                                   Model model, HttpSession session) {

        if (session == null || session.getAttribute("username") == null) {
            return "redirect:../login";
        }

        List<String> foundUsers = persistency.searchUsers(searchterm);
        System.out.println(foundUsers.toString());


        return "search";
    }
}
