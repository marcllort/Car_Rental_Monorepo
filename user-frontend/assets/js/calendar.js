import {logOut, protectedApiCall, startUp} from "./api_script.js";
import {prepareUI} from "./ui_script.js";

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        if (user && window.location.pathname.includes("calendar.html")) {
            prepareUI(user);
            firebase.auth().currentUser.getIdToken(true).then(function (idToken) {
                protectedApiCall(idToken);
            }).catch(function (error) {
                console.error(error.data);
                logOut();
            });
        } else {
            logOut();
        }
    });
};

document.addEventListener('DOMContentLoaded', function () {
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

        events: [
            {
                title: 'All Day Event',
                description: 'Christmas eve, hohoho!',
                collision: 'event21',
                availableDrivers: {'me':"Me", 'chop':"Chop", 'lua':"Lua"},
                start: '2020-12-01',
                backgroundColor: '#a20606',
                borderColor: '#a20606'
            }
        ],
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
            /*Open Sweet Alert*/
            calEvent.jsEvent.preventDefault();
            console.log(calEvent.event);

            var htmlContent = '    <h4 class="text-muted card-subtitle mb-2">Start Event</h4>\n' +
                '    <p class="card-text">'+calEvent.event.start+'<br /></p>\n' +
                '    <h4 class="text-muted card-subtitle mb-2">End Event</h4>\n' +
                '    <p class="card-text">'+calEvent.event.end+'<br /></p>\n' +
                '    <h4 class="text-muted card-subtitle mb-2">Description</h4>\n' +
                '    <p class="card-text">' + calEvent.event.extendedProps.description + '</p>\n'

            if (calEvent.event.extendedProps.collision !== undefined) {
                htmlContent += 'Collision with event ' + calEvent.event.extendedProps.collision + '<br>';
                if (calEvent.event.extendedProps.availableDrivers[0] === 'me') {
                    htmlContent += 'You are available to do this event!';
                }
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
                    text: 'You are available for this transfer. Want to assign it to yourself?', input: 'radio',
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
                        confirmButtonText: 'Lovely!'
                    })
                }
            })
        },

        eventDidMount: function (event, element) {
            if (event.event.extendedProps.availableDrivers[0] === 'me') {
                console.log(event);
                event.backgroundColor = '#a20606';
            }
        },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events

    });

    calendar.render();
});

window.logOut = function (event) {
    logOut();
}