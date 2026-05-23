const navToggle = document.getElementById("navToggle");
    const nav = document.getElementById("nav");
    navToggle?.addEventListener("click", () => nav.classList.toggle("open"));

    const toTop = document.getElementById("toTop");
    window.addEventListener("scroll", () => {
      if (window.scrollY > 400) toTop.classList.add("show");
      else toTop.classList.remove("show");
    });
    toTop?.addEventListener("click", () => window.scrollTo({ top: 0, behavior: "smooth" }));