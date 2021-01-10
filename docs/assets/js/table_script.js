import {getUser, URL} from "./api_script.js";
import {token} from "./dashboard_admin_script.js";

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

function populateTable(numberPage) {
    var length = 0;
    var selectorValue = document.getElementById("selector").value;

    var url = URL.concat('/admin/list-users?maxResults=');
    url = url.concat(selectorValue);
    url = url.concat('&pageNumber=');
    url = url.concat(numberPage);

    return axios.get(url, {
        headers: {
            Authorization: 'Bearer ' + token
        },
    }).then(resp => {
        var i = 1;
        document.getElementById("spinner").hidden=true;
        if (resp.data.length !== 0) {
            deleteTable();
            resp.data.forEach(function (user) {
                insertNewUser(i, user)
                i++;
            });

            length = resp.data.length + 1;
            return length;
        }
        return 0;
    });
}

function clickTable() {
    $(document.body).on("click", "tr", function () {
        popUp(this.cells[1].textContent);
    })
}

function popUp(email) {
    getUser(email).then(resp => {
        document.getElementById("uid-text").value = resp.data.uid;
        document.getElementById("name-text").value = resp.data.displayName;
        document.getElementById("email-text").value = resp.data.email;
        //document.getElementById("password-text").value = undefined;
        document.getElementById("phone-text").value = resp.data.phoneNumber;

        let valueClaim;
        for (var i in resp.data.customClaims) {
            valueClaim = i;
        }
        document.getElementById("claims-select").value = valueClaim;
        document.getElementById("photo-text").value = resp.data.photoUrl;
        document.getElementById("formCheck-disabled").checked = resp.data.disabled;
        document.getElementById("formCheck-email").checked = resp.data.emailVerified;
        document.getElementById("user-form").style.display = "block";
    });
}


function deleteTable() {
    var tableHeaderRowCount = 1;
    var table = document.getElementById('dataTable');
    var rowCount = table.rows.length;
    for (var i = tableHeaderRowCount; i < rowCount; i++) {
        table.deleteRow(tableHeaderRowCount);
    }
}

export {populateTable, deleteTable, clickTable}