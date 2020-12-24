import {logOut, prepareUI, protectedApiCall, redirectUserAdmin, startUp} from "./api_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("user_table.html")) {
            prepareUI(user);
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                protectedApiCall(idToken);
                redirectUserAdmin();
            }).catch(function (error) {
                console.error(error.data);
                logOut();
            });
        } else {
            logOut();
        }
    });
};

window.logOut = function (event) {
    logOut();
}
