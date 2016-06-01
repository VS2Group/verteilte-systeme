package de.hska.lkit.demo.web.controller;



import de.hska.lkit.demo.web.persistency.Persistency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.hska.lkit.demo.web.model.user.UserRegister;

/**
 * Created by Tobias Kerst on 29.05.2016.
 */
@Controller
public class RegisterController {

    private Persistency persistency;

    @Autowired
    public RegisterController(Persistency persistency) {
        this.persistency = persistency;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerUser (@ModelAttribute UserRegister user, Model model) {
        if (!user.getPassword().equals(user.getPasswordRepeat())) {
            System.out.println("Wrong password!");
            model.addAttribute("error", "Passwords don't match");
            return "register";

        } else {
            System.out.println("Passwords match");
            if (!persistency.userExists(user)) {
                persistency.createUser(user);
                System.out.println("User with name {" + user.getUsername() + "} created.");
            } else {
                model.addAttribute("error", "User already exists");
                System.out.println("User with username {" + user.getUsername() + "} already exists");
            }
        }
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String showRegister (@ModelAttribute UserRegister user) {
        return "register";
    }


}
