package pl.sda.spring_start.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sda.spring_start.model.Category;
import pl.sda.spring_start.model.Post;
import pl.sda.spring_start.model.User;

import java.util.List;
import java.util.Map;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // Filtrowanie po kategorii
    List<Post> findAllByCategory(Category category, Sort sort);

    // Szukanie postów po autorze i kategorii
    List<Post> findAllByCategoryAndAuthor(Category category, User author, Sort sort);

    // Szukanie słów kluczowych w tytule i zawartości posta
    List<Post> findAllByTitleLikeOrContentLike(String titlePattern, String contentPattern);

    // Statystyki postów - grupowanie postów po kategorii
    @Query(
            value = "SELECT p.category, count(*) FROM Post p GROUP BY p.category ORDER BY 2 DESC",
            nativeQuery = true
    )
    List<Object[]> postStatistics();
    @Query(
            value = "SELECT p.* FROM post p ORDER BY (SELECT count(*) FROM likes l WHERE l.post_id = p.post_id) - (SELECT count(*) FROM dislikes d WHERE d.post_id = p.post_id)",
            nativeQuery = true
    )
    List<Object[]> findAllSortedByResultASC();

}