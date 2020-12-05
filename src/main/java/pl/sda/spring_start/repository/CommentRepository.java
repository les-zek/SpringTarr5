package pl.sda.spring_start.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.spring_start.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}