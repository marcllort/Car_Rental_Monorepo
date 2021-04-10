import {
    confirmServiceId,
    getCalendar,
    getEventById,
    getFreeDrivers,
    logOut,
    setToken,
    startUp,
    updateService
} from "./api_script.js";
import {prepareUI} from "./ui_script.js";

var eventsString;

window.onload = function () {
    startUp()
    firebase.auth().onAuthStateChanged(function (user) {
        firebase.auth().currentUser.getIdTokenResult().then((idTokenResult) => {
            if (!idTokenResult.claims.role_super) {
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
            } else {
                Swal.fire('Calendar not available for admins!', '', 'error').then((result) => {
                    window.location = 'admin_table.html'
                });
            }
        });
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

            var id = calEvent.event._def.title.match(/\[(.*?)\]/);

            if (id == null) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'This event does not have an ID, so no info could be found',
                })
            } else {

                getEventById(id[1]).then((response) => {
                    if (response.ConfirmedDatetime != null) {
                        infoService(response);
                    } else {
                        assignService(calEvent, response);
                    }
                });
            }
        },

        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
    });

    calendar.render();
}

function infoService(data) {


    var htmlContent =
        '    <h4 class="text-muted card-subtitle mb-2">Start Event</h4>\n' +
        '    <input type="text" class="card-text" id=data.ServiceDatetime > <br /><br />\n' +
        '    <h4 class="text-muted card-subtitle mb-2">Origin</h4>\n' +
        '    <input type="text" class="card-text" id=data.Origin><br /><br />\n' +
        '    <h4 class="text-muted card-subtitle mb-2">Destination</h4>\n' +
        '    <input type="text" class="card-text" id=data.Destination><br /><br />\n' +
        '    <h4 class="text-muted card-subtitle mb-2">Description</h4>\n' +
        '    <input type="text" class="card-text" id=data.Description><br /> <br />\n' +
        '    <h4 class="text-muted card-subtitle mb-2">Price</h4>\n' +
        '    <input type="text" class="card-text" id=data.BasePrice><br /><br />\n' +
        '    <a href=data.CalendarEvent>Google calendar event</a> '
    Swal.fire({
        title: '<strong>Service information</strong>',
        icon: 'info',
        html: htmlContent,
        showCloseButton: true,
        showCancelButton: true,
        focusConfirm: false,
        confirmButtonText:
            '<i class="fa fa-thumbs-up"></i> Great!',
        confirmButtonAriaLabel: 'Thumbs up, great!',
        cancelButtonText:
            '<i class="fa fa-thumbs-down"></i>',
        cancelButtonAriaLabel: 'Thumbs down'
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            var datetime = document.getElementById("data.ServiceDatetime").value;
            var origin = document.getElementById("data.Origin").value;
            var destination = document.getElementById("data.Destination").value;
            var description = document.getElementById("data.Description").value;
            var price = document.getElementById("data.BasePrice").value;
            if (hasChanged(data)) {
                data.ServiceDatetime = datetime;
                data.Origin = origin;
                data.Destination = destination;
                data.Description = description;
                data.BasePrice = price;
                updateService(data);
                Swal.fire('Saved!', '', 'success')
            } else {
                Swal.fire('No changes!', '', 'info')
            }

        } else if (result.isDenied) {
            Swal.fire('Changes are not saved', '', 'info')
        }
    })
    document.getElementById("data.ServiceDatetime").value = data.ServiceDatetime.toLocaleString("es-ES");
    document.getElementById("data.Origin").value = data.Origin;
    document.getElementById("data.Destination").value = data.Destination;
    document.getElementById("data.Description").value = data.Description;
    document.getElementById("data.BasePrice").value = data.BasePrice;

}

function hasChanged(data) {
    var datetime = document.getElementById("data.ServiceDatetime").value;
    var origin = document.getElementById("data.Origin").value;
    var destination = document.getElementById("data.Destination").value;
    var description = document.getElementById("data.Description").value;
    var price = document.getElementById("data.BasePrice").value;

    if (datetime !== data.ServiceDatetime || origin !== data.Origin || destination !== data.Destination || description !== data.Description || Number(price) !== data.BasePrice) {
        return true;
    } else {
        return false;
    }

}

function assignService(calEvent, data) {
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

    console.log(calEvent.event._def.extendedProps);

    getFreeDrivers(calEvent.event._instance.range.start).then((response) => {
        if (response.DriversNames !== null) {
            if (response.DriversNames[0] === 'futbolsupplier@gmail.com') {
                textAvailability = 'You are available for this transfer. Want to assign it to yourself?';
            } else {
                textAvailability = 'You are NOT available for this transfer. Do you still want to assign it to yourself?';
            }
        } else {
            textAvailability = 'You are NOT available for this transfer. Do you still want to assign it to yourself?';
            response.DriversNames = ["Myself"];
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
                inputOptions: response.DriversNames
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

                if (answers[2] === "me") {
                    data.DriverId = response.DriversIds[0];
                } else {
                    data.DriverId = 2;
                }
                confirmServiceId(data);
            }
        })
    });
}

window.logOut = function (event) {
    logOut();
}