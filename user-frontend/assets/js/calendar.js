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

        events: 'en.usa#holiday@group.v.calendar.google.com',
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


        eventClick: function(calEvent, jsEvent, view, resourceObj) {
            /*Open Sweet Alert*/
            calEvent.jsEvent.preventDefault();


        },


        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events

    });

    calendar.render();
});

window.logOut = function (event) {
    logOut();
}