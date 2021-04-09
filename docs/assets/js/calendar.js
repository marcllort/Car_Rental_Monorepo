import {getCalendar, logOut, setToken, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";

var eventsString;

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("calendar.html")) {
            prepareUI(user);
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                setToken(idToken);
                getCalendar().then((response) => {
                    eventsString = response.data;
                    mapEvents();
                    createCalendar();
                });
            }).catch(function (error) {
                console.error(error.data);
            });
        } else {
            logOut();
        }
    });
};

function mapEvents() {
    eventsString.forEach(function (entry) {

        if (!entry.start.hasOwnProperty("dateTime")) {
            entry.start = entry.start.date
        } else {
            entry.start = entry.start.dateTime;
        }

        if (!entry.end.hasOwnProperty("dateTime")) {
            entry.end = entry.end.date
        } else {
            entry.end = entry.end.dateTime;
        }
        entry.title = entry.summary;
    })
}

function createCalendar() {
    document.getElementById("spinner").hidden = true;
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        displayEventTime: false, // don't show the time column in list view

        contentHeight: 'auto',
        // THIS KEY WON'T WORK IN PRODUCTION!!!
        // To make your own Google API key, follow the directions here:
        // http://fullcalendar.io/docs/google_calendar/
        googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE',
        schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',

        // US Holidays


        events: eventsString,
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },

        navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: function (arg) {

            var title = prompt('Event Title:');
            if (title) {
                calendar.addEvent({
                    title: title,
                    start: arg.start,
                    end: arg.end,
                    allDay: arg.allDay
                })
            }
            calendar.unselect()
        },


        eventClick: function (calEvent, jsEvent, view, resourceObj) {
            calEvent.jsEvent.preventDefault();
            console.log(calEvent);
            var htmlContent = '    <h4 class="text-muted card-subtitle mb-2">Start Event</h4>\n' +
                '    <p class="card-text">' + calEvent.event.start.toLocaleString("es-ES") + '<br /></p>\n' +
                '    <h4 class="text-muted card-subtitle mb-2">End Event</h4>\n' +
                '    <p class="card-text">' + calEvent.event.end.toLocaleString("es-ES") + '<br /></p>\n' +
                '    <h4 class="text-muted card-subtitle mb-2">Description</h4>\n' +
                '    <p class="card-text">' + calEvent.event.extendedProps.description + '</p>\n'

            if (calEvent.event.extendedProps.collision !== undefined) {
                htmlContent += '    <h4 class="text-muted card-subtitle mb-2">Collisions</h4>\n' + 'Collision with event ' + calEvent.event.extendedProps.collision + '<br>';
            }
            var textAvailability;
            if (calEvent.event.extendedProps.availableDrivers['me'] === 'Me') {
                textAvailability = 'You are available for this transfer. Want to assign it to yourself?';
            } else {
                textAvailability = 'You are NOT available for this transfer. Do you still want to assign it to yourself?';
            }

            var inputOptions = new Promise(function (resolve) {
                resolve({
                    'me': 'Yes',
                    'none': 'No'
                });
            });
            Swal.mixin({
                confirmButtonText: 'Next &rarr;',
                showCancelButton: true,
                progressSteps: ['1', '2', '3']
            }).queue([
                {
                    title: calEvent.event.title,
                    html: htmlContent
                },
                {
                    title: 'Available',
                    text: textAvailability,
                    input: 'radio',
                    inputOptions: inputOptions,
                    inputValidator: (value) => {
                        if (!value) {
                            return 'You need to choose something!'
                        }
                    }
                },
                {
                    title: 'Drivers',
                    text: 'Joint service? Assign to someone else? ',
                    input: 'select',
                    inputOptions: calEvent.event.extendedProps.availableDrivers
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
                        confirmButtonText: 'Ok'
                    })
                }
            })
        },

        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
    });

    calendar.render();
}


window.logOut = function (event) {
    logOut();
}