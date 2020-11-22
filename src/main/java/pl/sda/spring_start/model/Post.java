package pl.sda.spring_start.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
// import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private String title;
    @Type(type = "text")
    private String content;
    private LocalDateTime dateAdded;
    private Category category;
    @ManyToOne(
            fetch = FetchType.EAGER
    )

    private User author;
    @ManyToMany(
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="likes",
    joinColumns = @JoinColumn(name="post_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes = new HashSet<>();
    public Post(String title, String content, LocalDateTime dateAdded, Category category, User author) {
        this.title = title;
        this.content = content;
        this.dateAdded = dateAdded;
        this.category = category;
        this.author = author;
    }
}
