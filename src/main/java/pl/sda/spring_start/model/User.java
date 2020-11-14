package pl.sda.spring_start.model;
// klasa determinująca strukturę danych w projekcie

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity         // determinuje mapowanie klasy na tabelkę DB
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id                                                     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // auto inkrementacja
    private int userId;             // auto inkrementowany klucz głównym tabeli
    @Column(unique = true)
    private String email;
    private String password;
    private LocalDateTime registrationDateTime;
    private boolean status;
    @ManyToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL )
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password, LocalDateTime registrationDateTime, boolean status) {
        this.email = email;
        this.password = password;
        this.registrationDateTime = registrationDateTime;
        this.status = status;
    }
}