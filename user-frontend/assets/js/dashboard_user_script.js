import {logOut, protectedApiCall, publicApiCall, startUp} from "./api_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("dashboard_user.html")) {
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                publicApiCall();
                protectedApiCall(idToken);
            }).catch(function (error) {
                console.error(error.data);
                logOut();
            });
        }else {
            logOut();
        }
    });
};

window.logOut = function (event) {
    logOut();
}