
var SETTINGS_URL = "SettingsServlet";
var currentLanguage = "English";
var currentTheme = "Light";

var checkboxMap = {
    availability: "donorAvailable",
    emergency: "emergencyAlerts",
    nearby: "bloodRequestAlerts",
    emailNotifications: "emailNotifications",
    donationReminder: "smsNotifications",
    requestUpdates: "bloodRequestAlerts",
    showPhone: "showPhone",
    showLocation: "showEmail",
    profileVisibility: "profileVisibility"
};

function loadSettings() {
    fetch(SETTINGS_URL, {
        method: "GET",
        credentials: "same-origin",
        cache: "no-store"
    })
    .then(function (response) {
        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }
        return response.text();
    })
    .then(function (text) {
        var data = JSON.parse(text);
		currentLanguage = data.language === "ta" ? "Tamil" : "English";
		currentTheme = data.darkMode ? "Dark" : "Light";
		applyLanguage();
		applyTheme();
        var htmlId;
        var javaKey;
        var element;
        var language;
        var timezone;
        var theme;
        var communicationValue;
        var radio;

        for (htmlId in checkboxMap) {
            if (checkboxMap.hasOwnProperty(htmlId)) {
                javaKey = checkboxMap[htmlId];
                element = document.getElementById(htmlId);

                if (element && typeof data[javaKey] === "boolean") {
                    element.checked = data[javaKey];
                }
            }
        }

        language = document.getElementById("language");
        if (language) {
            language.value = data.language === "ta" ? "Tamil" : "English";
        }

        timezone = document.getElementById("timezone");
        if (timezone) {
            timezone.value = data.timezone === "UTC"
                ? "UTC"
                : "India Standard Time (IST)";
        }

        theme = document.getElementById("theme");
        if (theme) {
            theme.value = data.darkMode ? "Dark" : "Light";
        }

        communicationValue = data.emergencyAlerts
            ? "all"
            : (data.emailNotifications ? "important" : "none");

        radio = document.querySelector(
            'input[name="communication"][value="' + communicationValue + '"]'
        );

        if (radio) {
            radio.checked = true;
        }

        applyLanguage();
        applyTheme();
    })
    .catch(function (error) {
        console.log("Settings loading error:", error);
        alert("Could not load settings. Check the browser console.");
    });
}

function saveSettings() {
    var params = new URLSearchParams();
    var htmlId;
    var javaKey;
    var element;
    var language = document.getElementById("language");
    var timezone = document.getElementById("timezone");
    var theme = document.getElementById("theme");
    var communication = document.querySelector(
        'input[name="communication"]:checked'
    );

    for (htmlId in checkboxMap) {
        if (checkboxMap.hasOwnProperty(htmlId)) {
            javaKey = checkboxMap[htmlId];
            element = document.getElementById(htmlId);

            params.set(
                javaKey,
                element ? String(element.checked) : "false"
            );
        }
    }

    params.set(
        "language",
        language && language.value === "Tamil" ? "ta" : "en"
    );

    params.set(
        "timezone",
        timezone && timezone.value === "UTC" ? "UTC" : "Asia/Kolkata"
    );

    params.set("darkMode", String(theme && theme.value === "Dark"));
    params.set("largeText", "false");
    params.set("reducedMotion", "false");
    params.set("autoLogout", "false");
    params.set("rememberLogin", "true");

    if (communication) {
        if (communication.value === "none") {
            params.set("emailNotifications", "false");
            params.set("smsNotifications", "false");
        } else if (communication.value === "important") {
            params.set("emailNotifications", "true");
            params.set("smsNotifications", "false");
        }
    }

    fetch(SETTINGS_URL, {
        method: "POST",
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
        },
        body: params.toString()
    })
    .then(function (response) {
        if (!response.ok) {
            throw new Error("HTTP " + response.status);
        }
        return response.text();
    })
    .then(function (text) {
        var result = JSON.parse(text);

        if (!result.success) {
            throw new Error(result.message || "Save failed");
        }

        applyLanguage();
        applyTheme();

        alert(language && language.value === "Tamil"
            ? "அமைப்புகள் வெற்றிகரமாகச் சேமிக்கப்பட்டன."
            : "Settings saved successfully.");
    })
    .catch(function (error) {
        console.log("Settings saving error:", error);
        alert("Could not save settings. Check the browser console.");
    });
}


var tamilTranslations = {
    "Dashboard": "முகப்புப் பலகை",
    "My Profile": "எனது சுயவிவரம்",
    "Donate Blood": "இரத்த தானம் செய்க",
    "Find Donor": "இரத்த தானம் செய்பவரைத் தேடு",
    "Requests": "கோரிக்கைகள்",
    "Settings": "அமைப்புகள்",
    "Logout": "வெளியேறு",

    "Manage your Red Pulse preferences, notifications and account security.": "உங்கள் Red Pulse விருப்பங்கள், அறிவிப்புகள் மற்றும் கணக்குப் பாதுகாப்பை நிர்வகிக்கவும்.",
    "Account Preferences": "கணக்கு விருப்பங்கள்",
    "Customize how your account works.": "உங்கள் கணக்கு செயல்படும் முறையைத் தனிப்பயனாக்கவும்.",

    "Donor Availability": "இரத்த தானம் செய்பவரின் கிடைக்கும் நிலை",
    "Allow your profile to appear when patients search for donors.": "நோயாளிகள் இரத்த தானம் செய்பவர்களைத் தேடும்போது உங்கள் சுயவிவரம் காட்டப்பட அனுமதிக்கவும்.",
    "Emergency Requests": "அவசரக் கோரிக்கைகள்",
    "Receive urgent blood donation requests from your city.": "உங்கள் நகரத்திலிருந்து அவசர இரத்தத் தானக் கோரிக்கைகளைப் பெறவும்.",
    "Nearby Donor Alerts": "அருகிலுள்ள இரத்த தான அறிவிப்புகள்",
    "Get notified when a nearby blood requirement matches your group.": "அருகில் உங்கள் இரத்தக் குழுவிற்கான தேவை ஏற்படும்போது அறிவிப்பைப் பெறவும்.",

    "Notifications": "அறிவிப்புகள்",
    "Choose the alerts you want to receive.": "நீங்கள் பெற விரும்பும் அறிவிப்புகளைத் தேர்ந்தெடுக்கவும்.",
    "Email Notifications": "மின்னஞ்சல் அறிவிப்புகள்",
    "Receive important account and donation updates by email.": "கணக்கு மற்றும் இரத்த தானம் தொடர்பான முக்கிய புதுப்பிப்புகளை மின்னஞ்சலில் பெறவும்.",
    "Donation Reminders": "இரத்த தான நினைவூட்டல்கள்",
    "Receive reminders when you become eligible to donate again.": "மீண்டும் இரத்த தானம் செய்யத் தகுதியானபோது நினைவூட்டல்களைப் பெறவும்.",
    "Request Updates": "கோரிக்கை புதுப்பிப்புகள்",
    "Get updates about blood requests you created or responded to.": "நீங்கள் உருவாக்கிய அல்லது பதிலளித்த இரத்தக் கோரிக்கைகளின் புதுப்பிப்புகளைப் பெறவும்.",

    "Privacy & Visibility": "தனியுரிமை மற்றும் தெரிவுநிலை",
    "Control what other users can see.": "மற்ற பயனர்கள் எதைப் பார்க்கலாம் என்பதைக் கட்டுப்படுத்தவும்.",
    "Show Phone Number": "தொலைபேசி எண்ணைக் காட்டு",
    "Allow verified users to view your contact number.": "சரிபார்க்கப்பட்ட பயனர்கள் உங்கள் தொடர்பு எண்ணைப் பார்க்க அனுமதிக்கவும்.",
    "Show Exact Location": "சரியான இருப்பிடத்தைக் காட்டு",
    "Display your city only instead of your exact address.": "சரியான முகவரிக்குப் பதிலாக உங்கள் நகரத்தை மட்டும் காட்டவும்.",
    "Profile Search Visibility": "சுயவிவரத் தேடல் தெரிவுநிலை",
    "Allow your donor profile to appear in donor searches.": "இரத்த தானம் செய்பவர்களுக்கான தேடலில் உங்கள் சுயவிவரம் தோன்ற அனுமதிக்கவும்.",

    "Security": "பாதுகாப்பு",
    "Protect your Red Pulse account.": "உங்கள் Red Pulse கணக்கைப் பாதுகாக்கவும்.",
    "Password": "கடவுச்சொல்",
    "Keep your account secure by using a strong password.": "வலுவான கடவுச்சொல்லைப் பயன்படுத்தி உங்கள் கணக்கைப் பாதுகாக்கவும்.",
    "Change Password": "கடவுச்சொல்லை மாற்று",
    "Login Protection": "உள்நுழைவு பாதுகாப்பு",
    "Additional verification can help protect your account.": "கூடுதல் சரிபார்ப்பு உங்கள் கணக்கைப் பாதுகாக்க உதவும்.",
    "Enabled": "இயக்கப்பட்டுள்ளது",
    "Active Session": "செயலில் உள்ள அமர்வு",
    "Current browser session": "தற்போதைய உலாவி அமர்வு",

    "Communication Preferences": "தொடர்பு விருப்பங்கள்",
    "Choose how Red Pulse communicates with you.": "Red Pulse உங்களுடன் தொடர்புகொள்ளும் முறையைத் தேர்ந்தெடுக்கவும்.",
    "All Updates": "அனைத்து புதுப்பிப்புகளும்",
    "Receive important system, donation and emergency updates.": "முக்கியமான கணினி, இரத்த தானம் மற்றும் அவசரப் புதுப்பிப்புகளைப் பெறவும்.",
    "Important Only": "முக்கியமானவை மட்டும்",
    "Receive only emergency and account-related notifications.": "அவசர மற்றும் கணக்கு தொடர்பான அறிவிப்புகளை மட்டும் பெறவும்.",
    "Minimal Communication": "அத்தியாவசியத் தகவல்கள் மட்டும்",
    "Receive only essential security notifications.": "அத்தியாவசிய பாதுகாப்பு அறிவிப்புகளை மட்டும் பெறவும்.",

    "System Preferences": "கணினி விருப்பங்கள்",
    "Personalize your Red Pulse experience.": "உங்கள் Red Pulse அனுபவத்தைத் தனிப்பயனாக்கவும்.",
    "Language": "மொழி",
    "Time Zone": "நேர மண்டலம்",
    "Interface Theme": "திரைத் தோற்றம்",
    "Light": "ஒளி",
    "Dark": "இருள்",
    "System Default": "கணினியின் இயல்புநிலை",
    "Save Your Preferences": "உங்கள் விருப்பங்களைச் சேமிக்கவும்",
    "Your preferences help Red Pulse deliver the right information at the right time.": "சரியான நேரத்தில் சரியான தகவல்களை வழங்க உங்கள் விருப்பங்கள் Red Pulse-க்கு உதவுகின்றன.",
    "Save Changes": "மாற்றங்களைச் சேமி",
	/* Dashboard */
	"Good Evening": "மாலை வணக்கம்",
	"Good Morning": "காலை வணக்கம்",
	"Good Afternoon": "மதிய வணக்கம்",
	"Your kindness can save a life today.": "உங்கள் கருணை இன்று ஒரு உயிரைக் காப்பாற்றும்.",
	"Emergency Help": "அவசர உதவி",
	"Blood Group": "இரத்தக் குழு",
	"Donation Count": "இரத்த தான எண்ணிக்கை",
	"Status": "நிலை",
	"Available": "கிடைக்கிறது",
	"Ready to donate": "தானம் செய்யத் தயார்",
	"Total donations made": "மொத்த இரத்த தானங்கள்",
	"Quick Actions": "விரைவான செயல்கள்",
	"Recent Blood Requests": "சமீபத்திய இரத்தக் கோரிக்கைகள்",
	"Donate Now": "இப்போது இரத்த தானம் செய்க",
	"Search Donor": "இரத்த தானம் செய்பவரைத் தேடுக",
	"Create Request": "கோரிக்கையை உருவாக்கு",
	"Location: Chennai": "இடம்: சென்னை",
	"Emergency": "அவசரம்",
	"Urgent": "அவசரமானது",

	/* Common pages */
	"Donate Blood": "இரத்த தானம் செய்க",
	"Find Donor": "இரத்த தானம் செய்பவரைத் தேடுக",
	"Find Blood Donors": "இரத்த தானம் செய்பவர்களைக் கண்டறியவும்",
	"Search": "தேடு",
	"Search Donors": "இரத்த தானம் செய்பவர்களைத் தேடு",
	"Blood Requests": "இரத்தக் கோரிக்கைகள்",
	"Create Blood Request": "இரத்தக் கோரிக்கையை உருவாக்கு",
	"Submit Request": "கோரிக்கையைச் சமர்ப்பி",
	"Name": "பெயர்",
	"Phone": "தொலைபேசி",
	"Email": "மின்னஞ்சல்",
	"Blood Group:": "இரத்தக் குழு:",
	"City": "நகரம்",
	"State": "மாநிலம்",
	"Submit": "சமர்ப்பி",
	"Cancel": "ரத்து செய்",
	"Available": "கிடைக்கிறது",
	"Logout": "வெளியேறு",
	/* Blood Request Form */
	"Blood Requests": "இரத்தக் கோரிக்கைகள்",
	"Create and manage blood requests.": "இரத்தக் கோரிக்கைகளை உருவாக்கி நிர்வகிக்கவும்.",
	"Create Blood Request": "இரத்தக் கோரிக்கையை உருவாக்கு",
	"Enter the details of the blood you need.": "உங்களுக்குத் தேவையான இரத்தத்தின் விவரங்களை உள்ளிடவும்.",
	"Patient Name": "நோயாளியின் பெயர்",
	"Patient பெயர்": "நோயாளியின் பெயர்",
	"Enter patient name": "நோயாளியின் பெயரை உள்ளிடவும்",
	"Required Blood Group": "தேவையான இரத்தக் குழு",
	"Select Blood Group": "இரத்தக் குழுவைத் தேர்ந்தெடுக்கவும்",
	"Units Required": "தேவையான அலகுகள்",
	"Number of units": "அலகுகளின் எண்ணிக்கை",
	"Hospital Name": "மருத்துவமனையின் பெயர்",
	"Enter hospital name": "மருத்துவமனையின் பெயரை உள்ளிடவும்",
	"Hospital Address": "மருத்துவமனை முகவரி",
	"Enter hospital address": "மருத்துவமனை முகவரியை உள்ளிடவும்",
	"City": "நகரம்",
	"Enter city": "நகரத்தை உள்ளிடவும்",
	"Required Date": "தேவையான தேதி",
	"Priority": "முன்னுரிமை",
	"Select Priority": "முன்னுரிமையைத் தேர்ந்தெடுக்கவும்",
	"Contact Number": "தொடர்பு எண்",
	"Enter contact number": "தொடர்பு எண்ணை உள்ளிடவும்",
	"Submit Request": "கோரிக்கையைச் சமர்ப்பிக்கவும்",
	"Back to Dashboard": "முகப்புப் பலகைக்குத் திரும்பு",
	"Blood Group": "இரத்தக் குழு",
	"Name": "பெயர்",
	"Phone Number": "தொலைபேசி எண்",
	"Address": "முகவரி",
	"Submit": "சமர்ப்பி",
	"Cancel": "ரத்து செய்",
	"Search Donor": "இரத்த தானம் செய்பவரைத் தேடுக",
	"Find Blood Donors": "இரத்த தானம் செய்பவர்களைக் கண்டறியவும்",
	"Search Donors": "இரத்த தானம் செய்பவர்களைத் தேடவும்",
	"Donor Details": "இரத்த தானம் செய்பவரின் விவரங்கள்",
	"No donors found": "இரத்த தானம் செய்பவர்கள் எவரும் கிடைக்கவில்லை",
	"Save Changes": "மாற்றங்களைச் சேமிக்கவும்",

	// Dashboard and recent blood requests
	"Recent Blood": "சமீபத்திய இரத்தக் கோரிக்கைகள்",
	"A+ Blood Required": "A+ இரத்தம் தேவை",
	"O- Blood Required": "O- இரத்தம் தேவை",
	"A small act of kindness can become someone's biggest hope.": "ஒரு சிறிய உதவி ஒருவரின் மிகப்பெரிய நம்பிக்கையாக மாறலாம்.",
	"Donate blood. Save lives. Be a hero.": "இரத்த தானம் செய்யுங்கள். உயிர்களைக் காப்பாற்றுங்கள். நாயகராக இருங்கள்.",

	// Profile
	"Full Name": "முழுப் பெயர்",
	"Full பெயர்": "முழுப் பெயர்",
	"Date of Birth": "பிறந்த தேதி",
	"Number": "தொலைபேசி எண்",
	"Gender": "பாலினம்",
	"Personal Information": "தனிப்பட்ட தகவல்கள்",
	"Donation Information": "இரத்த தானத் தகவல்கள்",
	"Last Donation Date": "கடைசியாக இரத்த தானம் செய்த தேதி",
	"Total Donations": "மொத்த இரத்த தானங்கள்",
	"Weight (kg)": "எடை (கிலோ)",
	"Landmark": "அடையாள இடம்",
	"Health Information": "உடல்நலத் தகவல்கள்",
	"Medical Condition": "மருத்துவ நிலை",
	"None": "எதுவுமில்லை",
	"Diabetes": "நீரிழிவு நோய்",
	"Blood Pressure": "இரத்த அழுத்தம்",
	"Asthma": "ஆஸ்துமா",
	"Heart Disease": "இதய நோய்",
	"Other": "மற்றவை",
	"Emergency Contact": "அவசரத் தொடர்பு எண்",
	"அவசரம் Contact": "அவசரத் தொடர்பு எண்",
	"Edit Profile": "சுயவிவரத்தைத் திருத்து",

	// Donate blood
	"Confirm Your Blood Donation": "உங்கள் இரத்த தானத்தை உறுதிப்படுத்துங்கள்",
	"Please provide the details below to register your upcoming blood donation.": "உங்கள் அடுத்த இரத்த தானத்தைப் பதிவு செய்யக் கீழே உள்ள விவரங்களை வழங்கவும்.",
	"Hospital Name": "மருத்துவமனையின் பெயர்",
	"Hospital பெயர்": "மருத்துவமனையின் பெயர்",
	"Donation Date": "இரத்த தானத் தேதி",
	"Hospital Address": "மருத்துவமனை முகவரி",
	"மருத்துவமனை முகவரி": "மருத்துவமனை முகவரி",
	"Preferred Donation Time": "விரும்பும் இரத்த தான நேரம்",
	"Blood Group": "இரத்தக் குழு",
	"இரத்தக் குழு": "இரத்தக் குழு",
	"Select Blood Group": "இரத்தக் குழுவைத் தேர்ந்தெடுக்கவும்",
	"Select இரத்தக் குழு": "இரத்தக் குழுவைத் தேர்ந்தெடுக்கவும்",
	"Donation Type": "இரத்த தான வகை",
	"Select Donation Type": "இரத்த தான வகையைத் தேர்ந்தெடுக்கவும்",
	"Whole Blood": "முழு இரத்தம்",
	"Plasma": "பிளாஸ்மா",
	"Platelets": "இரத்தத் தட்டுகள்",
	"Every Donation Matters": "ஒவ்வொரு இரத்த தானமும் முக்கியம்",
	"Please make sure you are eligible and healthy before donating blood. Your contribution can help patients during emergencies, surgeries, and critical medical treatments.": "இரத்த தானம் செய்வதற்கு முன் நீங்கள் தகுதியுடனும் ஆரோக்கியத்துடனும் இருப்பதை உறுதிப்படுத்திக் கொள்ளுங்கள். உங்கள் பங்களிப்பு அவசரநிலைகள், அறுவை சிகிச்சைகள் மற்றும் முக்கியமான மருத்துவச் சிகிச்சைகளின்போது நோயாளிகளுக்கு உதவும்.",
	"Confirm Donation": "இரத்த தானத்தை உறுதிப்படுத்து",
	"A single donation can make a world of difference.": "ஒரு இரத்த தானம் உலகில் மிகப்பெரிய மாற்றத்தை ஏற்படுத்தும்.",
	"Thank you for choosing to be someone's lifesaver.": "ஒருவரின் உயிரைக் காப்பாற்ற முன்வந்ததற்கு நன்றி.",

	// Find a donor
	"Find a Blood Donor": "இரத்த தானம் செய்பவரைக் கண்டறியுங்கள்",
	"Find a Blood Donor ❤️": "இரத்த தானம் செய்பவரைக் கண்டறியுங்கள் ❤️",
	"Find suitable blood donors quickly and easily.": "பொருத்தமான இரத்த தானம் செய்பவர்களை விரைவாகவும் எளிதாகவும் கண்டறியுங்கள்.",
	"Home": "முகப்பு",
	"← முகப்புப் பலகை": "← முகப்புப் பலகை",
	"Search for Donors": "இரத்த தானம் செய்பவர்களைத் தேடுங்கள்",
	"தேடு for Donors": "இரத்த தானம் செய்பவர்களைத் தேடுங்கள்",
	"Enter the blood group and city to find available donors.": "கிடைக்கக்கூடிய இரத்த தானம் செய்பவர்களைக் கண்டறிய இரத்தக் குழுவையும் நகரத்தையும் உள்ளிடுங்கள்.",
	"City": "நகரம்",
	"நகரம்": "நகரம்",
	"Search for Blood Donors": "இரத்த தானம் செய்பவர்களைத் தேடுங்கள்",
	"இரத்த தானம் செய்பவரைத் தேடுகs": "இரத்த தானம் செய்பவர்களைத் தேடுங்கள்",
	"Available Donors": "கிடைக்கக்கூடிய இரத்த தானம் செய்பவர்கள்",
	"கிடைக்கிறது Donors": "கிடைக்கக்கூடிய இரத்த தானம் செய்பவர்கள்",
	"Matching donors will appear here.": "பொருத்தமான இரத்த தானம் செய்பவர்கள் இங்கே தோன்றுவார்கள்.",
	"Search for a Donor": "இரத்த தானம் செய்பவரைத் தேடுங்கள்",
	"தேடு for a Donor": "இரத்த தானம் செய்பவரைத் தேடுங்கள்",
	"Select a blood group and enter a city above to find matching donors.": "பொருத்தமான இரத்த தானம் செய்பவர்களைக் கண்டறிய மேலே இரத்தக் குழுவைத் தேர்ந்தெடுத்து நகரத்தை உள்ளிடுங்கள்.",
	"Search donors according to the required blood group.": "தேவையான இரத்தக் குழுவின் அடிப்படையில் இரத்த தானம் செய்பவர்களைத் தேடுங்கள்.",
	"தேடு donors according to the required blood group.": "தேவையான இரத்தக் குழுவின் அடிப்படையில் இரத்த தானம் செய்பவர்களைத் தேடுங்கள்.",
	"Nearby Donors": "அருகிலுள்ள இரத்த தானம் செய்பவர்கள்",
	"Find donors available in your selected city.": "நீங்கள் தேர்ந்தெடுத்த நகரத்தில் கிடைக்கும் இரத்த தானம் செய்பவர்களைக் கண்டறியுங்கள்.",
	"Save a Life": "ஒரு உயிரைக் காப்பாற்றுங்கள்",
	"Connecting with the right donor can save a life.": "சரியான இரத்த தானம் செய்பவருடன் இணைப்பது ஒரு உயிரைக் காப்பாற்றும்.",
	"Your search could save a life.": "உங்கள் தேடல் ஒரு உயிரைக் காப்பாற்றலாம்.",
	"Find the right donor and help someone in need.": "சரியான இரத்த தானம் செய்பவரைக் கண்டறிந்து உதவி தேவைப்படுபவருக்கு உதவுங்கள்.",

	// Blood requests
	"Blood Requests": "இரத்தக் கோரிக்கைகள்",
	"Blood கோரிக்கைகள்": "இரத்தக் கோரிக்கைகள்",
	"இரத்தக் கோரிக்கைகளை உருவாக்கி நிர்வகிக்கவும்.": "இரத்தக் கோரிக்கைகளை உருவாக்கி நிர்வகிக்கவும்.",
	"Create Blood Request": "இரத்தக் கோரிக்கையை உருவாக்கு",
	"இரத்தக் கோரிக்கையை உருவாக்கு": "இரத்தக் கோரிக்கையை உருவாக்கு",
	"Enter the details of the blood you need.": "உங்களுக்குத் தேவையான இரத்தத்தின் விவரங்களை உள்ளிடுங்கள்.",
	"Patient Name": "நோயாளியின் பெயர்",
	"நோயாளியின் பெயர்": "நோயாளியின் பெயர்",
	"Required Blood Group": "தேவையான இரத்தக் குழு",
	"Required இரத்தக் குழு": "தேவையான இரத்தக் குழு",
	"Required Units": "தேவையான இரத்த அலகுகள்",
	"தேவையான அலகுகள்": "தேவையான இரத்த அலகுகள்",
	"Required Date": "தேவையான தேதி",
	"தேவையான தேதி": "தேவையான தேதி",
	"Priority": "முன்னுரிமை",
	"Select Priority": "முன்னுரிமையைத் தேர்ந்தெடுக்கவும்",
	"Select முன்னுரிமை": "முன்னுரிமையைத் தேர்ந்தெடுக்கவும்",
	"Normal": "சாதாரணம்",
	"Urgent": "அவசரம்",
	"அவசரமானது": "அவசரமானது",
	"Emergency": "அவசரநிலை",
	"அவசரம்": "அவசரம்",
	"Contact Number": "தொடர்பு எண்",
	"தொடர்பு எண்": "தொடர்பு எண்",
	"Recent Blood Requests": "சமீபத்திய இரத்தக் கோரிக்கைகள்",
	"Recent Blood கோரிக்கைகள்": "சமீபத்திய இரத்தக் கோரிக்கைகள்",
	"Blood requests currently available in the system.": "தற்போது இந்த அமைப்பில் உள்ள இரத்தக் கோரிக்கைகள்.",
	"Patient:": "நோயாளி:",
	"Required:": "தேவையான தேதி:",
	"Units Required:": "தேவையான அலகுகள்:",
	"Help": "உதவுங்கள்",
	"Emergency Requests": "அவசர இரத்தக் கோரிக்கைகள்",
	"அவசரம் கோரிக்கைகள்": "அவசர இரத்தக் கோரிக்கைகள்",
	"Emergency blood requirements can be identified quickly.": "அவசர இரத்தத் தேவைகளை விரைவாகக் கண்டறியலாம்.",
	"அவசரம் blood requirements can be identified quickly.": "அவசர இரத்தத் தேவைகளை விரைவாகக் கண்டறியலாம்.",
	"Hospital Details": "மருத்துவமனை விவரங்கள்",
	"Provide accurate hospital and contact information.": "சரியான மருத்துவமனை மற்றும் தொடர்பு விவரங்களை வழங்குங்கள்.",
	"Help Save Lives": "உயிர்களைக் காப்பாற்ற உதவுங்கள்",
	"Every blood request is an opportunity to help someone.": "ஒவ்வொரு இரத்தக் கோரிக்கையும் ஒருவருக்கு உதவும் வாய்ப்பாகும்.",
	"Every request is a chance to save a life.": "ஒவ்வொரு கோரிக்கையும் ஒரு உயிரைக் காப்பாற்றும் வாய்ப்பாகும்.",
	"Your request can help connect a patient with a suitable donor.": "உங்கள் கோரிக்கை ஒரு நோயாளியைப் பொருத்தமான இரத்த தானம் செய்பவருடன் இணைக்க உதவும்."

	
};


function applyLanguage() {
    var language = document.getElementById("language");
    var isTamil = language
        ? language.value === "Tamil"
        : currentLanguage === "Tamil";

    document.documentElement.lang = isTamil ? "ta" : "en";

    var walker = document.createTreeWalker(
        document.body,
        NodeFilter.SHOW_TEXT,
        null,
        false
    );

    var node;
    var original;
    var translated;

    while (walker.nextNode()) {
        node = walker.currentNode;

        if (!node._redPulseOriginalText) {
            original = node.nodeValue;

            if (original.trim() !== "") {
                node._redPulseOriginalText = original;
            }
        }

        original = node._redPulseOriginalText;

        if (!original) {
            continue;
        }

        translated = original;

        if (isTamil) {
            Object.keys(tamilTranslations).forEach(function (key) {
                translated = translated.split(key).join(tamilTranslations[key]);
            });
        }

        node.nodeValue = isTamil
            ? translated
            : original;
    }

    /* Translate placeholders and accessibility labels */
    var elements = document.querySelectorAll(
        "input[placeholder], textarea[placeholder], input[value], button[aria-label], img[alt]"
    );

    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];

        ["placeholder", "value", "aria-label", "alt"].forEach(function (attribute) {
            var value = element.getAttribute(attribute);

            if (!value) {
                return;
            }

            if (!element.hasAttribute("data-original-" + attribute)) {
                element.setAttribute("data-original-" + attribute, value);
            }

            var originalValue = element.getAttribute("data-original-" + attribute);
            var result = originalValue;

            if (isTamil) {
                Object.keys(tamilTranslations).forEach(function (key) {
                    result = result.split(key).join(tamilTranslations[key]);
                });
            }

            element.setAttribute(attribute, result);
        });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    var language = document.getElementById("language");

    if (language) {
        language.addEventListener("change", applyLanguage);
    }
});


function applyTheme() {
    var theme = document.getElementById("theme");
    var isDark = theme
        ? theme.value === "Dark"
        : currentTheme === "Dark";

    document.documentElement.style.colorScheme = isDark ? "dark" : "light";

    if (document.body.classList) {
        document.body.classList.toggle("rp-dark-mode", isDark);
        document.body.classList.toggle("settings-dark-mode", isDark);
    }
}

function changePassword() {
    alert("Password change is not connected yet.");
}

if (document.addEventListener) {
    document.addEventListener("DOMContentLoaded", loadSettings);
} else {
    window.onload = loadSettings;
}

function changePassword() {
    const currentPassword = prompt("Enter your current password:");
    if (currentPassword === null) return;

    const newPassword = prompt("Enter your new password:");
    if (newPassword === null) return;

    const confirmPassword = prompt("Confirm your new password:");
    if (confirmPassword === null) return;

    if (!currentPassword || !newPassword || !confirmPassword) {
        alert("Please fill in all password fields.");
        return;
    }

    if (newPassword !== confirmPassword) {
        alert("The new passwords do not match.");
        return;
    }

    if (newPassword.length < 8) {
        alert("Your new password must contain at least 8 characters.");
        return;
    }

    const form = document.createElement("form");
    form.method = "POST";
    form.action = "ChangePasswordServlet";

    const fields = {
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmPassword: confirmPassword
    };

    Object.entries(fields).forEach(([name, value]) => {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = name;
        input.value = value;
        form.appendChild(input);
    });

    document.body.appendChild(form);
    form.submit();
}


