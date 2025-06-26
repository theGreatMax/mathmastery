package com.mathmastery.controllers;

import com.mathmastery.models.User;
import com.mathmastery.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")

public class UserApiController {

    private final UserRepository userRepository;

    @Autowired
    public UserApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/update-profile")
    public ResponseEntity<Map<String, Object>> getProfileInfo(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getFullname());
        response.put("email", user.getEmail());
        response.put("pendingEmail", user.getPendingEmail());  // if null, it's fine

        return ResponseEntity.ok(response);
    }

}
