document.addEventListener("DOMContentLoaded", () => {

```
const form = document.querySelector("form");

if (!form) {

    console.error("Login form not found.");

    return;

}


form.addEventListener("submit", (e) => {

    const email =
        document.querySelector("#email").value.trim();

    const password =
        document.querySelector("#password").value;


    if (email === "" || password === "") {

        e.preventDefault();

        alert("Please enter your email and password.");

        return;

    }

    /*
     * Do NOT use e.preventDefault()
     * when the details are valid.
     *
     * The form will be submitted to:
     *
     * LoginServlet
     */

});
```

});
