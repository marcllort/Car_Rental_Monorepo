import {protectedApiCall, startUp} from "./api_script.js";

var credential;

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
                credential = result.credential;
            }
            createUserAPICall(credential)
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

function createUserAPICall(credential) {
    var url = 'https://carrentalbarcelona.tk/protected/create-user-firebase';

    firebase.auth().onAuthStateChanged(function (user) {
        console.log(user)
        const data = {
            uid: user.uid,
            accessToken: credential.accessToken,
        }

        firebase.auth().currentUser.getIdToken(true).then(function (idToken) {

            const headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + idToken,
            }

            axios.post(url, data, {
                headers: headers
            }).then(resp => {
                console.log(resp);
            }).catch(error => {
                swalWithBootstrapButtons.fire(
                    'Error',
                    capitalizeFirstLetter(error.response.data.message),
                    'error'
                )
            });
        }).catch(function (error) {
            console.error(error.data);
        });
    });
}

function redirectUserAdmin() {
    firebase.auth().currentUser.getIdTokenResult().then((idTokenResult) => {

        /*if (!window.location.pathname.includes("index.html")) {
            window.location = 'index.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
        }*/
        if (!!idTokenResult.claims.role_super) {
            // Show admin UI.
            if (!window.location.pathname.includes("admin_table.html")) {
                window.location = 'admin_table.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
            }
        } else {
            // Show regular user UI.
            if (!window.location.pathname.includes("user_table.html")) {
                window.location = 'user_table.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
            }
        }
    }).catch((error) => {
        console.log(error);
    });
}