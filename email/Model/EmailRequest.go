package Model

type EmailRequest struct {
	Flow    string
	UserId  string
	Company string
	Price   string
	Drivers string
	Service Service
}
