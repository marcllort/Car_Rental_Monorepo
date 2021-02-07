package Model

import "time"

type Service struct {
	ServiceId         int
	Origin            string
	Destination       string
	ClientId          int
	DriverId          int
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
