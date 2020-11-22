package pl.sda.spring_start.controller;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller     // klasa mapująca url na wywołanie metody i zwracająca widok html
public class BlogController {
    private UserService userService;
    private PostService postService;

    @Autowired
    public BlogController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }
    @GetMapping("/addDislike&{pageIndex}&{postId}")
    public String addDislike(
            @PathVariable("postId") int postId,
            @PathVariable("pageIndex") Integer pageIndex,
            Authentication auth){
        String email = userService.getCredentials(auth).getUsername();
        postService.addDislike(postId, userService.getUserByEmail(email).get());
        return "redirect:/page="+pageIndex;
    }
    @GetMapping("/addLike&{pageIndex}&{postId}")
    public String addLike(
            @PathVariable("postId") int postId,
            @PathVariable("pageIndex") Integer pageIndex,
            Authentication auth){
        String email = userService.getCredentials(auth).getUsername();
        postService.addLike(postId, userService.getUserByEmail(email).get());
        return "redirect:/page="+pageIndex;
    }

    @GetMapping("/")        // na adresie localhost:8080/
    public String home(
            Model model,
            Authentication auth    // można wydobyć dane logowania gdy nie jest null
    ) {   // wywołaj metodę home()
        // dodaje atrybut do obiektu model, który może być przekazany do widoku
        // model.addAttribute(nazwaAtrybutu, wartość);
        model.addAttribute("posts", postService.getAllPosts(0, Sort.Direction.DESC, "dateAdded"));    // pierwsze 5 postów
        model.addAttribute("auth", userService.getCredentials(auth));
        model.addAttribute("pagesIndexes", postService.generatePagesIndexes(postService.getAllPosts()));
        model.addAttribute("pageIndex", 1);
        return "index";     // zwracającą nazwę dokumentu html który ma być wyświetlany
    }
    @GetMapping("/page={pageIndex}&{field}&{sortDirection}")
    public String home(
            @PathVariable("pageIndex") int pageIndex,
            @PathVariable("field") String field,
            @PathVariable("sortDirection") String sortDirection,
            Model model,
            Authentication auth
    ){
        // w sytuacji sortowani po like-ach i dislike-ach

        model.addAttribute("posts",
                postService.getAllPosts(pageIndex - 1,
                        sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        field
                ));
        model.addAttribute("auth", userService.getCredentials(auth));
        model.addAttribute("pagesIndexes", postService.generatePagesIndexes(postService.getAllPosts()));
        model.addAttribute("pageIndex", pageIndex);
        return "index";
    }

    @GetMapping("/posts&{postId}")
    public String getPost(
            @PathVariable("postId") int postId, Model model, Authentication auth
    ) {
        Optional<Post> postOptional = postService.getPostById(postId);
        postOptional.ifPresent(post -> model.addAttribute("post", post));
        model.addAttribute("auth", userService.getCredentials(auth));
        return "post";
    }

    @GetMapping("/addPost")                 // przejście metodą GET na stronę formularze
    public String addPost(Model model, Authentication auth) {     // i przekazanie pustego obiektu Post
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
        model.addAttribute("auth", userService.getCredentials(auth));
        return "addPost";                   // tu znajduje się formularz i jest uzupłeniany przez użytkownika
        // gdy wprowadza pola do formularza to set-uje pola klasy Post
    }

    @PostMapping("/addPost")                // przekazanie parametrów z formularza metodą POST
    public String addPost(
            @Valid                                // zwraca błędy walidacji obiektu PostDto
            @ModelAttribute PostDto postDto,      // obiekt model przekazuje obiekt post do metody
            BindingResult bindingResult,          // obiekt zawierający błędy walidacji
            Model model,
            Authentication auth
    ) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().stream().forEach(fieldError -> System.out.println(fieldError.toString()));
            model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
            return "addPost";               // gdy są błędy walidacji to wyświetl z powrotem formularz i nic nie zapisuj
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String loggedEmail = userDetails.getUsername();         // adres email z pobrany z danych logowania
        userDetails.getAuthorities().stream().forEach(o -> System.out.println(o));
        // zapisanie nowego posta do db
        postService.addPost(postDto.getTitle(), postDto.getContent(), postDto.getCategory(),
                userService.getUserByEmail(loggedEmail).get());  // przypisanie dodawanego posta do zalogowanego użytkownika
        return "redirect:/";                // przekierowuje na ades, który zwraca jakiś widok
    }

    @GetMapping("/register")
    public String addUser(Model model, Authentication auth) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("auth", userService.getCredentials(auth));
        return "addUser";
    }

    @PostMapping("/register")
    public String addUser(
            @Valid @ModelAttribute UserDto userDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }
        if (userService.getUserByEmail(userDto.getEmail()).isPresent()) {     // istnieje już taki email
            model.addAttribute("emailError", "e-mail address is not unique");
            return "addUser";
        }
        userService.registerUser(new User(userDto.getEmail(), userDto.getPassword(),
                LocalDateTime.now(), true));
        return "redirect:/";
    }

    @GetMapping("/login")       // adres zwracający frmularz logowania
    public String login(Model model, Authentication auth) {
        model.addAttribute("auth", userService.getCredentials(auth));
        return "login";         // zwrócenie szablonu widoku o nazwie login.html
    }

    @GetMapping("/login&error={loginError}")    // adres zwracający formularz logowania gdy wystąpiły błędy logowania
    public String login(@PathVariable("loginError") Boolean loginError, Model model,
                        Authentication auth) {
        System.out.println(loginError.getClass());
        model.addAttribute("loginError", loginError);
        model.addAttribute("auth", userService.getCredentials(auth));
        return "login";
    }

    @GetMapping("/deletePost&{postId}")
    public String deletePost(@PathVariable("postId") int postId, Model model, Authentication auth) {
        if (postService.getPostById(postId).isPresent()) {
            postService.deletePostById(postId);
            return "redirect:/";
        }
        model.addAttribute("errorMessage", "Delete action aborted! There is not post with id = " + postId);
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("auth", userService.getCredentials(auth));
        return "index";

    }

    @GetMapping("/editPost&{postId}")
    public String updatePost(
            @PathVariable("postId") Integer postId, Model model, Authentication auth) {
        if (postService.getPostById(postId).isPresent()) {
            Post postToUpdate = postService.getPostById(postId).get();
            PostDto postDto = new PostDto(
                    postToUpdate.getTitle(), postToUpdate.getContent(), postToUpdate.getCategory());
            model.addAttribute("postDto", postDto);
            model.addAttribute("postId", postId);
            model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
            model.addAttribute("auth", userService.getCredentials(auth));
            return "addPost";
        }
        model.addAttribute("errorMessage", "Update action aborted! There is not post with id = " + postId);
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("auth", userService.getCredentials(auth));
        return "index";     // gdy nie ma posta o określonym id przekierowujemy na stronę domową
    }

    @PostMapping("/editPost&{postId}")
    public String updatePost(
            @PathVariable("postId") int postId,
            @Valid @ModelAttribute("postDto") PostDto postDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
            return "addPost";
        }
        if (postService.editPost(postId, postDto)) {
            return "redirect:/posts&" + postId;
        }
        return "redirect:/";
    }
}