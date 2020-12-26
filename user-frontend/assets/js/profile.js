import {logOut, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("profile.html")) {
            prepareUI(user);
            var user = firebase.auth().currentUser;

            document.getElementById("username-text").value = user.displayName;
            document.getElementById("email-text").value = user.email;

        } else {
            logOut();
        }
    });
};

window.updateMyProfile = function () {
    var user = firebase.auth().currentUser;
    user.updateProfile({
        displayName: document.getElementById("username-text").value,
        photoURL: "https://example.com/jane-q-user/profile.jpg"
    }).then(function () {
        // Update successful.
    }).catch(function (error) {
        // An error happened.
    });
}

window.logOut = function (event) {
    logOut();
}