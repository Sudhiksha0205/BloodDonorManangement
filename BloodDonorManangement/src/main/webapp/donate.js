document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("donationForm");
    const dateInput = document.getElementById("donationDate");

    if (!form) {
        console.error("Donation form not found.");
        return;
    }


    /* =========================================
       PREVENT PREVIOUS DATES
       ========================================= */

    if (dateInput) {

        const today = new Date();

        const year = today.getFullYear();

        const month = String(
            today.getMonth() + 1
        ).padStart(2, "0");

        const day = String(
            today.getDate()
        ).padStart(2, "0");

        dateInput.min =
            year + "-" + month + "-" + day;
    }


    /* =========================================
       FORM SUBMISSION
       ========================================= */

    form.addEventListener("submit", function (event) {

        const hospitalName =
            document.getElementById("hospitalName").value.trim();

        const donationDate =
            document.getElementById("donationDate").value;

        const hospitalAddress =
            document.getElementById("hospitalAddress").value.trim();

        const donationTime =
            document.getElementById("donationTime").value;

        const bloodGroup =
            document.getElementById("bloodGroup").value;

        const donationType =
            document.getElementById("donationType").value;


        /* VALIDATION */

        if (
            hospitalName === "" ||
            donationDate === "" ||
            hospitalAddress === "" ||
            donationTime === "" ||
            bloodGroup === "" ||
            donationType === ""
        ) {

            event.preventDefault();

            alert(
                "Please fill in all donation details."
            );

            return;
        }


        /*
         * IMPORTANT:
         *
         * We DO NOT use event.preventDefault()
         * when all fields are valid.
         *
         * Therefore the browser submits:
         *
         * donate.html
         *       ↓
         * DonationServlet
         *       ↓
         * DonationDAO
         *       ↓
         * MySQL
         */

        console.log(
            "Donation form submitted."
        );

    });

});