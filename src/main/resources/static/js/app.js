AOS.init({
    duration: 1000,
    repeat: true,
});


// Header scroll effect
window.addEventListener('scroll', function() {
    const header = document.querySelector('.main-header');
    if (window.scrollY > 100) {
        header.classList.add('scrolled');
    } else {
        header.classList.remove('scrolled');
    }
});

// Smooth scrolling for navigation links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});
// Services data
const services = [
    {
        icon: "fas fa-user-graduate",
        title: "Find a Tutor",
        description: "Connect with qualified tutors at your school who understand your curriculum and can provide personalized support for your math journey.",
        features: [
            "Qualified local tutors",
            "Curriculum-aligned support",
            "Flexible scheduling"
        ]
    },
    {
        icon: "fas fa-chart-line",
        title: "Progress Tracking",
        description: "Monitor your learning journey with detailed analytics, identify strengths and improvement areas through comprehensive performance analysis.",
        features: [
            "Detailed analytics",
            "Performance insights",
            "Goal setting tools"
        ]
    },
    {
        icon: "fas fa-clipboard-check",
        title: "Exam Simulation",
        description: "Practice with realistic exam conditions, master time management, and build confidence through our comprehensive simulation environment.",
        features: [
            "Realistic exam conditions",
            "Time management practice",
            "Instant feedback"
        ]
    },
    {
        icon: "fas fa-book-open",
        title: "Interactive Lessons",
        description: "Engage with dynamic, multimedia lessons that adapt to your learning style and pace, making complex concepts easy to understand.",
        features: [
            "Adaptive learning paths",
            "Multimedia content",
            "Interactive exercises"
        ]
    },
    {
        icon: "fas fa-users",
        title: "Study Groups",
        description: "Join collaborative study sessions with peers, share knowledge, and learn together in a supportive community environment.",
        features: [
            "Peer collaboration",
            "Group discussions",
            "Shared resources"
        ]
    },
    {
        icon: "fas fa-mobile-alt",
        title: "Mobile Learning",
        description: "Access your learning materials anywhere, anytime with our mobile-optimized platform designed for on-the-go studying.",
        features: [
            "Cross-platform access",
            "Offline capabilities",
            "Synchronized progress"
        ]
    },
    {
        icon: "fas fa-trophy",
        title: "Achievement System",
        description: "Stay motivated with our gamified learning experience featuring badges, leaderboards, and rewards for your progress.",
        features: [
            "Achievement badges",
            "Progress rewards",
            "Leaderboards"
        ]
    },
    {
        icon: "fas fa-brain",
        title: "AI-Powered Assistance",
        description: "Get personalized help from our AI tutor that provides instant explanations, step-by-step solutions, and learning recommendations.",
        features: [
            "Instant explanations",
            "Step-by-step solutions",
            "Personalized recommendations"
        ]
    },
    {
        icon: "fas fa-calendar-alt",
        title: "Study Scheduler",
        description: "Organize your study time effectively with smart scheduling tools that help you balance learning with other commitments.",
        features: [
            "Smart scheduling",
            "Reminder notifications",
            "Time management tools"
        ]
    }
];

// Pagination variables
let currentPage = 1;
const itemsPerPage = 3;
const totalPages = Math.ceil(services.length / itemsPerPage);

// Initialize the page
function init() {
    renderServices();
    setupPagination();
    AOS.init({
        duration: 1000,
        once: true
    });
}

// Render services for current page
function renderServices() {
    const container = document.getElementById('servicesContainer');
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const currentServices = services.slice(startIndex, endIndex);

    container.innerHTML = '';

    currentServices.forEach((service, index) => {
        const serviceCard = createServiceCard(service, startIndex + index);
        container.appendChild(serviceCard);
    });

    // Add fade-in animation
    setTimeout(() => {
        const cards = container.querySelectorAll('.service-card');
        cards.forEach((card, index) => {
            setTimeout(() => {
                card.classList.add('visible');
            }, index * 150);
        });
    }, 100);
}

// Create service card element
function createServiceCard(service, index) {
    const col = document.createElement('div');
    col.className = 'col-lg-4 col-md-6';

    col.innerHTML = `
                <div class="service-card fade-in" 
                data-aos="fade-left"
                data-aos-delay="${index * 100}">
                    <div class="service-icon">
                        <i class="${service.icon}"></i>
                    </div>
                    <h3 class="service-title">${service.title}</h3>
                    <p class="service-description">${service.description}</p>
                    <ul class="service-features">
                        ${service.features.map(feature => `
                            <li><i class="fas fa-check"></i> ${feature}</li>
                        `).join('')}
                    </ul>
                </div>
            `;

    return col;
}

// Setup pagination
function setupPagination() {
    const paginationContainer = document.getElementById('paginationContainer');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const paginationInfo = document.getElementById('paginationInfo');
    const paginationDots = document.getElementById('paginationDots');

    // Show pagination only if there are more than 3 services
    if (services.length > itemsPerPage) {
        paginationContainer.style.display = 'flex';

        // Create pagination dots
        paginationDots.innerHTML = '';
        for (let i = 1; i <= totalPages; i++) {
            const dot = document.createElement('div');
            dot.className = `pagination-dot ${i === currentPage ? 'active' : ''}`;
            dot.addEventListener('click', () => goToPage(i));
            paginationDots.appendChild(dot);
        }

        // Setup navigation buttons
        prevBtn.addEventListener('click', () => goToPage(currentPage - 1));
        nextBtn.addEventListener('click', () => goToPage(currentPage + 1));

        updatePaginationState();
    }
}

// Go to specific page
function goToPage(page) {
    if (page < 1 || page > totalPages) return;

    currentPage = page;
    renderServices();
    updatePaginationState();

    // Smooth scroll to services section
    if (window.innerWidth <= 768) { // Adjust as needed (768px is common mobile threshold)
        document.getElementById('services').scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    }

}

// Update pagination state
function updatePaginationState() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const paginationInfo = document.getElementById('paginationInfo');
    const paginationDots = document.querySelectorAll('.pagination-dot');

    // Update buttons
    prevBtn.disabled = currentPage === 1;
    nextBtn.disabled = currentPage === totalPages;

    // Update info
    const startItem = (currentPage - 1) * itemsPerPage + 1;
    const endItem = Math.min(currentPage * itemsPerPage, services.length);
    paginationInfo.textContent = `${startItem}-${endItem} of ${services.length}`;

    // Update dots
    paginationDots.forEach((dot, index) => {
        dot.classList.toggle('active', index + 1 === currentPage);
    });
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', init);

// Add smooth scrolling for internal links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});



const questions = [
    {
        question: "What is the derivative of:",
        equation: " 3x<sup>2</sup> + 2x + 1",
        answer: "6x+2"
    },
    {
        question: "What is the integral of",
        equation: "2x",
        answer: "x<sup>2</sup> + C"
    },
    {
        question: "Solve for x: ",
        equation: "2x + 5 = 13",
        answer: "4"
    },
    // Add more questions as needed.
];


let currentQuestion = 0;
let correctAnswer = null;
let totalAttempts = 0;
let isSubmitting = false;

function getRandomQuestion() {
    const randomIndex = Math.floor(Math.random() * questions.length);
    return questions[randomIndex];
}

// Initialize with first question
function loadQuestion() {
    const question = getRandomQuestion();
    document.getElementById('question').textContent = question.question;
    document.getElementById("sampleQuestion").innerHTML = question.equation;
    document.getElementById('sampleAnswer').value = '';
    document.getElementById('sampleFeedback').innerHTML = '';
    correctAnswer = question.answer; // Store the correct answer for the current question

}

function checkAnswer() {
    if (isSubmitting) return;

    const userAnswer = document.getElementById('sampleAnswer').value.trim();
    const feedbackElement = document.getElementById('sampleFeedback');
    const submitButton = document.getElementById('submitSample');

    if (!userAnswer) {
        feedbackElement.innerHTML = '<div class="feedback-error"><i class="fas fa-exclamation-triangle me-2"></i>Please enter an answer!</div>';
        return;
    }

    isSubmitting = true;
    submitButton.innerHTML = '<div class="loading-spinner"></div>Checking...';
    submitButton.disabled = true;

    setTimeout(() => {
        totalAttempts++;

        if (userAnswer.toLowerCase() === correctAnswer.toLowerCase()) {

            feedbackElement.innerHTML = `
                        <div class="feedback-success">
                            <i class="fas fa-check-circle me-2"></i>
                            Correct! Well done! 🎉
                        </div>
                    `;

            setTimeout(() => {
                currentQuestion = (currentQuestion + 1) % questions.length;
                loadQuestion();
            }, 2000);
        } else {
            feedbackElement.innerHTML = `
                        <div class="feedback-error">
                            <i class="fas fa-times-circle me-2"></i>
                            Not quite right. The correct answer is: <strong>${correctAnswer}</strong>
                        </div>
                    `;

            setTimeout(() => {
                currentQuestion = (currentQuestion + 1) % questions.length;
                loadQuestion();
            }, 3000);
        }


        submitButton.innerHTML = '<i class="fas fa-paper-plane me-2"></i>Submit Answer';
        submitButton.disabled = false;
        isSubmitting = false;
    }, 1000);
}

// Event listeners
document.getElementById('submitSample').addEventListener('click', checkAnswer);

document.getElementById('sampleAnswer').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        checkAnswer();
    }
});

// Add input animation
document.getElementById('sampleAnswer').addEventListener('focus', function () {
    this.parentElement.style.transform = 'scale(1.02)';
});

document.getElementById('sampleAnswer').addEventListener('blur', function () {
    this.parentElement.style.transform = 'scale(1)';
});

// Initialize
loadQuestion();

// Add some interactive particles
function createParticle() {
    const particle = document.createElement('div');
    particle.style.position = 'absolute';
    particle.style.width = '4px';
    particle.style.height = '4px';
    particle.style.backgroundColor = 'rgba(255, 255, 255, 0.6)';
    particle.style.borderRadius = '50%';
    particle.style.pointerEvents = 'none';
    particle.style.left = Math.random() * 100 + '%';
    particle.style.top = '100%';
    particle.style.animation = 'floatUp 8s linear infinite';

    document.querySelector('.floating-elements').appendChild(particle);

    setTimeout(() => {
        particle.remove();
    }, 8000);
}

// Add floating particles periodically
setInterval(createParticle, 3000);

// Add CSS for floating particles
const style = document.createElement('style');
style.textContent = `
            @keyframes floatUp {
                0% {
                    transform: translateY(0) rotate(0deg);
                    opacity: 0;
                }
                10% {
                    opacity: 1;
                }
                90% {
                    opacity: 1;
                }
                100% {
                    transform: translateY(-100vh) rotate(360deg);
                    opacity: 0;
                }
            }
        `;
document.head.appendChild(style);


// Add smooth scrolling for internal links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href'))?.scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Add dynamic counter animation
function animateCounters() {
    const counters = document.querySelectorAll('.stat-number');

    counters.forEach(counter => {
        const target = counter.textContent;
        const numericValue = parseInt(target.replace(/\D/g, ''));
        const suffix = target.replace(/\d/g, '');

        let current = 0;
        const increment = numericValue / 100;
        const timer = setInterval(() => {
            current += increment;
            if (current >= numericValue) {
                current = numericValue;
                clearInterval(timer);
            }
            counter.textContent = Math.floor(current) + suffix;
        }, 20);
    });
}

// Trigger counter animation when section comes into view
const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            animateCounters();
            observer.disconnect();
        }
    });
});


// Add more dynamic interactions
document.addEventListener('DOMContentLoaded', function () {

    // Add tilt effect to feature cards
    const featureCards = document.querySelectorAll('.feature-card');

    featureCards.forEach(card => {
        card.addEventListener('mousemove', function (e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const centerX = rect.width / 2;
            const centerY = rect.height / 2;

            const rotateX = (y - centerY) / 10;
            const rotateY = (centerX - x) / 10;

            this.style.transform = `translateY(-15px) scale(1.02) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
        });

        card.addEventListener('mouseleave', function () {
            this.style.transform = 'translateY(-15px) scale(1.02)';
        });
    });

    // Add particle effect on scroll
    let particles = [];
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    canvas.style.position = 'fixed';
    canvas.style.top = '0';
    canvas.style.left = '0';
    canvas.style.pointerEvents = 'none';
    canvas.style.zIndex = '1';
    canvas.style.opacity = '0.6';
    document.body.appendChild(canvas);

    function resizeCanvas() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }

    function createParticle(x, y) {
        return {
            x: x,
            y: y,
            vx: (Math.random() - 0.5) * 2,
            vy: (Math.random() - 0.5) * 2,
            life: 1,
            decay: Math.random() * 0.02 + 0.01
        };
    }

    function updateParticles() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        for (let i = particles.length - 1; i >= 0; i--) {
            const particle = particles[i];

            particle.x += particle.vx;
            particle.y += particle.vy;
            particle.life -= particle.decay;

            if (particle.life <= 0) {
                particles.splice(i, 1);
                continue;
            }

            ctx.save();
            ctx.globalAlpha = particle.life;
            ctx.fillStyle = '#667eea';
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, 2, 0, Math.PI * 2);
            ctx.fill();
            ctx.restore();
        }

        requestAnimationFrame(updateParticles);
    }

    window.addEventListener('resize', resizeCanvas);
    resizeCanvas();
    updateParticles();

    // Create particles on mouse move over feature cards
    featureCards.forEach(card => {
        card.addEventListener('mousemove', function (e) {
            if (Math.random() < 0.1) {
                particles.push(createParticle(e.clientX, e.clientY));
            }
        });
    });
});



