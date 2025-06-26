package com.mathmastery.controllers;

import com.mathmastery.dto.SessionDto;
import com.mathmastery.models.TutorBookingRequest;
import com.mathmastery.models.User;
import com.mathmastery.repositories.TutorBookingRequestRepository;
import com.mathmastery.repositories.UserRepository;
import com.mathmastery.services.SessionService;
import org.springframework.security.web.webauthn.management.MapPublicKeyCredentialUserEntityRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class TutorDashboardController {


    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final TutorBookingRequestRepository bookingRequestRepository;

    public TutorDashboardController(SessionService sessionService, UserRepository userRepository, TutorBookingRequestRepository bookingRequestRepository) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.bookingRequestRepository = bookingRequestRepository;
    }

    @GetMapping("/tutor/dashboard")
    public String dashboard(Model model, Principal principal) {
        User tutor = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SessionDto> sessions = sessionService.getUpcomingSessionsForTutor(tutor.getId());
        List<TutorBookingRequest> pendingRequests = bookingRequestRepository.findByTutorIdAndStatus(tutor.getId(), TutorBookingRequest.Status.PENDING);

        model.addAttribute("tutor", tutor);
        model.addAttribute("sessions", sessions);
        model.addAttribute("pendingRequests", pendingRequests);

        return "tutor-dashboard";
    }


    @PostMapping("/tutor/booking/update")
    public String updateBookingStatus(@RequestParam Long bookingId,
                                      @RequestParam String action,
                                      Principal principal) {
        User tutor = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        TutorBookingRequest booking = bookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getTutor().getId().equals(tutor.getId())) {
            throw new RuntimeException("Unauthorized update attempt");
        }

        booking.setStatus("approve".equalsIgnoreCase(action)
                ? TutorBookingRequest.Status.APPROVED
                : TutorBookingRequest.Status.REJECTED);

        bookingRequestRepository.save(booking);

        return "redirect:/tutor/dashboard?status=" + action.toLowerCase();
    }


}
