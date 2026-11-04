document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector("form");

    if (!form) {
        console.error("Registration form not found.");
        return;
    }

    form.addEventListener("submit", (e) => {

        const password = document.querySelector("#password").value;
        const confirmPassword =
            document.querySelector("#confirmPassword").value;

        if (password !== confirmPassword) {
            e.preventDefault();
            alert("Passwords do not match!");
            return;
        }

        const dob = document.querySelector("#dob").value;

        if (dob) {

            const birthDate = new Date(dob);
            const today = new Date();

            let age =
                today.getFullYear() -
                birthDate.getFullYear();

            const monthDifference =
                today.getMonth() -
                birthDate.getMonth();

            if (
                monthDifference < 0 ||
                (monthDifference === 0 &&
                 today.getDate() < birthDate.getDate())
            ) {
                age--;
            }

            if (age < 18) {
                e.preventDefault();

                alert(
                    "You must be at least 18 years old to register as a donor."
                );

                return;
            }
        }

        // Do not prevent successful submission.
        // The form will be sent to RegisterServlet.
    });
});