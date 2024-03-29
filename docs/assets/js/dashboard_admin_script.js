import {deleteUser, logOut, protectedApiCall, revokeUser, setToken, startUp, updateUser} from "./api_script.js";
import {clickTable, populateTable} from "./table_script.js";
import {prepareUI, redirectUserAdmin} from "./ui_script.js";

var actualPage = 0;
var token;

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("admin_table.html")) {
            prepareUI(user);
            clickTable();
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                redirectUserAdmin();
                setToken(idToken);
                token = idToken;
                var selector = document.getElementById("selector");
                populateTable(actualPage).then((lengthUsers) => {
                    if (lengthUsers > selector.value) {
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

window.populateTable = function (page) {
    populateTable(page);
}

window.searchInTable = function () {
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

window.previousPage = function () {
    if (actualPage > 0) {
        actualPage--;
        populateTable(token, selector.value, actualPage);
        var numPage = actualPage + 1;
        document.getElementById("dataTable_info").textContent = "Showing page " + numPage;
    } else {
        document.getElementById("previousPage").className = "page-item disabled";
    }
}

window.nextPage = function () {
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

window.savePopUp = function () {
    console.log("updating");
    updateUser();
    document.getElementById("user-form").style.display = "none";
}

window.revokePopUp = function () {
    console.log("revoking");
    var email = document.getElementById("email-text").value;
    revokeUser(email);
    document.getElementById("user-form").style.display = "none";
}

window.deletePopUp = function () {
    //potser cal treure el type button
    console.log("deleting");
    var email = document.getElementById("email-text").value;
    deleteUser(email);
}

window.closePopUp = function () {
    document.getElementById("user-form").style.display = "none";
}

window.logOut = function () {
    logOut();
}

export {token}