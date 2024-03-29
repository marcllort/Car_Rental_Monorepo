var idToken;

const URL = "https://carrentalbarcelona.tk";

function setToken(idToken1) {
    idToken = idToken1;
    console.log(idToken);
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

function publicApiCall() {
    axios.get(URL.concat('/public/data'), {}).then(resp => {
        console.log(resp.data);
    });
}

function protectedApiCall() {
    axios.get(URL.concat('/protected/data'), {
        headers: {
            Authorization: 'Bearer ' + idToken
        }
    }).then(resp => {
        console.log(resp.data);
    });
}

function getUser(email) {
    var url = URL.concat('/admin/user') + '?emailOrUid=' + email

    return axios.get(url, {
        headers: {
            Authorization: 'Bearer ' + idToken
        }
    }).then(resp => {
        return resp
    });
}

function getCalendar() {
    var url = 'https://carrentalbarcelona.tk/protected/calendar';
    var data = JSON.stringify({
        "service": {
            "description": "Test descriptionmodified",
            "origin": "BCN Airporttttt",
            "destination": "Girona Airport",
            "serviceId": 4,
            "driverId": 1,
            "extraPrice": 0,
            "serviceDatetime": "2021-02-20T00:00:00Z",
            "payedDatetime": "2020-02-25T00:00:00Z",
            "specialNeeds": "none",
            "passengers": 3,
            "basePrice": 12,
            "calendarEvent": "calendarURL",
            "clientId": 1,
            "confirmedDatetime": null
        },
        "flow": "allEvents"
    });
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    return axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
        return resp
    });
}

function getEventById(input) {
    var eventId = Number(input);

    var url = 'https://carrentalbarcelona.tk/protected/calendar';
    var data = JSON.stringify({
        "service": {
            "description": "Test descriptionmodified",
            "origin": "BCN Airporttttt",
            "destination": "Girona Airport",
            "serviceId": eventId,
            "driverId": 1,
            "extraPrice": 0,
            "serviceDatetime": "2020-02-25T00:00:00Z",
            "payedDatetime": "2020-02-25T00:00:00Z",
            "specialNeeds": "none",
            "passengers": 3,
            "basePrice": 12,
            "calendarEvent": "calendarURL",
            "clientId": 1,
            "confirmedDatetime": null
        },
        "flow": "eventById"
    });
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    return axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
        return resp.data
    });
}

function confirmServiceId(data2) {

    var url = 'https://carrentalbarcelona.tk/protected/calendar';

    var data = JSON.stringify({
        "service": {
            "description": data2.Description,
            "origin": data2.Origin,
            "destination": data2.Destination,
            "serviceId": data2.ServiceId,
            "driverId": data2.DriverId,
            "extraPrice": data2.ExtraPrice,
            "serviceDatetime": data2.ServiceDatetime,
            "payedDatetime": data2.PayedDatetime,
            "specialNeeds": data2.SpecialNeeds,
            "passengers": data2.Passengers,
            "basePrice": data2.BasePrice,
            "calendarEvent": data2.CalendarEvent,
            "clientId": data2.ClientId,
            "confirmedDatetime": data2.ConfirmedDatetime
        },
        "flow": "confirmService"
    });

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }
    console.log(data);
    return axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
        return resp.data
    });
}

function updateService(data2) {

    var url = 'https://carrentalbarcelona.tk/protected/calendar';
    var data = JSON.stringify({
        "service": {
            "description": data2.Description,
            "origin": data2.Origin,
            "destination": data2.Destination,
            "serviceId": data2.ServiceId,
            "driverId": data2.DriverId,
            "extraPrice": data2.ExtraPrice,
            "serviceDatetime": data2.ServiceDatetime,
            "payedDatetime": data2.PayedDatetime,
            "specialNeeds": data2.SpecialNeeds,
            "passengers": data2.Passengers,
            "basePrice": Number(data2.BasePrice),
            "calendarEvent": data2.CalendarEvent,
            "clientId": data2.ClientId,
            "confirmedDatetime": data2.ConfirmedDatetime
        },
        "flow": "modifyService"
    });

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    console.log(data2);
    console.log(data);

    return axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
        return resp.data
    });
}

function getFreeDrivers(input) {
    var date = new Date(input)

    var url = 'https://carrentalbarcelona.tk/protected/calendar';
    var data = JSON.stringify({
        "service": {
            "description": "Test descriptionmodified",
            "origin": "BCN Airporttttt",
            "destination": "Girona Airport",
            "serviceId": 4,
            "driverId": 1,
            "extraPrice": 0,
            "serviceDatetime": date.toISOString(),
            "payedDatetime": "2020-02-25T00:00:00Z",
            "specialNeeds": "none",
            "passengers": 3,
            "basePrice": 12,
            "calendarEvent": "calendarURL",
            "clientId": 1,
            "confirmedDatetime": null
        },
        "flow": "freeDrivers"
    });
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    return axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
        return resp.data
    });
}

function deleteUser(email) {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: false
    })

    swalWithBootstrapButtons.fire({
        title: 'Are you sure you want to delete the user?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            var url = URL.concat('/admin/delete-user') + '?emailOrUid=' + email

            return axios.delete(url, {
                headers: {
                    Authorization: 'Bearer ' + idToken
                }
            }).then(resp => {
                swalWithBootstrapButtons.fire(
                    'Deleted',
                    "User successfully deleted",
                    'success'
                )
                return resp
            }).catch(error => {
                swalWithBootstrapButtons.fire(
                    'Error',
                    capitalizeFirstLetter(error.response.data.message),
                    'error'
                )
            });

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelled',
                'User will not be deleted.',
                'error'
            )
        }
    })

}

function updateUser() {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: false
    })

    swalWithBootstrapButtons.fire({
        title: 'Are you sure you want to save the user?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, save it!',
        cancelButtonText: 'No, cancel!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            var url = URL.concat('/admin/update-user')

            if (document.getElementById("password-text").value === "") {
                document.getElementById("password-text").value = "null";
            }
            const headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + idToken,
            }
            const data = {
                displayName: document.getElementById("name-text").value,
                email: document.getElementById("email-text").value,
                password: document.getElementById("password-text").value,
                customClaim: document.getElementById("claims-select").value,
                disabled: document.getElementById("formCheck-disabled").checked,
                emailVerified: document.getElementById("formCheck-email").checked,
                phoneNumber: document.getElementById("phone-text").value,
                photoURL: document.getElementById("photo-text").value
            }
            axios.post(url, data, {
                headers: headers
            }).then(resp => {
                swalWithBootstrapButtons.fire(
                    'Done',
                    "User successfully updated",
                    'success'
                ).then((result) => {
                    location.reload();
                })
            }).catch(error => {
                swalWithBootstrapButtons.fire(
                    'Error',
                    capitalizeFirstLetter(error.response.data.message),
                    'error'
                )
            });

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelled',
                'User will not be saved.',
                'error'
            )
        }
    })

}

function revokeUser(email) {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: false
    })

    swalWithBootstrapButtons.fire({
        title: 'Are you sure you want to revoke the token?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, revoke!',
        cancelButtonText: 'No, cancel!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            var url = URL.concat('/admin/revoke-token') + '?emailOrUid=' + email

            return axios.get(url, {
                headers: {
                    Authorization: 'Bearer ' + idToken
                }
            }).then(resp => {
                swalWithBootstrapButtons.fire(
                    'Revoked',
                    'User\'s token successfully revoked.',
                    'success'
                )
                return resp
            }).catch(error => {
                swalWithBootstrapButtons.fire(
                    'Error',
                    capitalizeFirstLetter(error.response.data.message),
                    'error'
                )
            });

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelled',
                'User\'s token not revoked.',
                'error'
            )
        }
    })

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

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

export {
    URL,
    setToken,
    startUp,
    publicApiCall,
    protectedApiCall,
    getUser,
    updateUser,
    revokeUser,
    deleteUser,
    logOut,
    getCalendar,
    getFreeDrivers,
    getEventById,
    confirmServiceId,
    updateService,
    idToken
};