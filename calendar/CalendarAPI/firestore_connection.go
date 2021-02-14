package CalendarAPI

import (
	"cloud.google.com/go/firestore"
	"context"
	"fmt"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"google.golang.org/api/calendar/v3"
	"google.golang.org/api/option"
	"io/ioutil"
	"log"
	"net/http"
	"time"

	firebase "firebase.google.com/go"
)

func ConnectFirestore() *firestore.Client {
	ctx := context.Background()
	sa := option.WithCredentialsFile("calendar/Creds/car-rental.json")

	app, err := firebase.NewApp(ctx, nil, sa)
	if err != nil {
		log.Fatalln(err)
	}

	client, err := app.Firestore(ctx)
	if err != nil {
		log.Fatalln(err)
	}

	return client
}

func GetRefreshToken(client *firestore.Client, uid string) string {
	ctx := context.Background()
	doc, err := client.Collection("users").Doc(uid).Get(ctx)

	if err != nil {
		log.Fatalln(err)
	}

	defer client.Close()

	refreshToken := fmt.Sprintf("%v", doc.Data()["refreshToken"])

	fmt.Println("Refresh Token: ", refreshToken)
	return refreshToken
}

func getClient(client *firestore.Client, uid string, config *oauth2.Config) *http.Client {
	// The file token.json stores the user's access and refresh tokens, and is
	// created automatically when the authorization flow completes for the first
	// time.

	tok := getTokenFromWeb(client, uid, config)

	return config.Client(context.Background(), tok)
}

func getTokenFromWeb(client *firestore.Client, uid string, config *oauth2.Config) *oauth2.Token {
	//var authCode string
	ctx := context.Background()
	//authCode = GetRefreshToken(client, uid)
	var token oauth2.Token
	token.RefreshToken = GetRefreshToken(client, uid)
	config.Client(ctx, &token)
	//tok, err := config.Exchange(context.TODO(), authCode)
	/*if err != nil {
		log.Fatalf("Unable to retrieve token from web: %v", err)
	}*/

	// save token in

	return &token
}

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
	client2 := getClient(client, uid, config)

	srv, err := calendar.New(client2)
	if err != nil {
		log.Fatalf("Unable to retrieve Calendar client: %v", err)
	}

	t := time.Now().Format(time.RFC3339)
	events, err := srv.Events.List("primary").ShowDeleted(false).
		SingleEvents(true).TimeMin(t).MaxResults(10).OrderBy("startTime").Do()
	if err != nil {
		log.Fatalf("Unable to retrieve next ten of the user's events: %v", err)
	}
	fmt.Println("Upcoming events:")
	if len(events.Items) == 0 {
		fmt.Println("No upcoming events found.")
	} else {
		for _, item := range events.Items {
			date := item.Start.DateTime
			if date == "" {
				date = item.Start.Date
			}
			fmt.Printf("%v (%v)\n", item.Summary, date)
		}
	}
}
