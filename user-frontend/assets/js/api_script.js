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

function deleteTable() {
    var tableHeaderRowCount = 1;
    var table = document.getElementById('dataTable');
    var rowCount = table.rows.length;
    for (var i = tableHeaderRowCount; i < rowCount; i++) {
        table.deleteRow(tableHeaderRowCount);
    }
}

function populateTable(idToken, numberResults, numberPage) {
    var length = 0;
    var url = URL.concat('/admin/list-users?maxResults=');
    url = url.concat(numberResults);
    url = url.concat('&pageNumber=');
    url = url.concat(numberPage);
    console.log(url);

    return axios.get(url, {
        headers: {
            Authorization: 'Bearer ' + idToken
        },
    }).then(resp => {
        var i = 1;

        if (resp.data.length !== 0) {
            deleteTable();
            resp.data.forEach(function (user) {
                console.log(user);
                insertNewUser(i, user)
                i++;
            });

            length = resp.data.length + 1;
            return length;
        }
        return 0;
    });
}

function insertNewUser(i, resp) {
    var table = document.getElementById("dataTable");
    var row = table.insertRow(i);

    var cell0 = row.insertCell(0);
    var cell1 = row.insertCell(1);
    var cell2 = row.insertCell(2);
    var cell3 = row.insertCell(3);
    var cell4 = row.insertCell(4);
    var cell5 = row.insertCell(5);

    var provider = "";
    var user = "";
    var photo;
    photo = "assets/img/avatars/avatar5.jpeg"

    resp.providerData.forEach(function (data) {
        if (data.photoUrl != null) {
            photo = data.photoUrl;
        }
        if (data.providerId === "password") {
            provider = provider + "Email/Password "
        } else if (data.providerId === "google.com") {
            provider = provider + "Google "
        }
        if (data.providerId === "google.com") {
            user = data.displayName;
        } else if (data.providerId === "password") {
            user = data.displayName;
        }
    });


    cell0.innerHTML = "<img class=\"rounded-circle mr-2\" width=\"30\" height=\"30\" src=" + photo + ">" + user
    cell1.innerHTML = resp.email;
    cell2.innerHTML = resp.phoneNumber;
    cell3.innerHTML = !resp.disabled;
    cell4.innerHTML = new Date(resp.userMetadata.lastSignInTimestamp).toLocaleDateString("es-ES");
    cell5.innerHTML = provider;
}

export {startUp, publicApiCall, protectedApiCall, logOut, populateTable, deleteTable};