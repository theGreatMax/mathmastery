package com.mathmastery.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // Nullable for non-students (e.g., tutors, admins)
    private Integer grade;

    private String school;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.INACTIVE;

    @Column(nullable = false)
    private Boolean emailVerified = true;

    private String pendingEmail;
    private String emailVerificationToken;
    private String resetToken;
    private LocalDateTime tokenExpiry;

    @Column(name = "last_email_resend_time")
    private LocalDateTime lastEmailResendTime;

    // Optional: Distinguish between different account types
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.STUDENT;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    public User() {}

    public User(Long id, String fullname, String password, String email, Integer grade, String school,
                UserStatus status, boolean emailVerified, Set<Role> roles, UserType userType) {
        this.id = id;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.grade = grade;
        this.school = school;
        this.status = status;
        this.emailVerified = emailVerified;
        this.roles = roles;
        this.userType = userType;
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, BANNED
    }

    public enum UserType {
        STUDENT,
        TUTOR,
        ADMIN
    }
}
