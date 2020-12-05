package pl.sda.spring_start.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;
    @Type(type = "text")
    private String message;
    private LocalDateTime dateAdded = LocalDateTime.now();
    @ManyToOne
    private User commentAuthor;
    @ManyToOne
    private Post rootPost;

    public Comment(String message) {
        this.message = message;
    }
}