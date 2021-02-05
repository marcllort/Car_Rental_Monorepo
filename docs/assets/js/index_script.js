import {idToken, logOut, setToken, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";


window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("index.html")) {
            prepareUI(user);
            firebase.auth().currentUser.getIdToken(true).then(function (idToken1) {
                setToken(idToken1);
            }).catch(function (error) {
                console.error(error.data);
            });
            checkUserInfoExists(user);
        } else {
            logOut();
        }
    });
};

function checkUserInfoExists(user) {
    var db = firebase.firestore();
    db.collection("users").doc(user.uid).get().then(snapshot => {
        const data = snapshot.data()  // a plain JS object
        if (data == undefined) {
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
        }
    })

}

function validateEmail(email) {
    var re = /\S+@\S+\.\S+/;
    return re.test(email);
}

function createUserAPICall(data) {
    var url = 'https://carrentalbarcelona.tk/protected/create-user-firebase';

    /*if (document.getElementById("password-text").value === "") {
        document.getElementById("password-text").value = "null";
    }*/
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + idToken,
    }

    axios.post(url, data, {
        headers: headers
    }).then(resp => {
        console.log(resp);
    }).catch(error => {
        swalWithBootstrapButtons.fire(
            'Error',
            capitalizeFirstLetter(error.response.data.message),
            'error'
        )
    });
}

window.logOut = function (event) {
    logOut();
}