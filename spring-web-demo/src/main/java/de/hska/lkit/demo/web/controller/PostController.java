package de.hska.lkit.demo.web.controller;

/**
 * Created by Tobias Kerst on 01.06.16.
 */

import de.hska.lkit.demo.web.model.Greeting;
import de.hska.lkit.demo.web.model.post.Message;
import de.hska.lkit.demo.web.persistency.Persistency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String showPostForm(@ModelAttribute Message message, HttpSession session, Model model) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:./login";
        }

        return "create-post";
    }

    @RequestMapping(value = "/create-post", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Message message, HttpSession session, Model model) {
        if (session == null || session.getAttribute("username") == null) {
            return "redirect:./login";
        }
        String username = session.getAttribute("username").toString();

        if (message.getContent().length() >= 140) {
            model.addAttribute("error", "Dein Post ist länger als 140 Zeichen (" + message.getContent().length() + "" +
                    " Zeichen). Bitte  halte dich etwas kürzer!");
            return "./create-post";
        }
        persistency.createPost(message, username);
        return "redirect:./timeline";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        return new Greeting("Hello, " + message.getContent() + "!");
    }


}
