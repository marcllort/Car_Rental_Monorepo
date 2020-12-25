import {logOut, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("index.html")) {
            prepareUI(user);

        } else {
            logOut();
        }
    });
};