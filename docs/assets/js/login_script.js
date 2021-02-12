import {protectedApiCall, startUp} from "./api_script.js";

var credential;

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("login.html")) {
            //redirectUserAdmin();
        }
    });
    loadAuth2()
};

window.loadAuth2 = function () {
    gapi.load('auth2', function () {
        gapi.auth2.init({
            client_id: '294401568654-k5lbeg7mqh9l8mmormfqpq18soihrdfu.apps.googleusercontent.com',
            cookie_policy: 'single_host_origin',
            scope: 'https://www.googleapis.com/auth/calendar'
        });
    });
}

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

    const user = firebase.auth().currentUser;
    var credentialAuth;
    if (user == null) {
        const auth = gapi.auth2.getAuthInstance();
        auth.then(() => {
            auth.grantOfflineAccess({
                'redirect_uri': 'postmessage',
                'prompt': 'consent'
            }).then(offlineAccessExchangeCode => {
                // send offline access exchange code to server ...
                const authResp = auth.currentUser.get().getAuthResponse();
                credentialAuth = authResp;
                const credential = firebase.auth.GoogleAuthProvider.credential(authResp.id_token);
                return firebase.auth().signInWithCredential(credential);
            }).then(user => {
                createUserAPICall(credentialAuth)
                protectedCall();
            });
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
    var url = 'http://localhost:8080/protected/create-user-firebase';

    console.log(credential)
    firebase.auth().onAuthStateChanged(function (user) {
        console.log(user)
        const data = {
            uid: user.uid,
            accessToken: credential.access_token,
            refreshToken: user.refreshToken,
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