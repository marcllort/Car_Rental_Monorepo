package CalendarAPI

import (
	"calendar/Utils"
	"cloud.google.com/go/firestore"
	"errors"
	"fmt"
	"golang.org/x/oauth2/google"
	"google.golang.org/api/calendar/v3"
	"io/ioutil"
	"log"
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

func GetEvents(srv *calendar.Service, startTime string, endTime string, excludeCalendars []string) []*calendar.Event {
	err, _, listRes := getEventList("all", srv)
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}

	var events []*calendar.Event
	for _, v := range listRes.Items {
		if !Utils.Contains(excludeCalendars, v.Id) {
			events = append(events, getDriverEvents(srv, v.Id, startTime, endTime)...)
		}
	}

	return events
}

func GetDriverEventsByEmail(srv *calendar.Service, driver string, startTime string, endTime string) []*calendar.Event {
	err, id, _ := getEventList(driver, srv)
	if err != nil {
		log.Fatalf("%v", err)
	}

	return getDriverEvents(srv, id, startTime, endTime)
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

func GetFreeDrivers(srv *calendar.Service, startTime string, endTime string) {
	err, _, listRes := getEventList("all", srv)
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}

	for _, v := range listRes.Items {
		getDriverEvents(srv, v.Id, startTime, endTime)
	}
}

func GetCalendarClient(client *firestore.Client, uid string) (error, *calendar.Service) {
	b, err := ioutil.ReadFile("calendar/Creds/calendar-api-credentials.json")
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
	return err, srv
}