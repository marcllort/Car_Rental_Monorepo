import {token} from "./dashboard_admin_script.js";

const URL = "http://localhost:8090";

function startUp() {
    const firebaseConfig = {
        apiKey: "AIzaSyCiCPnNzBZm-er7Hd6cHMdTpYO9KQ7l5OU",
        authDomain: "car-rental-2861f.firebaseapp.com",
        projectId: "car-rental-2861f",
        storageBucket: "car-rental-2861f.appspot.com",
        messagingSenderId: "294401568654",
        appId: "1:294401568654:web:80b9a3185608bf9bddbedf"
    };

    // Initialize Firebase
    if (!firebase.apps.length) {
        firebase.initializeApp(firebaseConfig);
    } else {
        firebase.app(); // if already initialized, use that one
    }
}

function publicApiCall() {
    axios.get(URL.concat('/public/data'), {}).then(resp => {
        console.log(resp.data);
    });
}

function protectedApiCall(idToken) {
    axios.get(URL.concat('/protected/data'), {
        headers: {
            Authorization: 'Bearer ' + idToken
        }
    }).then(resp => {
        console.log(resp.data);
        console.log(idToken);
    });
}

function getUser(email) {
    var url = URL.concat('/admin/user') + '?emailOrUid=' + email
    console.log(url);
    return axios.get(url, {
        headers: {
            Authorization: 'Bearer ' + token
        }
    }).then(resp => {
        return resp
    });
}

function logOut() {
    var user = firebase.auth().currentUser;
    if (user == null) {
        console.log("User already logged out")
    } else {
        firebase.auth().signOut();
    }
    window.location = 'login.html'
}

export {URL, startUp, publicApiCall, protectedApiCall, getUser, logOut};