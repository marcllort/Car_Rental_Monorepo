import {listUsers, logOut, protectedApiCall, publicApiCall, startUp} from "./api_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                publicApiCall();
                protectedApiCall(idToken);
                listUsers(idToken);
            }).catch(function (error) {
                console.error(error.data);
            });
        }else {
            logOut();
        }
    });
};

window.logOut = function (event) {
    logOut();
}
