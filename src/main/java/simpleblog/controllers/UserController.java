package simpleblog.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import simpleblog.entities.Role;
import simpleblog.entities.User;
import simpleblog.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    String register(@RequestBody User user) {

        if(userService.findUserByName(user.getUsername()) !=null){
            return "user " + user.getUsername() + " already exsit";
        }
        else{
            List<Role> roles = new ArrayList<Role>();
            roles.add(new Role("normaluser"));
            user.setRoles(roles);
     
            Integer passwordSalt = new Random().nextInt();
            String hashedPassword = new Md5Hash(user.getPassword(), ByteSource.Util.bytes(passwordSalt.toString()), 1024).toBase64();
            
            user.setPassword(hashedPassword);
            user.setPasswordSalt(passwordSalt);
            userService.register(user);
            return "register successfully";
        }
        

    }

    @PostMapping("/loginAuth")
    String usrLogin(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || password == null) {
            return "enter the username and password";
        }

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {

            return "用户名不存在！";
        } catch (AuthenticationException e) {

            return "账号或密码错误！";
        } catch (AuthorizationException e) {

            return "没有权限";
        }
        return "login success";

    }

    // @RequiresRoles("admin")
    @GetMapping(value = "/users")

    List<User> getAllUsers() {
   
        return userService.listAllUser();
    }

    @RequiresRoles("admin")
    @GetMapping(value = "/user/{username}")

    User findUserByName(@PathVariable("username") String username) {
        return userService.findUserByName(username);
    }
}
