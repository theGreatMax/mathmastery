package com.mathmastery.controllers;

import com.mathmastery.dto.UserProgressDTO;
import com.mathmastery.services.UserProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/progress")
public class UserProgressController {

    private final UserProgressService userProgressService;

    public UserProgressController(UserProgressService userProgressService) {
        this.userProgressService = userProgressService;
    }

    @PostMapping
    public ResponseEntity<?> submitProgress(@RequestBody UserProgressDTO dto,
                                            Principal principal) {
        String username = principal.getName(); // Authenticated user
        userProgressService.saveProgress(username, dto);
        return ResponseEntity.ok().build();
    }
}
