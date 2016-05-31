package de.hska.lkit.demo.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Tobias Kerst on 29.05.2016.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/")
    public String showMainPage(Model model) {
        return "redirect:login";
    }

    @RequestMapping(value = "login")
    public String loginUser(@ModelAttribute User user, Model model) {
        if (user != null) {
            System.out.println("User tried to log in: " + user);
        }
        return "login";
    }

}
