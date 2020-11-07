package pl.sda.spring_start.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sda.spring_start.model.User;
import pl.sda.spring_start.repository.UserRepository;

// implementuje logikę aplikacji
@Service
public class UserService {
    // wstrzykowanie zależności
    @Autowired
    UserRepository userRepository;

    public void registerUser (User user) {
        userRepository.save(user);
    }
}
