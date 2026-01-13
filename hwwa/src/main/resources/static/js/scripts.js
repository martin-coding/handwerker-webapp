function openDeleteModal(employeeId) {
    const form = document.getElementById('deleteEmployeeForm');
    // Replace __ID__ in the form action with the real employee ID
    form.setAttribute('action', '/employee/delete/' + employeeId);

    // Show the modal
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteEmployeeModal'));
    deleteModal.show();
}



document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".input-wrapper").forEach(wrapper => {
        const toggleIcon = wrapper.querySelector(".icon-right");
        const passwordInput = wrapper.querySelector("input[type='password'], input[type='text']");

        if (!toggleIcon || !passwordInput) return;

        toggleIcon.style.cursor = "pointer";

        toggleIcon.addEventListener("click", () => {
            const isHidden = passwordInput.type === "password";

            passwordInput.type = isHidden ? "text" : "password";
            toggleIcon.textContent = isHidden ? "visibility_off" : "visibility";
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".input-wrapper").forEach(wrapper => {
        const toggleIcon = wrapper.querySelector(".icon-employee");
        const passwordInput = wrapper.querySelector("input[type='password'], input[type='text']");

        if (!toggleIcon || !passwordInput) return;

        toggleIcon.style.cursor = "pointer";

        toggleIcon.addEventListener("click", () => {
            const isHidden = passwordInput.type === "password";

            passwordInput.type = isHidden ? "text" : "password";
            toggleIcon.textContent = isHidden ? "visibility_off" : "visibility";
        });
    });
});


document.addEventListener("DOMContentLoaded", () => {

    // --- Menu toggle for small screens ---
    const menuBtn = document.getElementById('menuBtn');
    const mobileNav = document.getElementById('mobileNav');

    if (menuBtn && mobileNav) {
        menuBtn.addEventListener('click', () => {
            if (mobileNav.classList.contains('d-none')) {
                mobileNav.classList.remove('d-none');
                mobileNav.classList.add('d-flex', 'flex-column');
            } else {
                mobileNav.classList.add('d-none');
                mobileNav.classList.remove('d-flex', 'flex-column');
            }
        });
    }
});

