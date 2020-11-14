package pl.sda.spring_start.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.sda.spring_start.model.Category;
import pl.sda.spring_start.model.Post;
import pl.sda.spring_start.model.PostDto;
import pl.sda.spring_start.service.PostService;
import pl.sda.spring_start.service.UserService;

import javax.validation.Valid;
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
    public String home(Model model) {   // wywołaj metodę home()
        // dodaje atrybut do obiektu model, który może być przekazany do widoku
        // model.addAttribute(nazwaAtrybutu, wartość);
        model.addAttribute("posts", postService.getAllPosts());
        return "index";     // zwracającą nazwę dokumentu html który ma być wyświetlany
    }

    @GetMapping("/posts&{postId}")
    public String getPost(@PathVariable("postId") int postId, Model model) {
        Optional<Post> postOptional = postService.getPostById(postId);
        postOptional.ifPresent(post -> model.addAttribute("post", post));
        return "post";
    }

    @GetMapping("/addPost")
    public String addPost(Model model) {
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("categories", new ArrayList<>(Arrays.asList(Category.values())));
        return "addPost";
    }

    @PostMapping("/addPost")
    public String addPost(
            @Valid
            @ModelAttribute PostDto postDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().stream().forEach(fieldError -> System.out.println(fieldError.toString()));
            return "addPost";
        }
        postService.addPost(postDto.getTitle(), postDto.getContent(), postDto.getCategory(),
                userService.getUserById(4).get());
        return "redirect:/";
    }
}