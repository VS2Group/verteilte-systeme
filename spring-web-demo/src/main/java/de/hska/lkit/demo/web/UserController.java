package de.hska.lkit.demo.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hska.lkit.demo.web.Redisexample;
import de.hska.lkit.demo.web.User;




@Controller
public class UserController {

    private final Redisexample redisexample;

    @Autowired
    public UserController(Redisexample redisexample) {
        this.redisexample = redisexample;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String addUser(@ModelAttribute User user) {
        return "register";
    }

    @RequestMapping(value = "/createtoby")
    public String saveUser(@ModelAttribute User user, Model model) {

       // redisexample.create(user);
        redisexample.createToby();
        System.out.println("usercreated");
        model.addAttribute("message", "User successfully added");

      // Map<Object, Object> retrievedUsers = redisexample.findAllUsers();

       // model.addAttribute("users", retrievedUsers);
        return "login";
    }

  /* @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
     //   Map<Object, Object> retrievedUsers = redisexample.findAllUsers();
       // model.addAttribute("users", retrievedUsers);
        return "users";
    }*/
}

