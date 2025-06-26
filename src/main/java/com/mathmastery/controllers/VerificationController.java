package com.mathmastery.controllers;

import com.mathmastery.models.User;
import com.mathmastery.repositories.UserRepository;
import com.mathmastery.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VerificationController {
    private final UserService userService;
    private final UserRepository userRepository;

    public VerificationController(UserService userService, UserRepository userRepository) {
        this.userService = userService;

        this.userRepository = userRepository;
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        boolean verified = userService.verifyEmail(token);
        model.addAttribute("verified", verified);
        return "verification-result";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        // Update email now
        user.setEmail(user.getPendingEmail());
        user.setPendingEmail(null);
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("toastMessage", "Email verified successfully!");
        redirectAttributes.addFlashAttribute("toastSuccess", true);

        return "redirect:/dashboard";
    }

}
