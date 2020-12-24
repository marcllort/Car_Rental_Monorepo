import {logOut, populateTable, protectedApiCall, startUp} from "./api_script.js";

var actualPage = 0;
var token;

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
                token = idToken;
                var selector = document.getElementById("selector");
                populateTable(idToken, selector.value, actualPage).then((lenghtUsers) => {
                    if (lenghtUsers > selector.value) {
                        document.getElementById("nextPage").className = "page-item";
                    }
                });

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

window.previousPage = function (event) {
    if (actualPage > 0) {
        actualPage--;
        populateTable(token, selector.value, actualPage);
        var numPage = actualPage + 1;
        document.getElementById("dataTable_info").textContent = "Showing page " + numPage;
    } else {
        document.getElementById("previousPage").className = "page-item disabled";
    }
}

window.nextPage = function (event) {
    actualPage++;
    var numPage = actualPage + 1;
    populateTable(token, selector.value, actualPage).then((lenghtUsers) => {
        if (lenghtUsers < selector.value) {
            document.getElementById("nextPage").className = "page-item disabled";
        } else {
            document.getElementById("previousPage").className = "page-item";
            document.getElementById("dataTable_info").textContent = "Showing page " + numPage;
        }
    });

}


window.logOut = function (event) {
    logOut();
}
