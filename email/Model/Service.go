package Model

import "time"

type Service struct {
	ServiceId         int        `json:"serviceId"`
	Origin            string     `json:"origin"`
	Destination       string     `json:"destination"`
	ClientId          int        `json:"clientId"`
	DriverId          int        `json:"driverId"`
	Description       string     `json:"description"`
	ServiceDatetime   *time.Time `json:"serviceDatetime"`
	CalendarEvent     string     `json:"calendarEvent"`
	CalendarDatetime  *time.Time `json:"calendarDatetime"`
	PayedDatetime     *time.Time `json:"payedDatetime"`
	BasePrice         float32    `json:"basePrice"`
	ExtraPrice        float32    `json:"extraPrice"`
	ConfirmedDatetime *time.Time `json:"confirmedDatetime"`
	Passengers        int        `json:"passengers"`
	SpecialNeeds      string     `json:"specialNeeds"`
}

type ServiceView struct {
	ServiceId         int
	Origin            string
	Destination       string
	DriverId          int
	DriverName        string
	DriverPhone       string
	DriverMail        string
	DriverCountry     string
	ClientId          int
	ClientName        string
	ClientPhone       string
	ClientMail        string
	ClientCountry     string
	Description       string
	ServiceDatetime   *time.Time
	CalendarEvent     string
	CalendarDatetime  *time.Time
	PayedDatetime     *time.Time
	BasePrice         float32
	ExtraPrice        float32
	ConfirmedDatetime *time.Time
	Passengers        int
	SpecialNeeds      string
}

type DateType time.Time

func (t DateType) String() string {
	return time.Time(t).String()
}
