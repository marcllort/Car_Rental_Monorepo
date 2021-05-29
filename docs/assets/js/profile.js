import {idToken, logOut, setToken, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("profile.html")) {
            prepareUI(user);
            firebase.auth().currentUser.getIdToken(true).then(function (idToken1) {
                setToken(idToken1);
                firebase.auth().currentUser.getIdTokenResult().then((idTokenResult) => {
                    if (idTokenResult.claims.role_super) {
                        var index = document.getElementById("indexNav");
                        var calendar = document.getElementById("calendarNav");
                        var accordeon = document.getElementById("accordionSidebar");
                        index.remove();
                        calendar.remove();
                        accordeon.appendChild("<li class=\"nav-item\"><a class=\"nav-link\" href=\"admin_table.html\"><i class=\"fas fa-table\"></i><span>Table</span></a></li>")
                    }
                })
            }).catch(function (error) {
                console.error(error.data);
            });
            var user = firebase.auth().currentUser;
            getFirebaseUserData(user);


        } else {
            logOut();
        }
    });
};

function getFirebaseUserData(user) {
    var db = firebase.firestore();

    db.collection("users").doc(user.uid).get().then(function (doc) {
        if (doc.exists) {
            console.log("Document data:", doc.data());
            updateUI(user, doc.data())
        } else {
            console.log("No such document!");
        }
    }).catch(function (error) {
        console.log("Error getting document:", error);
    });
}

function updateUI(user, data) {
    var userImage = document.getElementById("userProfileImage");

    user.providerData.forEach(function (data) {
        if (data.photoURL != null) {
            userImage.src = data.photoURL;
        }
    });

    document.getElementById("username-text").value = data.name;
    document.getElementById("email-text").value = data.email;
    document.getElementById("password-text").value = data.password;
    document.getElementById("repassword-text").value = data.password;
    document.getElementById("calendar-text").value = data.calendarURL;
    document.getElementById("signature-text").value = data.emailSign;
    if (data.checked != null) {
        document.getElementById("signature-formCheck").checked = true;
    } else {
        document.getElementById("signature-formCheck").checked = data.checked;
    }
    if (data.phone !== undefined) {
        document.getElementById("phone-text").value = data.phone;
    }
    if (data.city !== undefined) {
        document.getElementById("city-text").value = data.city;
    }
    if (data.country !== undefined) {
        document.getElementById("country-text").value = data.country;
    }
}

function updateUserAPICall(data) {
    var url = 'https://carrentalbarcelona.tk/protected/update-user-firebase';

    /*if (document.getElementById("password-text").value === "") {
        document.getElementById("password-text").value = "null";
    }*/
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
    }).catch(error => {
        swal.fire(
            'Error',
            error.response.data.message,
            'error'
        )
    });
}

window.updateMyProfile = function () {
    var user = firebase.auth().currentUser;
    user.updateProfile({
        displayName: document.getElementById("username-text").value,
        photoURL: "https://example.com/jane-q-user/profile.jpg"
    }).then(function () {
        // Update successful.
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        })

        var password = document.getElementById("password-text").value;
        var repassword = document.getElementById("repassword-text").value;

        if (password !== repassword) {
            Toast.fire({
                icon: 'error',
                title: 'Passwords do not match',
            })
        } else {
            var data = {
                uid: user.uid,
                name: document.getElementById("username-text").value,
                email: document.getElementById("email-text").value,
                password: document.getElementById("password-text").value,
                emailSign: document.getElementById("signature-text").value,
                language: "ENG",
                calendarURL: document.getElementById("calendar-text").value,
            }
            if (document.getElementById("phone-text").value !== "") {
                data['phone'] = document.getElementById("phone-text").value;
            }
            if (document.getElementById("city-text").value !== "") {
                data['city'] = document.getElementById("city-text").value;
            }
            if (document.getElementById("country-text").value !== "") {
                data['country'] = document.getElementById("country-text").value;
            }
            if (!document.getElementById("signature-formCheck").checked) {
                data['checked'] = document.getElementById("signature-formCheck").checked;
            }

            updateUserAPICall(data);

            Toast.fire({
                icon: 'success',
                title: 'Profile successfully updated!',
            })
        }
    }).catch(function (error) {
        console.log(error);
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Something went wrong!',
        })
    });
}

window.logOut = function (event) {
    logOut();
}