import {listUsers, logOut, protectedApiCall, startUp} from "./api_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
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
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                protectedApiCall(idToken);
                var selector = document.getElementById("selector");
                listUsers(idToken, selector.value);
            }).catch(function (error) {
                console.error(error.data);
            });
        } else {
            logOut();
        }
    });
};

window.searchInTable = function (event) {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("dataTable");
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

window.logOut = function (event) {
    logOut();
}
