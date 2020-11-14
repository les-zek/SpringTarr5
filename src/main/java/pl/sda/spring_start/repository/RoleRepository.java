package pl.sda.spring_start.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.spring_start.model.Role;

public interface RoleRepository extends JpaRepository <Role, Integer> {
}
