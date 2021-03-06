package com.car.rental.legal.model

data class Service(
    val serviceId: Int,
    val origin: String?,
    val destination: String?,
    val clientId: Int,
    val driverId: Int,
    val description: String?,
    val serviceDatetime: String?,
    val calendarEvent: String?,
    val payedDatetime: String?,
    val basePrice: Float?,
    val extraPrice: Float?,
    val confirmedDatetime: String?,
    val passengers: Int,
    val specialNeeds: String?
)

