package com.car.rental.legal.model

data class Service(
        private val serviceId: Int,
        private val origin: String?,
        private val destination: String?,
        private val clientId: Int,
        private val driverId: Int,
        private val description: String?,
        private val serviceDatetime: String?,
        private val calendarEvent: String?,
        private val payedDatetime: String?,
        private val basePrice: Float?,
        private val extraPrice: Float?,
        private val confirmedDatetime: String?,
        private val passengers: Int,
        private val specialNeeds: String?
)

