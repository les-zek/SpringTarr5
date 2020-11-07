package pl.sda.spring_start.model;
// klasa determinująca strukturę danych w projekcie

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity         // determinuje mapowanie klasy na tabelkę DB
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id                                                     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // auto inkrementacja
    private int userId;             // auto inkrementowany klucz głównym tabeli
    private String email;
    private String password;
    private LocalDateTime registrationDateTime;
    private boolean status;

    public User(String email, String password, LocalDateTime registrationDateTime, boolean status) {
        this.email = email;
        this.password = password;
        this.registrationDateTime = registrationDateTime;
        this.status = status;
    }
}