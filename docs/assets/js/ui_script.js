function prepareUI(user) {
    var userNavBar = document.getElementById("userNavBar");
    var userNavBarImage = document.getElementById("userNavBarImage");

    user.providerData.forEach(function (data) {
        if (data.photoURL != null) {
            userNavBarImage.src = data.photoURL;
        }
        if (data.providerId === "google.com") {
            userNavBar.innerText = data.displayName;
        } else if (data.providerId === "password") {
            userNavBar.innerText = data.displayName;
        }
    });
}

function redirectUserAdmin() {
    firebase.auth().currentUser.getIdTokenResult().then((idTokenResult) => {

        if (!window.location.pathname.includes("index.html")) {
            window.location = 'index.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
        }
        /*if (!!idTokenResult.claims.role_super) {
            // Show admin UI.
            if (!window.location.pathname.includes("admin_table.html")) {
                window.location = 'admin_table.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
            }
        } else {
            // Show regular user UI.
            if (!window.location.pathname.includes("user_table.html")) {
                window.location = 'user_table.html' // fer que es puguin fer totes les funcions de firebase.admin AbstractFirebaseAuth.java
            }
        }*/
    }).catch((error) => {
        console.log(error);
    });
}

export {prepareUI, redirectUserAdmin}