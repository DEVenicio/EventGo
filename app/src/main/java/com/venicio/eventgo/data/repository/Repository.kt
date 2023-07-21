package com.venicio.eventgo.data.repository

import com.venicio.eventgo.data.model.CheckIn
import com.venicio.eventgo.data.network.EventGoClient
import com.venicio.eventgo.data.network.EventGoService

class Repository {

    private val service = EventGoClient.getInstanceRetrofit(EventGoService.BASE_URL)

    fun fetchAllEvents() = service.getAllEvents()

    fun fetchEventDetail(id: String) = service.getDetailEvent(id)

    fun sendDataCheckIn(dataCheckIn: CheckIn) = service.sendDataCheckIn(dataCheckIn)

}