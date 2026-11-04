document.addEventListener("DOMContentLoaded", function () {


const form = document.getElementById("requestForm");
const requestList = document.getElementById("requestList");
const createButton = document.getElementById("createRequestBtn");


// ==========================
// CREATE REQUEST
// ==========================

if (form && createButton) {

    createButton.addEventListener("click", function () {

        const patientName =
            document.getElementById("patientName").value.trim();

        const bloodGroup =
            document.getElementById("requiredBloodGroup").value;

        const unitsValue =
            document.getElementById("units").value.trim();

        const hospitalName =
            document.getElementById("hospitalName").value.trim();

        const hospitalAddress =
            document.getElementById("hospitalAddress").value.trim();

        const city =
            document.getElementById("city").value.trim();

        const requiredDate =
            document.getElementById("requiredDate").value;

        const priority =
            document.getElementById("priority").value;

        const contactNumber =
            document.getElementById("contactNumber").value.trim();


        // ==========================
        // VALIDATION
        // ==========================

        if (patientName === "") {
            alert("Please enter patient name.");
            return;
        }

        if (bloodGroup === "") {
            alert("Please select blood group.");
            return;
        }

        if (unitsValue === "") {
            alert("Please enter units required.");
            return;
        }

        const units = parseInt(unitsValue);

        if (isNaN(units) || units < 1 || units > 10) {
            alert("Units must be between 1 and 10.");
            return;
        }

        if (hospitalName === "") {
            alert("Please enter hospital name.");
            return;
        }

        if (hospitalAddress === "") {
            alert("Please enter hospital address.");
            return;
        }

        if (city === "") {
            alert("Please enter city.");
            return;
        }

        if (requiredDate === "") {
            alert("Please select required date.");
            return;
        }

        const today =
            new Date().toISOString().split("T")[0];

        if (requiredDate < today) {
            alert("Required date cannot be in the past.");
            return;
        }

        if (priority === "") {
            alert("Please select priority.");
            return;
        }

        if (contactNumber === "") {
            alert("Please enter contact number.");
            return;
        }


        // ==========================
        // CREATE FORM DATA
        // ==========================

        const formData = new FormData();

        formData.append("patientName", patientName);
        formData.append("bloodGroup", bloodGroup);
        formData.append("units", units);
        formData.append("hospitalName", hospitalName);
        formData.append("hospitalAddress", hospitalAddress);
        formData.append("city", city);
        formData.append("requiredDate", requiredDate);
        formData.append("priority", priority);
        formData.append("contactNumber", contactNumber);


        // ==========================
        // SEND TO SERVLET
        // ==========================

        createButton.disabled = true;
        createButton.textContent = "Creating Request...";


        fetch("BloodRequestServlet", {
            method: "POST",
            body: formData
        })

        .then(function (response) {

            if (response.status === 401) {

                alert(
                    "Please login before creating a blood request."
                );

                window.location.href = "login.html";

                return null;
            }

            return response.text();

        })

        .then(function (result) {

            createButton.disabled = false;
            createButton.textContent =
                "📌 Create Blood Request";


            if (result === null) {
                return;
            }


            result = result.trim();


            if (result === "SUCCESS") {

                alert(
                    "Blood request created successfully!"
                );

                form.reset();

                loadRequests();

            } else {

                alert(
                    "Unable to create blood request.\n\n" +
                    result
                );

            }

        })

        .catch(function (error) {

            console.error(error);

            createButton.disabled = false;

            createButton.textContent =
                "📌 Create Blood Request";

            alert(
                "Something went wrong while creating the blood request."
            );

        });

    });

}


// ==========================
// LOAD REQUESTS
// ==========================

if (requestList) {
    loadRequests();
}


});

function loadRequests() {


const requestList =
    document.getElementById("requestList");


requestList.innerHTML = `
    <div class="empty">

        <div class="empty-icon">
            🔎
        </div>

        <h3>
            Loading Requests...
        </h3>

        <p>
            Please wait while we load the latest
            blood requests.
        </p>

    </div>
`;


fetch("BloodRequestServlet")

    .then(function (response) {

        if (response.status === 401) {

            window.location.href = "login.html";

            return null;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to load requests"
            );

        }


        return response.json();

    })

    .then(function (requests) {

        if (!requests) {
            return;
        }


        requestList.innerHTML = "";


        if (requests.length === 0) {

            requestList.innerHTML = `
                <div class="empty">

                    <div class="empty-icon">
                        📭
                    </div>

                    <h3>
                        No Blood Requests
                    </h3>

                    <p>
                        There are currently no
                        blood requests.
                    </p>

                </div>
            `;

            return;
        }


        requests.forEach(function (request) {

            const item =
                document.createElement("div");


            item.className =
                "request-item";


            let badgeClass =
                "badge-normal";


            if (
                request.priority &&
                request.priority.toLowerCase() === "urgent"
            ) {

                badgeClass =
                    "badge-urgent";

            } else if (
                request.priority &&
                request.priority.toLowerCase() === "emergency"
            ) {

                badgeClass =
                    "badge-emergency";

            }


            item.innerHTML = `
                <div class="request-info">

                    <h3>
                        🩸
                        ${escapeHTML(request.bloodGroup)}
                        Blood Required
                    </h3>

                    <p>
                        Patient:
                        ${escapeHTML(request.patientName)}
                    </p>

                    <p>
                        🏥
                        ${escapeHTML(request.hospitalName)}
                    </p>

                    <p>
                        📍
                        ${escapeHTML(request.city || "")}
                    </p>

                    <p>
                        📅 Required:
                        ${escapeHTML(request.requiredDate)}
                    </p>

                    <p>
                        Units Required:
                        ${escapeHTML(request.units)}
                    </p>

                </div>


                <div class="request-tags">

                    <span class="badge badge-group">
                        ${escapeHTML(request.bloodGroup)}
                    </span>

                    <span class="badge ${badgeClass}">
                        ${escapeHTML(request.priority || "Normal")}
                    </span>

                    <button
                        type="button"
                        class="fulfill-btn"
                        onclick="contactRequester('${escapeAttribute(request.contactNumber)}')">

                        ❤️ Help

                    </button>

                </div>
            `;


            requestList.appendChild(item);

        });

    })

    .catch(function (error) {

        console.error(error);


        requestList.innerHTML = `
            <div class="empty">

                <div class="empty-icon">
                    ❌
                </div>

                <h3>
                    Unable to Load Requests
                </h3>

                <p>
                    Please try again.
                </p>

            </div>
        `;

    });


}

function contactRequester(phone) {


if (!phone) {

    alert(
        "Contact number is not available."
    );

    return;
}


window.location.href =
    "tel:" + phone;


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
