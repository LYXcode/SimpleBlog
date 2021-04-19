package simpleblog.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import simpleblog.entities.Post;
import simpleblog.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByCreatorId(Long creatorId);
    Optional<Post> findById(Long id);
}
