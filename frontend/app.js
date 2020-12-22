function emailLogin() {
    startUp();
    var user = firebase.auth().currentUser;
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
                    window.location = 'mainPage.html'
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

function googleLogin() {

    startUp();

    provider = new firebase.auth.GoogleAuthProvider();

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

//Fer switch, i fer us de https://firebase.google.com/docs/auth/admin/custom-claims
/*
firebase.auth().currentUser.getIdTokenResult()
  .then((idTokenResult) => {
     // Confirm the user is an Admin.
     console.log(idTokenResult.claims)
     if (!!idTokenResult.claims.ROLE_SUPER) {
       // Show admin UI.
       showAdminUI(); // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
     } else {
       // Show regular user UI.
       showRegularUI();
     }
  })
  .catch((error) => {
    console.log(error);
  });
 */
window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("index.html")) {
            window.location = 'mainPage.html'
        }else if(user && window.location.pathname.includes("mainPage.html")){
            firebase.auth().currentUser.getIdToken(/* forceRefresh */ true).then(function (idToken) {
                publicApiCall();
                protectedApiCall(idToken);
            }).catch(function (error) {
                console.error(error.data);
            });
        }
    });
};

function protectedApiCall(idToken){
    axios.get('http://localhost:8090/protected/data', {
        headers: {
            Authorization: 'Bearer ' + idToken
        }
    }).then(resp => {
        console.log(resp.data);
        console.log(idToken);
    });
}

function publicApiCall(){
    axios.get('http://localhost:8090/public/data', {
    }).then(resp => {
        console.log(resp.data);
    });
}

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

function logOut() {
    startUp();
    var user = firebase.auth().currentUser;
    if (user == null) {
        alert("User already logged out")
    } else {
        firebase.auth().signOut();
    }
    window.location = 'index.html'
}
