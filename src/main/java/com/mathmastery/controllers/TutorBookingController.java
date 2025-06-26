package com.mathmastery.controllers;

import com.mathmastery.dto.TutorDTO;
import com.mathmastery.models.TutorBookingRequest;
import com.mathmastery.models.TutorSchedule;
import com.mathmastery.models.User;
import com.mathmastery.repositories.TutorBookingRequestRepository;
import com.mathmastery.repositories.TutorScheduleRepository;
import com.mathmastery.repositories.UserRepository;
import com.mathmastery.services.EmailService;
import com.mathmastery.services.TutorBookingService;
import com.mathmastery.services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class TutorBookingController {

    private final TutorBookingService bookingService;
    private UserService userRepository;
    private TutorScheduleRepository scheduleRepository;
    private final UserRepository userRepo;
    private final TutorScheduleRepository scheduleRepo;
    private final TutorBookingRequestRepository bookingRepo;
    private final EmailService mailService;

    public TutorBookingController(TutorBookingService bookingService, UserService userRepository, TutorScheduleRepository scheduleRepository, UserRepository userRepo, TutorScheduleRepository scheduleRepo, TutorBookingRequestRepository bookingRepo, EmailService mailService) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepo = userRepo;
        this.scheduleRepo = scheduleRepo;
        this.bookingRepo = bookingRepo;
        this.mailService = mailService;
    }

    @PostMapping
    public String createBooking(
            @RequestParam Long tutorId,
            @RequestParam Long scheduleId,
            @RequestParam String message,
            @RequestParam Long studentId // replace this with authenticated user ID later
    ) {

        User student = userRepo.findById(studentId).orElseThrow();
        User tutor = userRepo.findById(tutorId).orElseThrow();
        TutorSchedule schedule = scheduleRepo.findById(scheduleId).orElseThrow();

        TutorBookingRequest request = new TutorBookingRequest();
        request.setStudent(student);
        request.setTutor(tutor);
        request.setSchedule(schedule);
        request.setMessage(message);

        // Send email to tutor
        String tutorEmail = tutor.getEmail();
        System.out.println(tutorEmail);
        String subject = "New Booking Request from " + student.getFullname();
        String body = "<p>You have a new tutoring request from <strong>" + student.getFullname() + "</strong>.</p>" +
                "<p><strong>Time:</strong> " + schedule.getDayOfWeek() + " " + schedule.getStartTime() + "-" + schedule.getEndTime() + "</p>" +
                "<p><strong>Message:</strong> " + message + "</p>" +
                "<p>Please visit your dashboard to approve or reject this request.</p>";

        mailService.sendEmail(tutorEmail, subject, body);
        bookingService.createBooking(studentId, tutorId, scheduleId, message);
        return "Booking request sent!";


    }


}