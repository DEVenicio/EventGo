package com.venicio.eventgo.data.model


data class EventModel(
    val date: Long,
    val description: String,
    val id: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val people: List<Any>?,
    val price: Double,
    val title: String
)