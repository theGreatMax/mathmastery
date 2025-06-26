package com.mathmastery.controllers;

import com.mathmastery.models.TutorBookingRequest;
import com.mathmastery.models.User;
import com.mathmastery.repositories.TutorBookingRequestRepository;
import com.mathmastery.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/tutor/bookings")
public class TutorBookingRestController {

    private final UserRepository userRepository;
    private final TutorBookingRequestRepository bookingRequestRepository;

    public TutorBookingRestController(UserRepository userRepository, TutorBookingRequestRepository bookingRequestRepository) {
        this.userRepository = userRepository;
        this.bookingRequestRepository = bookingRequestRepository;
    }

    @PostMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam String action,
            Principal principal) {

        User tutor = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        TutorBookingRequest booking = bookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getTutor().getId().equals(tutor.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized update attempt");
        }

        if (!action.equalsIgnoreCase("approve") && !action.equalsIgnoreCase("reject")) {
            return ResponseEntity.badRequest().body("Invalid action");
        }

        booking.setStatus(
            action.equalsIgnoreCase("approve")
                ? TutorBookingRequest.Status.APPROVED
                : TutorBookingRequest.Status.REJECTED);

        bookingRequestRepository.save(booking);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", action.equalsIgnoreCase("approve") ? "Booking approved" : "Booking rejected",
            "bookingId", bookingId,
            "status", booking.getStatus().toString()
        ));
    }
}
