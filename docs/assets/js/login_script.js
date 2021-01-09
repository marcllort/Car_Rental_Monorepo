import {protectedApiCall, startUp} from "./api_script.js";
import {redirectUserAdmin} from "./ui_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("login.html")) {
            redirectUserAdmin();
        }
    });
};

window.emailLogin = function () {
    const user = firebase.auth().currentUser;

    if (user == null) {
        firebase.auth()
            .signInWithEmailAndPassword(document.getElementById("email").value,
                document.getElementById("password").value).then(function (result) {
            protectedCall();
        }).catch(function (error) {
            console.log(error);
            alert(error);
        });
    } else {
        alert("Already logged in!");
    }
}

window.googleLogin = function () {
    const provider = new firebase.auth.GoogleAuthProvider();
    const user = firebase.auth().currentUser;

    if (user == null) {
        firebase.auth().signInWithPopup(provider).then(function (result) {
            protectedCall();
        }).catch(function (error) {
            console.log(error);
            alert(error);
        });
    } else {
        alert("Already logged in!");
    }
};

function protectedCall() {
    firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
        protectedApiCall(idToken);
    }).catch(function (error) {
        console.error(error.data);
    });
}