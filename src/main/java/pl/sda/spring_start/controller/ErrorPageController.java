package pl.sda.spring_start.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.sda.spring_start.service.UserService;

@Controller
public class ErrorPageController implements ErrorController {
    @Autowired
    private UserService userService;

    @Override
    public String getErrorPath() {  // przekierowanie na adres /error dla błędów 4XX
        return "/error";
    }
    @GetMapping("/error")           // mapowanie przekierowania na adres /error
    public String getErrorPage(
            Model model,
            Authentication auth
    ){
        model.addAttribute("auth", userService.getCredentials(auth));
        return "errorPage";         // zwrócenie szablonu widoku Thymeleaf
    }
}