package CalendarAPI

import (
	"cloud.google.com/go/firestore"
	"fmt"
	"golang.org/x/oauth2/google"
	"google.golang.org/api/calendar/v3"
	"io/ioutil"
	"log"
	"time"
)

func GetEvents(client *firestore.Client, uid string) {
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
	if err != nil {
		log.Fatalf("Unable to retrieve Calendar client: %v", err)
	}

	listRes, err := srv.CalendarList.List().Fields("items/id").Do()
	if err != nil {
		log.Fatalf("Unable to retrieve list of calendars: %v", err)
	}
	for _, v := range listRes.Items {
		log.Printf("Calendar ID: %v\n", v.Id)
	}

	t := time.Date(2020, 1, 1, 0, 0, 0, 0, time.UTC).Format(time.RFC3339)
	if len(listRes.Items) > 0 {
		id := listRes.Items[3].Id
		res, err := srv.Events.List(id).ShowDeleted(false).
			SingleEvents(true).TimeMin(t).MaxResults(10).OrderBy("startTime").Do()
		if err != nil {
			log.Fatalf("Unable to retrieve calendar events list: %v", err)
		}
		for _, item := range res.Items {
			date := item.Start.DateTime
			if date == "" {
				date = item.Start.Date
			}
			fmt.Printf("%v (%v)\n", item.Summary, date)
		}
		log.Printf("Calendar ID %q Summary: %v Summary: %v\n", id, res.Summary)
		log.Printf("Calendar ID %q next page token: %v\n", id, res.NextPageToken)
	}
}
