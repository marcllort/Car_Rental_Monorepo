import {idToken, logOut, setToken, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";


window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        firebase.auth().currentUser.getIdTokenResult().then((idTokenResult) => {
            if (!idTokenResult.claims.role_super) {
                firebase.auth().onAuthStateChanged(function (user) {
                    if (user && window.location.pathname.includes("index.html")) {
                        prepareUI(user);
                        firebase.auth().currentUser.getIdToken(true).then(function (idToken1) {
                            setToken(idToken1);
                            summaryAPICall();
                            checkUserInfoExists(user);
                        }).catch(function (error) {
                            console.error(error.data);
                        });

                    } else {
                        logOut();
                    }
                });
            } else {
                window.location = 'admin_table.html'
            }
        });
    });
};

function checkUserInfoExists(user) {
    var db = firebase.firestore();
    db.collection("users").doc(user.uid).get().then(snapshot => {
        const data = snapshot.data()  // a plain JS object
        console.log(data);
        if (data === undefined) {
            createUserFirebaseAPI(user);
        }
    }).catch(error => {
        console.log(error);
    });
}

function createUserFirebaseAPI(user) {

    Swal.mixin({
        confirmButtonText: 'Next &rarr;',
        showCancelButton: true,
        progressSteps: ['1', '2', '3', '4', '5', '6']
    }).queue([
        {
            title: 'Set-up',
            text: 'Welcome! We need some information to finish the setup. Please introduce your name',
            input: 'text',
            customClass: {
                validationMessage: 'This field is required'
            },
            preConfirm: (value) => {
                if (!value) {
                    Swal.showValidationMessage(
                        'This field is required'
                    )
                }
            }
        },
        {
            title: 'Set-up',
            text: 'Now we need your email',
            input: 'text',
            customClass: {
                validationMessage: 'This field is required'
            },
            preConfirm: (value) => {
                if (!value) {
                    Swal.showValidationMessage(
                        'This field is required'
                    )
                }
                if (!validateEmail(value)) {
                    Swal.showValidationMessage(
                        'This email is not correct'
                    )
                }
            }
        },
        {
            title: 'Set-up',
            text: 'To be able to send emails from your own domain, we also need your password email',
            input: 'text',
            customClass: {
                validationMessage: 'This field is required'
            },
            preConfirm: (value) => {
                if (!value) {
                    Swal.showValidationMessage(
                        'This field is required'
                    )
                }
            }
        },
        {
            title: 'Set-up',
            text: 'What is the sign that you usually use on your emails? (i.e Sent from Marc iphone...)',
            input: 'text',
            customClass: {
                validationMessage: 'This field is required'
            },
            preConfirm: (value) => {
                if (!value) {
                    Swal.showValidationMessage(
                        'This field is required'
                    )
                }
            }
        },
        {
            title: 'Set-up',
            text: 'Preferred language? ',
            input: 'select',
            inputOptions: {
                'ENG': 'English',
                'ES': 'Spanish',
                'CAT': 'Catalan'
            }
        },
        {
            title: 'Set-up',
            text: 'And the last step, we need your Google calendar URL',
            input: 'text',
            customClass: {
                validationMessage: 'This field is required'
            },
            preConfirm: (value) => {
                if (!value) {
                    Swal.showValidationMessage(
                        'This field is required'
                    )
                }
            }
        },

    ]).then((result) => {
        if (result.value) {
            const answers = JSON.stringify(result.value)
            Swal.fire({
                title: 'All done!',
                html: `
                                Your answers:
                                <pre><code>${answers}</code></pre>
                              `,
                confirmButtonText: 'Confirm'
            })
            console.log(result.value[0]);
            const data = {
                uid: user.uid,
                name: result.value[0],
                email: result.value[1],
                password: result.value[2],
                emailSign: result.value[3],
                language: result.value[4],
                calendarURL: result.value[5]
            }
            createUserAPICall(data)
            updateUserAPICall(data)
        }
    })

}

function validateEmail(email) {
    var re = /\S+@\S+\.\S+/;
    return re.test(email);
}

function updateUserAPICall(data) {
    var url = 'https://carrentalbarcelona.tk/protected/update-user-firebase';

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }
    console.log(data)
    axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
    }).catch(error => {
        swal.fire(
            'Error',
            error.response.data.message,
            'error'
        )
    });
}

function createUserAPICall(data) {
    var url = 'https://carrentalbarcelona.tk/protected/create-user-firebase';

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
    }).catch(error => {
        swal.fire(
            'Error',
            error.response.data.message,
            'error'
        )
    });
}

function summaryAPICall() {
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
        "flow": "summary"
    });
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    axios.post(url, data, {
        headers: headers
    }).then(resp => {
        document.getElementById("monthly_earnings").textContent = resp.data.ActualMonthIncome + '€';
        document.getElementById("anual_earnings").textContent = resp.data.AnualIncome + '€';
        document.getElementById("unconfirmed_events").textContent = resp.data.UnconfirmedEvents

        var ctx = document.getElementById('myChart').getContext('2d');
        var delayed;
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                    data: [resp.data.MonthlyIncome[0], resp.data.MonthlyIncome[1], resp.data.MonthlyIncome[2], resp.data.MonthlyIncome[3], resp.data.MonthlyIncome[4], resp.data.MonthlyIncome[5], resp.data.MonthlyIncome[6], resp.data.MonthlyIncome[7], resp.data.MonthlyIncome[8], resp.data.MonthlyIncome[9], resp.data.MonthlyIncome[10], resp.data.MonthlyIncome[11]],
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: false,
                    tooltip: true,
                },
                animation: {
                    onComplete: () => {
                        delayed = true;
                    },
                    delay: (context) => {
                        let delay = 0;
                        if (context.type === 'data' && context.mode === 'default' && !delayed) {
                            delay = context.dataIndex * 300 + context.datasetIndex * 100;
                        }
                        return delay;
                    },
                }
            }
        });


    }).catch(error => {
        console.log(error)
    });
}

window.logOut = function (event) {
    logOut();
}