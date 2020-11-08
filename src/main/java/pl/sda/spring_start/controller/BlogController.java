package pl.sda.spring_start.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Leszek's blog");
        model.addAttribute("subtitle", "Most stupid blog ever");
        return "index";
    }
}
