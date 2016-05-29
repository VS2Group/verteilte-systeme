package de.hska.lkit.demo.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tobias Kerst on 29.05.2016.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "login")
    public String showLogin(@ModelAttribute User user, Model mode) {
        return "login";
    }
}
