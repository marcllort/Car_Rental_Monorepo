package CalendarAPI

import (
	"cloud.google.com/go/firestore"
	"context"
	firebase "firebase.google.com/go"
	"fmt"
	"golang.org/x/oauth2"
	"google.golang.org/api/option"
	"log"
	"net/http"
	"os"
)

func ConnectFirestore() *firestore.Client {
	ctx := context.Background()
	creds_firestore := os.Getenv("CREDS_FIRESTORE")
	sa := option.WithCredentialsFile(creds_firestore)

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

func getClientToken(client *firestore.Client, uid string, config *oauth2.Config) *http.Client {
	//var authCode string
	ctx := context.Background()

	var token oauth2.Token
	token.RefreshToken = GetRefreshToken(client, uid)

	config.Client(ctx, &token)

	return config.Client(context.Background(), &token)
}
