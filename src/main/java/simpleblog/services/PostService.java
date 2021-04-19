package simpleblog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import simpleblog.entities.Post;
import simpleblog.entities.User;
import simpleblog.repositories.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public void insert(@RequestBody Post post){
        postRepository.save(post);
    }

    public List<Post> findPostsByCreator(User creator){

       return postRepository.findByCreatorId(creator.getId());
    }

    public Optional<Post> findPostsById(Long id){

        return postRepository.findById(id);
     }
}
