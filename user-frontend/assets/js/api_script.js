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

function logOut() {
    var user = firebase.auth().currentUser;
    if (user == null) {
        console.log("User already logged out")
    } else {
        firebase.auth().signOut();
    }
    window.location = 'login.html'
}

function listUsers(idToken) {
    axios.get(URL.concat('/admin/list-users'), {
        headers: {
            Authorization: 'Bearer ' + idToken
        }
    }).then(resp => {
        var i = 0;
        resp.data.forEach(function (user) {
            insertNewUser(i, user)
            i++;
        });


    });

}

function insertNewUser(i, resp) {
    var table = document.getElementById("dataTable");
    var row = table.insertRow(1);

    var cell0 = row.insertCell(0);
    var cell1 = row.insertCell(1);
    var cell2 = row.insertCell(2);
    var cell3 = row.insertCell(3);
    var cell4 = row.insertCell(4);
    var cell5 = row.insertCell(5);

    var provider, phone;
    if (resp.providerData.providerId === "password"){
        provider="Email/Password"
    }else {
        provider="Google"
    }

    cell0.innerHTML = "<img class=\"rounded-circle mr-2\" width=\"30\" height=\"30\" src=\"assets/img/avatars/avatar5.jpeg\">"+resp.displayName
    cell1.innerHTML = resp.email;
    cell2.innerHTML = resp.phoneNumber;
    cell3.innerHTML = !resp.disabled.val;
    cell4.innerHTML = new Date(resp.userMetadata.lastSignInTimestamp).toLocaleDateString("es-ES");
    cell5.innerHTML = provider;
}

export {startUp, publicApiCall, protectedApiCall, logOut, listUsers};