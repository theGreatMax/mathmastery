package com.mathmastery.controllers;



import com.mathmastery.models.User;
import com.mathmastery.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

;
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping("/login") 
    public String login() {
        return "login"; // Returns login.html
    }


    @GetMapping("/student/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // this is the logged-in username
        // Use your repository to find the user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));



        model.addAttribute("name", user.getFullname());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("school", user.getSchool());


        model.addAttribute("user", user);

        return "dashboard"; // dashboard.html//
    }



}
