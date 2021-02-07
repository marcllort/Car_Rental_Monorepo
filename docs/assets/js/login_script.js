import {idToken, protectedApiCall, startUp} from "./api_script.js";

var accessToken;

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("login.html")) {
            console.log(accessToken)
            const data = {
                uid: user.uid,
                accessToken: accessToken,
            }
            createUserAPICall(data)

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
    provider.addScope('https://www.googleapis.com/auth/calendar');
    const user = firebase.auth().currentUser;

    if (user == null) {
        firebase.auth().signInWithPopup(provider).then(function (result) {
            if (result.credential) {
                // This gives you a Google Access Token.
                accessToken = result.credential.accessToken;
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

function createUserAPICall(data) {
    var url = 'https://carrentalbarcelona.tk/protected/create-user-firebase';

    /*if (document.getElementById("password-text").value === "") {
        document.getElementById("password-text").value = "null";
    }*/
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
}