package de.hska.lkit.demo.web;


import jdk.nashorn.tools.ShellFunctions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by Tobias Kerst on 29.05.2016.
 */
@Controller
public class RegisterController {

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerUser (@ModelAttribute UserRegister user, Model model) {
        if (!user.getPassword().equals(user.getPasswordRepeat())) {
            System.out.println("Wrong password!");
            model.addAttribute("error", "Passwords don't match");
            return "register";

        } else {
            System.out.println("Passwords match");
        }
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String showRegister (@ModelAttribute UserRegister user) {
        return "register";
    }

}
