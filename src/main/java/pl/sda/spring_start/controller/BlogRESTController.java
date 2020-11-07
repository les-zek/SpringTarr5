package pl.sda.spring_start.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sda.spring_start.model.User;
import pl.sda.spring_start.service.UserService;

import java.time.LocalDateTime;

// klasa mapująca żądania prokołu http - adres lokalny http://localhost:8080
//@Controller       //- mapuje żądanie i zwraca widok html
@RestController     //- mapuje żądania i dane REST - Reprentative State Transfer
public class BlogRESTController {
    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public void registerUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        User user = new User(email, password, LocalDateTime.now(), false);
        userService.registerUser(user);
    }
    // GET      - SELECT - pobiera zawartość z bazy i zwraca obiekt lub listę obiektów
    // POST     - INSERT - wprowadza dane do bazy i nic nie zwraca
    // PUT      - UPDATE - edytuje dane z bazy i nic nie zwraca
    // DELETE   - DELETE - usuwanie obiektu lub listy obiektów z bazy

    @GetMapping("/")                         // localhost:8080/
    public String home() {
        return "Hello in homepage";
    }

    @GetMapping("/home/{name}&{status}")     // localhost:8080/home/Michal&true
    public String homeWithName(
            @PathVariable("name") String name,
            @PathVariable("status") Boolean status
    ) {
        return status ? "Hello " + name + " in hompage" : "Your account is locked";
    }

    @GetMapping("/credentials")         //localhost:8080/credentials?login=miki&password=qwe123
    public String getCredentials(
            @RequestParam("login") String login,
            @RequestParam("password") String password
    ) {
        return String.format("login : %s \npassword : %s", login, password);
    }


}