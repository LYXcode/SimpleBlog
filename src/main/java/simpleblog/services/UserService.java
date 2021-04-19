package simpleblog.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simpleblog.entities.Post;
import simpleblog.entities.Role;
import simpleblog.entities.User;
import simpleblog.repositories.PostRepository;
import simpleblog.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public void register(User user) {
        userRepository.save(user);
    }

    public List<User> listAllUser() {
        return userRepository.findAll();
    }


    public User findUserByName(String username){

        return userRepository.findByUsername(username);
    }
    
    @PostConstruct
    void initAdmin() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(new Role("admin"));
        Integer passwordSalt = new Random().nextInt();
        String hashedPassword = new Md5Hash("admin", ByteSource.Util.bytes(passwordSalt.toString()), 1024).toBase64();
        User admin = new User("admin", hashedPassword, roles);
        admin.setPasswordSalt(passwordSalt);
        userRepository.save(admin);

        Post post = new Post();
        post.setTitle("admin title");
        post.setBody("admin post body");
        post.setDateCreated(new Date());
        post.setCreatorId(userRepository.findByUsername("admin").getId());
        post.setCreatorName(userRepository.findByUsername("admin").getUsername());
        postRepository.save(post);
        
    }

}
