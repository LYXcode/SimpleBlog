package simpleblog.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import simpleblog.entities.Post;
import simpleblog.entities.User;
import simpleblog.services.*;

@RestController
public class BlogController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    // @GetMapping(value = "/")
    // public String index() {
    //     return "index";
    // }

    @GetMapping(value = "/posts")
    @RequiresAuthentication
    public List<Post> getAlPostsByCreator() {
        String username = SecurityUtils.getSubject().getPrincipals().toString();
        User creator = userService.findUserByName(username);
        return postService.findPostsByCreator(creator);

    }

    @PostMapping(value = "/post")
    @RequiresAuthentication
    public void publishPost(@RequestBody Post post) {
        String username = SecurityUtils.getSubject().getPrincipals().toString();
        User creator = userService.findUserByName(username);
        if (post.getDateCreated() == null) {
            post.setDateCreated(new Date());
        }
        if(post.getCreatorId() == null){
            post.setCreatorId(creator.getId());
        }
        if(post.getCreatorName()  == null){
            post.setCreatorName(creator.getUsername());
        }
        postService.insert(post);
    }

    @GetMapping(value = "allposts")
    // @RequiresRoles("admin")
    public List<Post> getAllPosts(){
       return postService.getAllPosts();
    }

    @GetMapping(value = "post/{id}")
    // @RequiresRoles("admin")
    public Optional<Post> getPostById(@PathVariable Long id){
       return postService.findPostsById(id);
    }

}