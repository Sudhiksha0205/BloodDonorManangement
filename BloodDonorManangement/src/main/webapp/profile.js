document.addEventListener("DOMContentLoaded", () => {

    const inputs = document.querySelectorAll("input, select");

    const editBtn = document.getElementById("editBtn");
    const saveBtn = document.getElementById("saveBtn");

    // --------------------------------------------------
    // Disable fields initially
    // --------------------------------------------------

    function disableFields() {

        inputs.forEach(input => {

            // Buttons are not part of the input collection,
            // so only input/select elements are affected.

            if (input.tagName === "SELECT") {
                input.disabled = true;
            } else {
                input.readOnly = true;
            }
        });
    }


    // --------------------------------------------------
    // Enable fields when Edit Profile is clicked
    // --------------------------------------------------

    function enableFields() {

        inputs.forEach(input => {

            if (input.tagName === "SELECT") {
                input.disabled = false;
            } else {
                input.readOnly = false;
            }
        });
    }


    // --------------------------------------------------
    // Load profile from backend
    // --------------------------------------------------

    function loadProfile() {

        fetch("ProfileServlet")
            .then(response => {

                if (response.redirected) {
                    window.location.href = response.url;
                    return null;
                }

                if (!response.ok) {
                    throw new Error("Failed to load profile.");
                }

                return response.json();
            })
            .then(profile => {

                if (!profile) {
                    return;
                }

                // Profile heading
                document.getElementById("name").textContent =
                    profile.fullName || "Donor";

                document.getElementById("donorId").textContent =
                    "Donor ID : BD" + String(profile.id).padStart(6, "0");


                // Personal Information

                document.getElementById("fullName").value =
                    profile.fullName || "";

                document.getElementById("email").value =
                    profile.email || "";

                document.getElementById("phone").value =
                    profile.phone || "";

                document.getElementById("dob").value =
                    profile.dob || "";

                document.getElementById("gender").value =
                    profile.gender || "";


                // Donation Information

                document.getElementById("bloodGroup").value =
                    profile.bloodGroup || "";

                document.getElementById("lastDonation").value =
                    profile.lastDonationDate || "";


                // Address

                document.getElementById("state").value =
                    profile.state || "";

                document.getElementById("city").value =
                    profile.city || "";

                document.getElementById("landmark").value =
                    profile.landmark || "";


                // Medical Information

                document.getElementById("medical").value =
                    profile.medicalCondition || "None";


                // These are currently frontend-only fields
                // because they do not exist in your users table.

                document.getElementById("donations").value = 0;
                document.getElementById("weight").value = "";
                document.getElementById("emergencyName").value = "";
                document.getElementById("emergencyPhone").value = "";

            })
            .catch(error => {

                console.error("Profile loading error:", error);

                alert("Unable to load profile from the server.");
            });
    }


    // --------------------------------------------------
    // Edit Profile
    // --------------------------------------------------

    editBtn.addEventListener("click", () => {

        enableFields();

        alert("You can now edit your profile.");

    });


    // --------------------------------------------------
    // Save Profile
    // --------------------------------------------------

    saveBtn.addEventListener("click", () => {

        const dob = document.getElementById("dob").value;

        // ----------------------------------------------
        // Age validation
        // ----------------------------------------------

        if (dob !== "") {

            const birth = new Date(dob);
            const today = new Date();

            let age =
                today.getFullYear() -
                birth.getFullYear();

            const month =
                today.getMonth() -
                birth.getMonth();

            if (
                month < 0 ||
                (month === 0 &&
                 today.getDate() < birth.getDate())
            ) {
                age--;
            }

            if (age < 18) {

                alert(
                    "You must be at least 18 years old to donate blood."
                );

                return;
            }
        }


        // ----------------------------------------------
        // Last donation eligibility
        // ----------------------------------------------

        let eligible = true;

        const lastDonation =
            document.getElementById("lastDonation").value;

        if (lastDonation !== "") {

            const last = new Date(lastDonation);
            const today = new Date();

            const diff =
                (today - last) /
                (1000 * 60 * 60 * 24);

            if (diff < 90) {
                eligible = false;
            }
        }


        // ----------------------------------------------
        // Prepare data
        // ----------------------------------------------

        const formData = new URLSearchParams();

        formData.append(
            "fullName",
            document.getElementById("fullName").value
        );

        formData.append(
            "email",
            document.getElementById("email").value
        );

        formData.append(
            "phone",
            document.getElementById("phone").value
        );

        formData.append(
            "dob",
            document.getElementById("dob").value
        );

        formData.append(
            "gender",
            document.getElementById("gender").value
        );

        formData.append(
            "bloodGroup",
            document.getElementById("bloodGroup").value
        );

        formData.append(
            "lastDonationDate",
            document.getElementById("lastDonation").value
        );

        formData.append(
            "state",
            document.getElementById("state").value
        );

        formData.append(
            "city",
            document.getElementById("city").value
        );

        formData.append(
            "landmark",
            document.getElementById("landmark").value
        );

        formData.append(
            "medicalCondition",
            document.getElementById("medical").value
        );


        // ----------------------------------------------
        // Send changes to backend
        // ----------------------------------------------

        fetch("ProfileServlet", {
            method: "POST",
            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },
            body: formData.toString()
        })
        .then(response => response.text())
        .then(result => {

            if (result === "SUCCESS") {

                document.getElementById("name").textContent =
                    document.getElementById("fullName").value;

                disableFields();

                if (eligible) {

                    alert(
                        "Profile updated successfully.\n\n" +
                        "Eligibility Status: Eligible to Donate"
                    );

                } else {

                    alert(
                        "Profile updated successfully.\n\n" +
                        "Eligibility Status: Not Eligible " +
                        "(Wait 3 months after your last donation)."
                    );
                }

            } else {

                alert(
                    "Profile update failed.\n\n" +
                    result
                );
            }

        })
        .catch(error => {

            console.error("Profile update error:", error);

            alert(
                "Unable to connect to the server."
            );
        });

    });


    // --------------------------------------------------
    // Start
    // --------------------------------------------------

    disableFields();

    loadProfile();

});