document.addEventListener("DOMContentLoaded", () => {


    /* ==============================
       Greeting
    ============================== */

    const heading = document.querySelector("#welcome");

    if (heading) {

        const hour = new Date().getHours();

        let greeting;

        if (hour < 12) {

            greeting = "Good Morning";

        } 
        else if (hour < 18) {

            greeting = "Good Afternoon";

        } 
        else {

            greeting = "Good Evening";

        }

        heading.textContent =
            greeting + ", User ❤️";

    }


    /* ==============================
       Emergency Button
    ============================== */

    const emergency =
        document.querySelector("#emergencyBtn");

    if (emergency) {

        emergency.addEventListener("click", function () {

            alert(
                "🚨 EMERGENCY SUPPORT\n\n" +
                "Your emergency request has been initiated.\n\n" +
                "Our team will contact you soon."
            );

        });

    }


    /* ==============================
       Load Logged-in User
    ============================== */

    fetch("DashboardServlet")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to load dashboard information."
                );

            }

            return response.json();

        })

        .then(user => {

            console.log(
                "Logged-in user:",
                user
            );


            /* Greeting */

            const welcome =
                document.querySelector("#welcome");

            if (welcome && user.fullName) {

                const hour =
                    new Date().getHours();

                let greeting;

                if (hour < 12) {

                    greeting = "Good Morning";

                } 
                else if (hour < 18) {

                    greeting = "Good Afternoon";

                } 
                else {

                    greeting = "Good Evening";

                }

                welcome.textContent =
                    greeting +
                    ", " +
                    user.fullName +
                    " ❤️";

            }


            /* Blood Group */

            const bloodGroup =
                document.querySelector("#userBloodGroup");

            if (bloodGroup) {

                bloodGroup.textContent =
                    user.bloodGroup || "--";

            }

        })

        .catch(error => {

            console.error(
                "Could not load user information:",
                error
            );

        });


    /* ==============================
       Card Animation
    ============================== */

    const cards =
        document.querySelectorAll(
            ".card, .panel, .quote"
        );


    cards.forEach((card, index) => {

        card.style.opacity = "0";

        card.style.transform =
            "translateY(30px)";


        setTimeout(() => {

            card.style.transition =
                "all 0.6s ease";

            card.style.opacity = "1";

            card.style.transform =
                "translateY(0)";

        }, index * 150);

    });


});