package CalendarAPI

import (
	"calendar/Utils"
	"errors"
	"fmt"
	"golang.org/x/oauth2/google"
	"google.golang.org/api/calendar/v3"
	"io/ioutil"
	"log"
	"os"
	"strings"
	"time"
)

func getEventList(calendarName string, srv *calendar.Service) (error, string, *calendar.CalendarList) {
	listRes, err := srv.CalendarList.List().Fields("items/id").Do()
	if calendarName == "all" {
		return err, "", listRes
	}
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}
	for _, v := range listRes.Items {
		if calendarName == v.Id {
			return nil, v.Id, listRes
		}
		//log.Printf("Calendar ID: %v\n", v.Id)
	}
	return errors.New("Non-existing calendar string"), "", listRes
}

func getDriverEvents(srv *calendar.Service, id string, startTime string, endTime string) []*calendar.Event {

	res, err := srv.Events.List(id).ShowDeleted(false).
		SingleEvents(true).TimeMin(startTime).TimeMax(endTime).MaxResults(100).OrderBy("startTime").Do()
	if err != nil {
		log.Fatalf("Unable to retrieve calendar events list: %v", err)
	}

	for _, item := range res.Items {
		date := item.Start.DateTime
		if date == "" {
			date = item.Start.Date
		}
		fmt.Printf("%v (%v) Desc: %v\n", item.Summary, date, item.Description)
	}
	//log.Printf("Calendar ID %q Summary: %v Summary: %v\n", id, res.Summary)

	return res.Items
}

func GetEvents(srv *calendar.Service, startTime string, endTime string, excludeCalendars []string) ([]*calendar.Event, []string) {
	err, _, listRes := getEventList("all", srv)
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}

	var events []*calendar.Event
	var eventsCalendar []string
	for _, v := range listRes.Items {
		if !Utils.Contains(excludeCalendars, v.Id) {
			events = append(events, getDriverEvents(srv, v.Id, startTime, endTime)...)
			eventsCalendar = append(eventsCalendar, v.Id)
		}
	}

	return events, eventsCalendar
}

func GetDriverEventsByEmail(srv *calendar.Service, driver string, startTime string, endTime string) []*calendar.Event {
	err, id, _ := getEventList(driver, srv)
	if err != nil {
		log.Fatalf("%v", err)
	}

	return getDriverEvents(srv, id, startTime, endTime)
}

func GetFreeDrivers(srv *calendar.Service, startTime *time.Time, duration time.Duration, excludeCalendars []string) []string {
	err, _, listRes := getEventList("all", srv)
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}

	var freeDrivers []string

	for _, v := range listRes.Items {
		if !Utils.Contains(excludeCalendars, v.Id) {
			services := getDriverEvents(srv, v.Id, startTime.Format(time.RFC3339), startTime.Add(duration).Format(time.RFC3339))
			if len(services) == 0 {
				freeDrivers = append(freeDrivers, v.Id)
			}
		}
	}
	fmt.Printf("%v\n", freeDrivers)
	return freeDrivers
}

func CreateCalendarEvent(srv *calendar.Service, summary string, location string, description string, driver string,
	startTime *time.Time, duration time.Duration) string {

	event := &calendar.Event{
		Summary:     summary,
		Location:    location,
		Description: description,
		Start: &calendar.EventDateTime{
			DateTime: startTime.Format(time.RFC3339),
			TimeZone: "Europe/Madrid",
		},
		End: &calendar.EventDateTime{
			DateTime: startTime.Add(duration).Format(time.RFC3339),
			TimeZone: "Europe/Madrid",
		},
		Attendees: []*calendar.EventAttendee{
			{Email: driver},
		},
	}

	calendarId := "primary"
	event, err := srv.Events.Insert(calendarId, event).Do()
	if err != nil {
		log.Fatalf("Unable to create event. %v\n", err)
	}
	fmt.Printf("Event created: %s\n", event.HtmlLink)

	return event.HtmlLink
}

func UpdateCalendarEvent(srv *calendar.Service, eventId string, summary string, location string, description string,
	driver string, startTime *time.Time, duration time.Duration, excludeCalendars []string) {

	durationParse, _ := time.ParseDuration("1h30m")
	events, eventsCalendar := GetEvents(srv, startTime.Format(time.RFC3339), startTime.Add(durationParse).Format(time.RFC3339), excludeCalendars)

	var event *calendar.Event
	var calendarId string
	for i, v := range events {
		if strings.Contains(v.Summary, "["+eventId+"]") {
			event = v
			calendarId = eventsCalendar[i]
			event.Summary = summary
			event.Location = location
			event.Description = description
			event.Attendees = []*calendar.EventAttendee{
				{Email: driver},
			}
			event.Start = &calendar.EventDateTime{
				DateTime: startTime.Format(time.RFC3339),
				TimeZone: "Europe/Madrid",
			}
			event.End = &calendar.EventDateTime{
				DateTime: startTime.Add(duration).Format(time.RFC3339),
				TimeZone: "Europe/Madrid",
			}

			srv.Events.Update(calendarId, event.Id, event).Do() //Update with Id
		}
	}
}

func GetDriversEmail(srv *calendar.Service, excludeCalendars []string) []string {
	err, _, listRes := getEventList("all", srv)
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}

	var emails []string

	for _, v := range listRes.Items {
		if !Utils.Contains(excludeCalendars, v.Id) {
			emails = append(emails, v.Id)
		}
	}

	return emails
}

func GetCalendarClient(uid string) *calendar.Service {
	client := ConnectFirestore()
	creds_calendar := os.Getenv("CREDS_CALENDAR")

	b, err := ioutil.ReadFile(creds_calendar)
	if err != nil {
		log.Fatalf("Unable to read client secret file: %v", err)
	}

	// If modifying these scopes, delete your previously saved token.json.
	config, err := google.ConfigFromJSON(b, calendar.CalendarReadonlyScope)
	if err != nil {
		log.Fatalf("Unable to parse client secret file to config: %v", err)
	}
	client2 := getClientToken(client, uid, config)

	srv, err := calendar.New(client2)
	return srv
}
