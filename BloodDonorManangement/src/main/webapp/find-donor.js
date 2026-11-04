document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("donorSearchForm");
    const results = document.getElementById("donorResults");

    if (!form || !results) {
        return;
    }

    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const bloodGroup =
            document.getElementById("bloodGroup").value;

        const city =
            document.getElementById("city").value.trim();

        if (bloodGroup === "" || city === "") {

            alert("Please select a blood group and enter a city.");
            return;
        }

        results.innerHTML = `
            <div class="request">
                <h3>🔎 Searching for donors...</h3>
                <p>Please wait.</p>
            </div>
        `;

        fetch(
            "FindDonorServlet?bloodGroup="
            + encodeURIComponent(bloodGroup)
            + "&city="
            + encodeURIComponent(city)
        )

        .then(function (response) {

            if (response.status === 401) {
                window.location.href = "login.html";
                return null;
            }

            if (!response.ok) {
                throw new Error("Search failed");
            }

            return response.json();
        })

        .then(function (donors) {

            if (!donors) {
                return;
            }

            results.innerHTML = "";

            if (donors.length === 0) {

                results.innerHTML = `
                    <div class="request">
                        <h3>😔 No Donors Found</h3>
                        <p>
                            No ${bloodGroup} donors were found
                            in ${city}.
                        </p>
                    </div>
                `;

                return;
            }

            const grid =
                document.createElement("div");

            grid.className = "donor-grid";

            donors.forEach(function (donor) {

                const card =
                    document.createElement("div");

                card.className = "donor-card";

                const initial =
                    donor.fullName
                        ? donor.fullName
                            .charAt(0)
                            .toUpperCase()
                        : "D";

                card.innerHTML = `
                    <div class="donor-avatar">
                        ${initial}
                    </div>

                    <h3>
                        ${escapeHTML(donor.fullName)}
                    </h3>

                    <p>
                        📍 ${escapeHTML(donor.city)},
                        ${escapeHTML(donor.state)}
                    </p>

                    <p>
                        📞 ${escapeHTML(donor.phone)}
                    </p>

                    <span class="donor-group">
                        ${escapeHTML(donor.bloodGroup)}
                    </span>

                    <span class="donor-status status-available">
                        Available
                    </span>

                    <button
                        type="button"
                        class="contact-btn"
                        onclick="contactDonor('${escapeAttribute(donor.phone)}')">
                        📞 Contact Donor
                    </button>
                `;

                grid.appendChild(card);
            });

            results.appendChild(grid);

        })

        .catch(function (error) {

            console.error(error);

            results.innerHTML = `
                <div class="request">
                    <h3>❌ Error</h3>
                    <p>
                        Unable to search donors.
                        Please try again.
                    </p>
                </div>
            `;
        });
    });

});


function contactDonor(phone) {

    if (!phone) {

        alert("Phone number is not available.");
        return;
    }

    window.location.href = "tel:" + phone;
}


function escapeHTML(value) {

    if (!value) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function escapeAttribute(value) {

    if (!value) {
        return "";
    }

    return String(value)
        .replace(/\\/g, "\\\\")
        .replace(/'/g, "\\'");
}