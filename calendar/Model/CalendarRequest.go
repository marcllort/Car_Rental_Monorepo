package Model

type CalendarRequest struct {
	Flow    string
	UserId  string
	Service Service
	Client  ClientUser
}
