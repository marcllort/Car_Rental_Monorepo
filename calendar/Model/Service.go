package Model

import "time"

type Service struct {
	ServiceId         int
	Origin            string
	Destination       string
	ClientId          int
	DriverId          int
	Description       string
	ServiceDatetime   string
	CalendarEvent     string
	CalendarDatetime  string
	PayedDatetime     string
	BasePrice         float32
	ExtraPrice        float32
	ConfirmedDatetime string
	Passengers        int
	SpecialNeeds      string
}

type DateType time.Time

func (t DateType) String() string {
	return time.Time(t).String()
}
