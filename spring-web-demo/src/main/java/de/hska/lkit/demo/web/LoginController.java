package de.hska.lkit.demo.web;


import org.springframework.beans.factory.annotation.Autowired;
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

    private final Persistency persistency;

    @Autowired
    LoginController(Persistency persistency) {
        this.persistency = persistency;
    }

    @RequestMapping(value = "/")
    public String showMainPage(Model model) {
        return "redirect:login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute User user, Model model) {
        if (user == null) {
            return "login";
        }
        if (persistency.userExists(user)) {
            User savedUser = persistency.getUser(user);
        } else {
            System.out.println("Login failed! User {" + user.getUsername() +"} doesn't exist.");
            model.addAttribute("error", "Login failed. User doesn't exist.");
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String showLoginForm(@ModelAttribute User user) {
        return "login";
    }

}
