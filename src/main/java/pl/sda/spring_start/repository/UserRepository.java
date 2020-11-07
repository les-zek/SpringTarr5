package pl.sda.spring_start.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.spring_start.model.User;

// repozytorium interfejs implementujący zapytania SQL
@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
}
