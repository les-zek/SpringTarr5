package pl.sda.spring_start.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.sda.spring_start.configuration.EncoderAlgorithm;
import pl.sda.spring_start.model.User;
import pl.sda.spring_start.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service            // servis - implementuje logikę aplikacji
public class UserService {
    @Autowired      // wstrzykiwanie zależności
    UserRepository userRepository;
    @Autowired
    EncoderAlgorithm encoderAlgorithm;

    public void registerUser(User user) {

        user.setPassword(encoderAlgorithm.getPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);          // INSERT INTO user values (?,?,?,?)
    }

    public void activateUser(int userId) {   // UPDATE user SET status = 1 WHERE user_id = ?;
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(true);
            userRepository.save(user);      // save gdy jest wywoływana na istniejącym w db obiekcie to działa jak update
        }
    }

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getAllUsersOrderByRegistrationDateTimeDesc() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "registrationDateTime"));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }
}