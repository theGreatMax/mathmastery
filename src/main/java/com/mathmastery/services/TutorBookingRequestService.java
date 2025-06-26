package com.mathmastery.services;

import com.mathmastery.models.TutorBookingRequest;
import com.mathmastery.repositories.TutorBookingRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorBookingRequestService {

    private final TutorBookingRequestRepository bookingRepo;
    private final EmailService mailService;

    public TutorBookingRequestService(TutorBookingRequestRepository bookingRepo, EmailService mailService) {
        this.bookingRepo = bookingRepo;
        this.mailService = mailService;
    }

    public List<TutorBookingRequest> getPendingRequests(Long tutorId) {
        return bookingRepo.findByTutorIdAndStatus(tutorId, TutorBookingRequest.Status.PENDING);
    }

    public void updateStatus(Long bookingId, TutorBookingRequest.Status status) {
        TutorBookingRequest booking = bookingRepo.findById(bookingId).orElseThrow();
        booking.setStatus(status);
        bookingRepo.save(booking);

        String studentEmail = booking.getStudent().getEmail();
        String subject = "Your Booking Has Been " + status.name();
        String body = "<p>Your request with <strong>" + booking.getTutor().getFullname() + "</strong> has been <strong>" + status.name().toLowerCase() + "</strong>.</p>" +
                "<p><strong>Time:</strong> " + booking.getSchedule().getDayOfWeek() + " " + booking.getSchedule().getStartTime() + "-" + booking.getSchedule().getEndTime() + "</p>" +
                "<p>Check your dashboard for more details.</p>";

        mailService.sendEmail(studentEmail, subject, body);
    }




}

