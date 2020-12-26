
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');


    var calendar = new FullCalendar.Calendar(calendarEl, {
        displayEventTime: false, // don't show the time column in list view

        contentHeight: 'auto',
        // THIS KEY WON'T WORK IN PRODUCTION!!!
        // To make your own Google API key, follow the directions here:
        // http://fullcalendar.io/docs/google_calendar/
        googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE',

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
        select: function(arg) {
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
        eventClick: function(arg) {
            if (confirm('Are you sure you want to delete this event?')) {
                arg.event.remove()
            }
        },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events

        /*eventClick: function(calEvent, jsEvent, view) {
            window.location = "http://www.domain.com?start=" + calEvent.start;

        }*/
        /*loading: function(bool) {
            document.getElementById('loading').style.display =
                bool ? 'block' : 'none';
        }*/

    });

    calendar.render();
});
