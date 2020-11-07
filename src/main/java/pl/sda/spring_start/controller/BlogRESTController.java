package pl.sda.spring_start.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// klasa mapująca żądania prokołu http - adres lokalny http://localhost:8080
//@Controller       //- mapuje żądanie i zwraca widok html
@RestController     //- mapuje żądania i dane REST - Reprentative State Transfer
public class BlogRESTController {
    // GET      - SELECT - pobiera zawartość z bazy i zwraca obiekt lub listę obiektów
    // POST     - INSERT - wprowadza dane do bazy i nic nie zwraca
    // PUT      - UPDATE - edytuje dane z bazy i nic nie zwraca
    // DELETE   - DELETE - usowanie obiektu lub listy obiektów z bazy
    @GetMapping("/")                         // localhost:8080/
    public String home(){
        return "Hello in homepage";
    }
    @GetMapping("/home/{name}&{status}")     // localhost:8080/home/Michal&true
    public String homeWithName(
            @PathVariable("name") String name,
            @PathVariable("status") Boolean status
    ){
        return status ? "Hello " + name + " in hompage" : "Your account is locked";
    }
    @GetMapping("/credentials")
    public String getCredentials(
            @RequestParam("login") String login,
            @RequestParam("password") String password
    ){
        return String.format("login : %s \npassword : %s", login, password);
    }


}