package pl.sda.spring_start.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.sda.spring_start.model.*;
import pl.sda.spring_start.service.PostService;
import pl.sda.spring_start.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Controller     // klasa mapująca url na wywołanie metody i zwracająca widok html
public class BlogController {
    private UserService userService;
    private PostService postService;
    @Autowired
    public BlogController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }
    @GetMapping("/")        // na adresie localhost:8080/
    public String home(
            Model model,
            Authentication auth     // można wydobyć dane logowania gdy nie jest null
    ){   // wywołaj metodę home()
        // dodaje atrybut do obiektu model, który może być przekazany do widoku
        // model.addAttribute(nazwaAtrybutu, wartość);
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("auth", auth);
        return "index";     // zwracającą nazwę dokumentu html który ma być wyświetlany
    }
    @GetMapping("/posts&{postId}")
    public String getPost(
            @PathVariable("postId") int postId, Model model, Authentication auth
    ){
        Optional<Post> postOptional = postService.getPostById(postId);
        postOptional.ifPresent(post -> model.addAttribute("post", post));
        model.addAttribute("auth", auth);
        return "post";
    }
    @GetMapping("/addPost")                 // przejście metodą GET na stronę formularze
    public String addPost(Model model, Authentication auth){     // i przekazanie pustego obiektu Post
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
        model.addAttribute("auth", auth);
        return "addPost";                   // tu znajduje się formularz i jest uzupłeniany przez użytkownika
        // gdy wprowadza pola do formularza to set-uje pola klasy Post
    }
    @PostMapping("/addPost")                // przekazanie parametrów z formularza metodą POST
    public String addPost(
            @Valid                                // zwraca błędy walidacji obiektu PostDto
            @ModelAttribute PostDto postDto,      // obiekt model przekazuje obiekt post do metody
            BindingResult bindingResult,          // obiekt zawierający błędy walidacji
            Model model
    ){
        if(bindingResult.hasErrors()){
            bindingResult.getFieldErrors().stream().forEach(fieldError -> System.out.println(fieldError.toString()));
            model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
            return "addPost";               // gdy są błędy walidacji to wyświetl z powrotem formularz i nic nie zapisuj
        }
        // zapisanie nowego posta do db
        postService.addPost(postDto.getTitle(), postDto.getContent(), postDto.getCategory(),
                userService.getUserById(3).get());  // rozwiązanie na chwilę !!!
        return "redirect:/";                // przekierowuje na ades, który zwraca jakiś widok
    }
    @GetMapping("/register")
    public String addUser(Model model, Authentication auth){
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("auth", auth);
        return "addUser";
    }
    @PostMapping("/register")
    public String addUser(
            @Valid @ModelAttribute UserDto userDto,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()){
            return "addUser";
        }
        if(userService.getUserByEmail(userDto.getEmail()).isPresent()){     // istnieje już taki email
            model.addAttribute("emailError", "e-mail address is not unique");
            return "addUser";
        }
        userService.registerUser(new User(userDto.getEmail(), userDto.getPassword(),
                LocalDateTime.now(), true));
        return "redirect:/";
    }
    @GetMapping("/login")       // adres zwracający frmularz logowania
    public String login(Model model, Authentication auth){
        model.addAttribute("auth", auth);
        return "login";         // zwrócenie szablonu widoku o nazwie login.html
    }
    @GetMapping("/login&error={loginError}")    // adres zwracający formularz logowania gdy wystąpiły błędy logowania
    public String login(@PathVariable("loginError") Boolean loginError, Model model,
                        Authentication auth){
        System.out.println(loginError.getClass());
        model.addAttribute("loginError", loginError);
        model.addAttribute("auth", auth);
        return "login";
    }

}