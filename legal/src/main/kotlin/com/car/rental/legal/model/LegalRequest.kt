package com.car.rental.legal.model

data class LegalRequest(
        val flow: String?,
        val userId: String?,
        val company: String?,
        val price: String?,
        val drivers: String?,
        val service: Service?
)