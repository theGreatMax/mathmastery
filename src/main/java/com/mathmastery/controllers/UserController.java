package com.mathmastery.controllers;

import com.mathmastery.models.User;
import com.mathmastery.repositories.UserRepository;
import com.mathmastery.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;

    }

    // PROFILE update
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam String email,
            @RequestParam String school,
            @RequestParam int grade,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.updateProfile(user.getId(), school, grade);

        if(user.getSchool().equals(school)){
            redirectAttributes.addFlashAttribute("gradeChangeRequest", true);
        }

        if (!user.getEmail().equals(email)) {
            userService.requestEmailChange(user.getId(), email);

            redirectAttributes.addFlashAttribute("emailChangeRequested", true);

        }

        return "redirect:/dashboard";
    }


    // PASSWORD update
    @PostMapping("/update-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            Principal principal,
            Model model
    ) {
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "New passwords do not match");
            return "redirect:/dashboard"; // You can handle error messages properly if needed
        }

        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            userService.changePassword(user.getId(), currentPassword, newPassword);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/dashboard"; // again, you can improve error handling
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(Principal principal) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPendingEmail() == null || user.getEmailVerificationToken() == null) {
            return ResponseEntity.badRequest().body("No pending email verification");
        }

        userService.resendVerificationEmail(user);
        return ResponseEntity.ok("Verification email resent");
    }

    @GetMapping("/resend-cooldown")
    public ResponseEntity<Map<String, Object>> getResendCooldown(Principal principal) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        if (user.getLastEmailResendTime() != null) {
            Duration duration = Duration.between(user.getLastEmailResendTime(), LocalDateTime.now());
            long secondsPassed = duration.getSeconds();
            long secondsRemaining = 60 - secondsPassed;
            response.put("secondsRemaining", Math.max(secondsRemaining, 0));
        } else {
            response.put("secondsRemaining", 0L);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/cancel-email-change")
    @ResponseBody
    public ResponseEntity<String> cancelEmailChange(Principal principal) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.cancelPendingEmail(user.getId());

        return ResponseEntity.ok("Canceled");
    }





}
