# 📚 Math Mastery

**Math Mastery** is a full-stack educational web application that helps Grade 10–12 students practice mathematics through interactive quizzes, AI-generated explanations, and a tutor support system.

---

## 🚀 Live Demo

🌐 Coming Soon – hosted on [Render](https://render.com)

---

## 🖼 Screenshots

| Dashboard View | Practice Mode | Tutor Booking |
|----------------|----------------|----------------|
| ![Dashboard](screenshots/dashboard.png) | ![Practice](screenshots/practice.png) | ![Booking](screenshots/booking.png) |

---

## ✨ Features

- ✅ Grade-based content (Grade 10, 11, 12)
- 📚 Practice by topic & subtopic
- ⏱ Timed quizzes and exam simulations
- 📊 Track time spent and progress
- 🤖 AI-powered solution explanations using OpenAI
- 👨‍🏫 Tutor booking and session approval
- 📬 Email notifications (Spring Mail)
- 🧾 Dashboards for students & tutors

---

## 🛠 Tech Stack

**Backend:**
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL

**Frontend:**
- Thymeleaf (MVC)
- Bootstrap 5
- JavaScript, HTML5, CSS3

**Other Tools:**
- OpenAI API
- GitHub, Git
- Render (for deployment)
- Spring Mail (email notifications)

---

## 📂 Project Structure

```bash
math-mastery/
├── src/
│   └── main/
│       ├── java/com/mathmastery/
│       └── resources/
│           ├── templates/       # Thymeleaf templates
│           ├── static/          # JS, CSS, images
│           └── application.yml
├── pom.xml
└── README.md
