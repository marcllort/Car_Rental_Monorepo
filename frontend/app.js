
function login () {

    var firebaseConfig = {
        apiKey: "AIzaSyCiCPnNzBZm-er7Hd6cHMdTpYO9KQ7l5OU",
        authDomain: "car-rental-2861f.firebaseapp.com",
        projectId: "car-rental-2861f",
        storageBucket: "car-rental-2861f.appspot.com",
        messagingSenderId: "294401568654",
        appId: "1:294401568654:web:80b9a3185608bf9bddbedf"
    };
    // Initialize Firebase
    firebase.initializeApp(firebaseConfig);
    //Source:::Firebase : Google Authentication(Youtube)
    //Get the Elements

    const GoogleSignIn = document.getElementById("GoogleSignIn");
    //*********************** */
    GoogleSignIn.addEventListener('click', e => {
        provider = new firebase.auth.GoogleAuthProvider();
        // firebase.auth().signInWithRedirect(provider).then(function (result)
        //Error3:After Sign But For Pop-Up Is Working


        firebase.auth().signInWithPopup(provider).then(function (result) {
            // This gives you a Google Access Token. You can use it to access the Google API.
            var token = result.credential.accessToken;
            // The signed-in user info.
            var user = result.user;
            // ...
            console.log(user, token, result);
            console.log("Success ..Google Account Linked");

            //token = useAuth();

            axios.get('http://localhost:8090/public/data', {
                headers: {
                    Authorization: 'Bearer ' + token
                }
            }).then(resp => {

                console.log(resp.data);
            });

        }).catch(function (error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            // The email of the user's account used.
            var email = error.email;
            // The firebase.auth.AuthCredential type that was used.
            var credential = error.credential;
            console.log(error);
            // ...
        });


    });
    //Error1:; The current domain is not authorized for OAuth operations. 
    //This will prevent signInWithPopup, signInWithRedirect, linkWithPopup and linkWithRedirect from working. Add your domain (127.0.0.1) to the OAuth redirect domains list in the Firebase console -> Auth section -> Sign in method tab.
    //Solution::Copy Pasted the Project xampp--Localhost

    //Error2:Error 403: restricted_client(Redirect Or PopUp Both)
    //This app is not yet configured to make OAuth requests. To do that, set up the app’s OAuth consent screen in the Google Cloud Console.
    //Solution: StackOverFlow ::Settings--ProjectSettings--GeneralTab--SupportEmail(For EveryThing it Happens(Done For ))



    //Answer:Console log Message and Authentication Google Mail
};