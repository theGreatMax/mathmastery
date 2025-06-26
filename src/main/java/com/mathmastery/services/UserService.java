package com.mathmastery.services;

import com.mathmastery.dto.ScheduleDTO;
import com.mathmastery.dto.TutorDTO;
import com.mathmastery.models.PendingUser;
import com.mathmastery.models.TutorBookingRequest;
import com.mathmastery.models.TutorSchedule;
import com.mathmastery.models.User;
import com.mathmastery.repositories.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PendingUserRepository pendingUserRepository;
    private final TutorScheduleRepository tutorScheduleRepository;
    private final TutorBookingRequestRepository tutorBookingRequestRepository;


    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public UserService(PendingUserRepository pendingUserRepository, UserRepository userRepository, EmailService emailService, TutorScheduleRepository tutorScheduleRepository, TutorBookingRequestRepository tutorBookingRequestRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tutorScheduleRepository = tutorScheduleRepository;
        this.tutorBookingRequestRepository = tutorBookingRequestRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.pendingUserRepository = pendingUserRepository;
    }


    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public int getGradeByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(User::getGrade)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void registerUser(PendingUser pendingUser) {

        if (userRepository.existsByEmail(pendingUser.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }


        pendingUser.setPassword(passwordEncoder.encode(pendingUser.getPassword()));
        pendingUser.generateVerificationToken();
        pendingUserRepository.save(pendingUser);

        emailService.sendVerificationEmail(pendingUser.getEmail(), pendingUser.getVerificationToken());

    }

    @Transactional
    public boolean verifyEmail(String token) {
        PendingUser pendingUser = pendingUserRepository.findByVerificationToken(token);

        if (pendingUser == null) {
            return false;
        }

        if (pendingUser.getVerificationTokenExpiry() != null && pendingUser.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Move user from PendingUser to User
        User user = new User();
        user.setFullname(pendingUser.getFullname());
        user.setEmail(pendingUser.getEmail());
        user.setSchool(pendingUser.getSchool());
        user.setGrade(pendingUser.getGrade());
        user.setPassword(pendingUser.getPassword());
        user.setEmailVerified(true);
        user.setStatus(User.UserStatus.ACTIVE);


        userRepository.save(user);  // Save to the main users table
        pendingUserRepository.delete(pendingUser);
        emailService.sendUserIsVerifiedEmail(user.getEmail()); // notify user that their email has been verified

        // Remove from pending_users

        return true;
    }


    // Update profile (email, school, grade)
    public void updateProfile(Long userId, String school, int grade) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setSchool(school);
        user.setGrade(grade);
        userRepository.save(user);
    }

    // Change password
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    public void requestEmailChange(Long userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmail().equals(newEmail)) {
            String token = UUID.randomUUID().toString();
            user.setPendingEmail(newEmail);
            user.setEmailVerificationToken(token);
            user.setEmailVerified(false); // temporarily unverified
            userRepository.save(user);

            // send verification email
            sendVerificationEmail(user, token);
        }
    }

    private void sendVerificationEmail(User user, String token) {
        String verificationLink = "http://localhost:8080/verify-email?token=" + token;
        String emailBody = "Hi " + user.getFullname() + ",\n\n"
                + "Please verify your new email address by clicking the link below:\n"
                + verificationLink + "\n\n"
                + "If you didn't request this, please ignore this email.";

        // You need to implement an EmailService to actually send the email
        emailService.sendEmail(user.getPendingEmail(), "Verify your new email address", emailBody);
    }


    public void resendVerificationEmail(User user) {
        LocalDateTime now = LocalDateTime.now();

        if (user.getLastEmailResendTime() != null) {
            Duration duration = Duration.between(user.getLastEmailResendTime(), now);
            if (duration.getSeconds() < 60) {
                throw new IllegalStateException("Please wait before resending the email.");
            }
        }

        // Update resend time
        user.setLastEmailResendTime(now);
        userRepository.save(user);

        // Reuse existing token and pending email
        sendVerificationEmail(user, user.getEmailVerificationToken());
    }


    public void cancelPendingEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPendingEmail(null);
        user.setEmailVerificationToken(null);
        user.setTokenExpiry(null); // Optional, if you're using expiry field
        userRepository.save(user);
    }


    public List<TutorDTO> getAllTutors(Long studentId) {
        List<User> tutors = userRepository.findByUserType(User.UserType.TUTOR);
        List<TutorDTO> result = new ArrayList<>();

        for (User tutor : tutors) {
            List<TutorSchedule> schedules = tutorScheduleRepository.findByTutor(tutor);
            List<ScheduleDTO> scheduleDTOs = schedules.stream().map(s -> {
                ScheduleDTO dto = new ScheduleDTO();
                dto.setId(s.getId());
                dto.setDayOfWeek(s.getDayOfWeek());
                dto.setStartTime(s.getStartTime());
                dto.setEndTime(s.getEndTime());
                return dto;
            }).toList();

            TutorDTO dto = new TutorDTO();
            dto.setId(tutor.getId());
            dto.setName(tutor.getFullname());
            dto.setSchool(tutor.getSchool());
            dto.setSubject("Mathematics"); // or dynamic
            dto.setBio("Experienced tutor in Math and Science.");
            dto.setImageUrl("/img/default-avatar.png");
            dto.setSchedule(scheduleDTOs);
            result.add(dto);
        }

        return result;
    }






}



