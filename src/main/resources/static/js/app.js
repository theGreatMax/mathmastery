AOS.init({
  duration: 1000,
  repeat: true,
});


document.addEventListener("DOMContentLoaded", () => {
  const heroSignupLink = document.getElementById("hero-signup");
  const headerSignup = document.getElementById("header-signup");
  const header = document.getElementById("header");
  const logIn = document.getElementsByClassName("header-right .btn");


  const headerHeight = header.getBoundingClientRect().height;

  const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.boundingClientRect.top < headerHeight && !entry.isIntersecting) {
            headerSignup.style.display = "block";
          } else {
            headerSignup.style.display = "none";
          }
        });
      },
      {
        root: null,
        threshold: 0,
      }
  );

  observer.observe(heroSignupLink);
});

// document.getElementById("footer-about-link").addEventListener("click", function (event) {
//   event.preventDefault();
//   document.getElementById("about-us").scrollIntoView({behavior: "smooth"});
// });
//
//
// document.getElementById("footer-services").addEventListener("click", function (event) {
//   event.preventDefault();
//   document.getElementById("services").scrollIntoView({behavior: "smooth"});
// });


// Array of question objects.
const questions = [
  {
    question: "What is the derivative of:" ,
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

// Returns a random question from the array.
function getRandomQuestion() {
  const randomIndex = Math.floor(Math.random() * questions.length);
  return questions[randomIndex];
}

document.addEventListener("DOMContentLoaded", function() {

  const questionElement = document.getElementById('question');
  const equation = document.getElementById('sampleQuestion')
  const answerInput = document.getElementById('sampleAnswer');
  const submitButton = document.getElementById('submitSample');
  const feedbackElement = document.getElementById('sampleFeedback');

  // Holds the current question.
  let currentQuestion = null;

  // Function to load a new random question.
  function loadNewQuestion() {
    currentQuestion = getRandomQuestion();
    questionElement.innerHTML = currentQuestion.question;
    equation.innerHTML = currentQuestion.equation;
    answerInput.value = "";
    feedbackElement.innerHTML = "";
  }

  // Load the initial question.
  loadNewQuestion();

  // Handle answer submission.
  submitButton.addEventListener('click', function() {
    const userAnswer = answerInput.value.trim();

    // Check answer in a case-insensitive manner.
    if (userAnswer.toLowerCase() === currentQuestion.answer.toLowerCase()) {
      feedbackElement.innerHTML = "<span class='text-success'>Correct!</span>";
    } else {
      feedbackElement.innerHTML = `<span class='text-danger'>Incorrect. The correct answer is ${currentQuestion.answer}.</span>`;
    }

    // After 2 seconds, load another question.
    setTimeout(loadNewQuestion, 5000);
  });
});
