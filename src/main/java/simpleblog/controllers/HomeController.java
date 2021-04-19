package simpleblog.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/myposts")
    public String myPost() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "myPost";
        } else {
            return "login";
        }

    }

    @GetMapping(value = "/topost")
    public String toPost() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "toPost";
        } else {
            return "login";
        }

    }

    @GetMapping(value = "/newuser")
    public String register() {

        return "register";

    }



    @GetMapping(value = "/the_post/{id}")
    public String postDetail() {

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "postDetail";
        } else {
            return "login";
        }
    }

    @GetMapping(value = "/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";

    }
}
