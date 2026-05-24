const navToggle = document.getElementById("navToggle");
const nav = document.getElementById("nav");

if (navToggle && nav) {
  navToggle.addEventListener("click", () => {
    nav.classList.toggle("open");
  });
}

const toTop = document.getElementById("toTop");

window.addEventListener("scroll", () => {
  if (!toTop) return;
  if (window.scrollY > 400) toTop.classList.add("show");
  else toTop.classList.remove("show");
});

if (toTop) {
  toTop.addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
}

const slides = document.querySelectorAll(".hero-slide");
const prevBtn = document.getElementById("prevHero");
const nextBtn = document.getElementById("nextHero");

let currentSlide = 0;
let timerId = null;

function showSlide(i) {
  if (!slides.length) return;
  slides.forEach((s) => s.classList.remove("active"));
  slides[i].classList.add("active");
}

function nextSlide() {
  if (!slides.length) return;
  currentSlide = (currentSlide + 1) % slides.length;
  showSlide(currentSlide);
}

function prevSlide() {
  if (!slides.length) return;
  currentSlide = (currentSlide - 1 + slides.length) % slides.length;
  showSlide(currentSlide);
}

function startAuto() {
  if (!slides.length) return;
  if (timerId) clearInterval(timerId);
  timerId = setInterval(nextSlide, 5000);
}

if (slides.length) {
  showSlide(currentSlide);
  startAuto();
}

if (nextBtn) {
  nextBtn.addEventListener("click", () => {
    nextSlide();
    startAuto();
  });
}

if (prevBtn) {
  prevBtn.addEventListener("click", () => {
    prevSlide();
    startAuto();
  });
}

document.addEventListener("visibilitychange", () => {
  if (!slides.length) return;
  if (document.hidden) {
    if (timerId) clearInterval(timerId);
    timerId = null;
  } else {
    startAuto();
  }
});
ss