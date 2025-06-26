package com.mathmastery.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathmastery.dto.ScheduleDTO;
import com.mathmastery.dto.TutorDTO;
import com.mathmastery.models.*;
import com.mathmastery.repositories.*;

import com.mathmastery.services.QuestionService;
import com.mathmastery.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AppController {


    private final UserService userService;

    //    private final UserProgressService userProgressService;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    private final SubtopicService subtopicService;
    private final SubtopicRepository subtopicRepository;
    private final TutorScheduleRepository tutorScheduleRepository;
    private final TutorBookingRequestRepository tutorBookingRequestRepository;

    public AppController(UserService userService, TopicRepository topicRepository, UserRepository userRepository, SubtopicService subtopicService, SubtopicRepository subtopicRepository, TutorScheduleRepository tutorSchedule, TutorBookingRequestRepository tutorBookingRequestRepository) {
        this.userService = userService;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.subtopicService = subtopicService;
        this.subtopicRepository = subtopicRepository;
        this.tutorScheduleRepository = tutorSchedule;
        this.tutorBookingRequestRepository = tutorBookingRequestRepository;
    }

    @GetMapping("/")
    public String showLandingPage() {
        return "index";
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new PendingUser());
        return "signup";
    }

    // User Sign-Up Endpoint
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") PendingUser pendingUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "signup";
        }

        try {
            userService.registerUser(pendingUser);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }

        return "redirect:/sign-up-success";
    }

    @GetMapping("/sign-up-success")
    public String signUpSuccess() {
        return "sign-up-success";
    }

    @RequestMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/contact-us")
    public String contactPage() {
        return "contact-us";

    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }

    @PostMapping("/api/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
    }


    @GetMapping("/practice-mode")
    public String showPracticeMode(Model model, Principal principal) {
        String email = principal.getName();

        // Use your repository to find the user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Initialize progress if needed
//        userProgressService.initializeUserProgressIfMissing(user);

        List<Topic> gradeTopics = topicRepository.findByGrade(user.getGrade());
        List<Topic> topicViews = new ArrayList<>();

        for (Topic topic : gradeTopics) {
            Topic view = new Topic();
            view.setTitle(topic.getTitle());
            view.setDescription(topic.getDescription());
            view.setIconClass(topic.getIconClass());
            view.setColorClass(topic.getColorClass());
            view.setTopicSlug(topic.getTopicSlug());
            view.setSubtopics(topic.getSubtopics());


            topicViews.add(view);
        }

        model.addAttribute("topics", topicViews);
        model.addAttribute("grade", user.getGrade());
        return "practice-mode";
    }

    @GetMapping("/practice-mode-next")
    public String viewSubtopics(@RequestParam("topic") String topicSlug, Model model) {
        Topic topic = topicRepository.findByTopicSlug(topicSlug);
        long topicID = topic.getId();
        List<Subtopic> subtopics = subtopicRepository.findByTopic_id(topicID);

        if (subtopics.isEmpty()) {
            model.addAttribute("errorMessage", "No subtopics available for this topic.");
        }

        model.addAttribute("topic", topic);
        model.addAttribute("subtopics", subtopics);
        return "subtopics";
    }

    @GetMapping("/practice-page/{topicsSlug}/{slug}")
    public String showPracticeQuestions(@PathVariable String topicsSlug,
                                        @PathVariable String slug,
                                        Model model) {
        Subtopic subtopic = subtopicService.findBySlugAndTopicSlug(topicsSlug, slug);
        if (subtopic == null) {
            model.addAttribute("errorMessage", "Subtopic not found.");
            return "practice-page";
        }

        model.addAttribute("subtopic", subtopic);
        return "practice-page";
    }

    @GetMapping("/tutor-booking")
    public String getTutors(Model model, Principal principal, ObjectMapper objectMapper) throws Exception {

        User student = userRepository.findByEmail(principal.getName()).orElseThrow();
        Long studentId = student.getId();

        List<TutorDTO> tutors = userService.getAllTutors(studentId).stream().map(tutor -> {
            TutorDTO dto = new TutorDTO();
            dto.setId(tutor.getId());
            dto.setName(tutor.getName());
            dto.setSubject("Mathematics"); // or dynamic
            dto.setBio("Experienced tutor in Math and Science.");
            dto.setImageUrl("/img/default-avatar.png");

            // Convert tutor's schedule (assume you have a getSchedule() returning List<TutorSchedule>)
            List<ScheduleDTO> schedule = tutor.getSchedule().stream()
                    .map(s -> new ScheduleDTO(s.getId(), s.getDayOfWeek(), s.getStartTime(), s.getEndTime()))
                    .collect(Collectors.toList());

            // Check for pending booking
            boolean alreadyBooked = tutorBookingRequestRepository
                    .existsByTutorIdAndStudentIdAndStatus(tutor.getId(), studentId, TutorBookingRequest.Status.PENDING);
            dto.setAlreadyBooked(alreadyBooked);

            // Set booking status (latest)
            tutorBookingRequestRepository
                    .findFirstByTutorIdAndStudentIdOrderByCreatedAtDesc(tutor.getId(), studentId)
                    .ifPresent(booking -> dto.setBookingStatus(booking.getStatus().name()));


            try {
                dto.setScheduleJson(objectMapper.writeValueAsString(schedule)); // ✅ JSON for HTML
            } catch (Exception e) {
                dto.setScheduleJson("[]");
            }


            return dto;
        }).collect(Collectors.toList());


        model.addAttribute("tutors", tutors);
        model.addAttribute("studentID", student.getId());

        return "tutor-booking";
    }



    @GetMapping("/booking-success")
    public String bookingSuccess(@RequestParam Long tutorId,
                                 @RequestParam Long scheduleId,
                                 Model model) {
        User tutor = userRepository.findById(tutorId).orElseThrow();

        System.out.println("Booking success for tutor: " + tutor.getFullname() + " with ID: " + tutorId);

        TutorSchedule schedule = tutorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));

        System.out.println("Schedule details: " + schedule.getDayOfWeek() + " from " + schedule.getStartTime() + " to " + schedule.getEndTime());

        model.addAttribute("tutor", tutor);
        model.addAttribute("schedule", schedule);

        return "booking-success";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam Long tutorId,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        User student = userRepository.findByEmail(principal.getName()).orElseThrow();

        // Find the pending booking
        TutorBookingRequest booking = tutorBookingRequestRepository
                .findByTutorIdAndStudentIdAndStatus(tutorId, student.getId(), TutorBookingRequest.Status.PENDING)
                .orElseThrow(() -> new RuntimeException("No pending booking found."));

        tutorBookingRequestRepository.delete(booking);

        redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully.");
        return "redirect:/tutor-booking";
    }

}