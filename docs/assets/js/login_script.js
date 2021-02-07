import {protectedApiCall, startUp} from "./api_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("login.html")) {
            //redirectUserAdmin();
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
    provider.addScope('https://www.googleapis.com/auth/calendar');
    const user = firebase.auth().currentUser;

    if (user == null) {
        firebase.auth().signInWithPopup(provider).then(function (result) {
            if (result.credential) {
                // This gives you a Google Access Token.
                var token = result.credential.accessToken;
                console.log(token)
                var requestOptions = {
                    method: 'GET',
                    redirect: 'follow'
                };

                fetch("https://www.googleapis.com/calendar/v3/users/me/calendarList?access_token=" + token, requestOptions)
                    .then(response => response.text())
                    .then(result => console.log(result))
                    .catch(error => console.log('error', error));
            }
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