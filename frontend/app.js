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

    //Error1:; The current domain is not authorized for OAuth operations.
    //This will prevent signInWithPopup, signInWithRedirect, linkWithPopup and linkWithRedirect from working. Add your domain (127.0.0.1) to the OAuth redirect domains list in the Firebase console -> Auth section -> Sign in method tab.
    //Solution::Copy Pasted the Project xampp--Localhost

    //Error2:Error 403: restricted_client(Redirect Or PopUp Both)
    //This app is not yet configured to make OAuth requests. To do that, set up the app’s OAuth consent screen in the Google Cloud Console.
    //Solution: StackOverFlow ::Settings--ProjectSettings--GeneralTab--SupportEmail(For EveryThing it Happens(Done For ))


    //Answer:Console log Message and Authentication Google Mail
};

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        console.log(window.location.pathname);
        if (user && window.location.pathname.includes("index.html")) {
            window.location = 'mainPage.html'
        }
    });
};

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
