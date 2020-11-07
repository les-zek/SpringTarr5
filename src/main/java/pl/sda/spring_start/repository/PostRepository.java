package pl.sda.spring_start.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.spring_start.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
