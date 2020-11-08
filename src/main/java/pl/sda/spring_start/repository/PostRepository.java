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
    // filtrowanie po kategorii i sortowanie po dacie
    List<Post> findAllByCategory(Category category, Sort sort);

    // szukanie po autorze i kategorii
    List<Post> findAllByCategoryAndAuthor(Category category, User author, Sort sort);

    // szukanie słów kluczowych w tytule i zawartości
    List<Post> findAllByTitleInOrContentIn(List<String> titleWords, List<String> content);

    // grupowanie po kategorii - statystyka
    @Query(
            value = "SELECT p.category, count(*) from Post p group by p.category order by 2 DESC",
            nativeQuery = true
    )
    Map<Object, Object> postStatistics();
}
