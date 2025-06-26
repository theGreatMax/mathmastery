package com.mathmastery.services;

import com.mathmastery.models.*;
import com.mathmastery.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class TutorBookingService {

    private final TutorBookingRequestRepository bookingRepo;
    private final UserRepository userRepo;
    private final TutorScheduleRepository scheduleRepo;

    public TutorBookingService(TutorBookingRequestRepository bookingRepo, UserRepository userRepo, TutorScheduleRepository scheduleRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.scheduleRepo = scheduleRepo;
    }

    public void createBooking(Long studentId, Long tutorId, Long scheduleId, String message) {
        User student = userRepo.findById(studentId).orElseThrow();
        User tutor = userRepo.findById(tutorId).orElseThrow();
        TutorSchedule schedule = scheduleRepo.findById(scheduleId).orElseThrow();

        TutorBookingRequest request = new TutorBookingRequest();
        request.setStudent(student);
        request.setTutor(tutor);
        request.setSchedule(schedule);
        request.setMessage(message);

        bookingRepo.save(request);
    }
}
