package Model

type EmailRequest struct {
	Flow    string  `json:"flow"`
	UserId  string  `json:"userId"`
	Company string  `json:"company"`
	Price   string  `json:"price"`
	Drivers string  `json:"drivers"`
	Service Service `json:"service"`
}
