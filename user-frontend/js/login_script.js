import {startUp} from "./api_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("login.html")) {
            firebase.auth().currentUser.getIdTokenResult().then((idTokenResult) => {
                if (!!idTokenResult.claims.role_super) {
                    // Show admin UI.
                    window.location = 'dashboard_admin.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
                } else {
                    // Show regular user UI.
                    window.location = 'dashboard_user.html'
                }
            }).catch((error) => {
                console.log(error);
            });
        }
    });
};

window.emailLogin = function (event) {
    const user = firebase.auth().currentUser;
    if (user == null) {
        firebase.auth()
            .signInWithEmailAndPassword(document.getElementById("email").value, document.getElementById("password").value).then(function (result) {
            // This gives you a Google Access Token. You can use it to access the Google API.
            //var token = result.credential.accessToken;
            // The signed-in user info.
            //var user = result.user;

            firebase.auth().currentUser.getIdToken(/* forceRefresh */ true).then(function (idToken) {
                axios.get('http://localhost:8090/protected/data', {
                    headers: {
                        Authorization: 'Bearer ' + idToken
                    }
                }).then(resp => {
                    console.log(idToken);
                    console.log(resp.data);
                });
            }).catch(function (error) {
                console.error(error.data);
            });
        }).catch(function (error) {
            console.log(error);
        });
    } else {
        alert("Already logged in!");
    }
}

window.googleLogin = function (event) {
    var provider = new firebase.auth.GoogleAuthProvider();

    var user = firebase.auth().currentUser;
    if (user == null) {
        firebase.auth().signInWithPopup(provider).then(function (result) {
            // This gives you a Google Access Token. You can use it to access the Google API.
            var token = result.credential.accessToken;
            // The signed-in user info.
            var user = result.user;
            console.log(result)
            firebase.auth().currentUser.getIdToken(/* forceRefresh */ true).then(function (idToken) {
                axios.get('http://localhost:8090/protected/data', {
                    headers: {
                        Authorization: 'Bearer ' + idToken
                    }
                }).then(resp => {
                    console.log(idToken);
                    console.log(resp.data);
                });
            }).catch(function (error) {
                console.error(error.data);
            });
        }).catch(function (error) {
            console.log(error);
        });

    } else {
        alert("Already logged in!");
    }

};