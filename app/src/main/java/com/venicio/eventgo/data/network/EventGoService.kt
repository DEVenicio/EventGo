package com.venicio.eventgo.data.network

import com.venicio.eventgo.data.model.CheckIn
import com.venicio.eventgo.data.model.EventModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventGoService {

    @GET("events")
    fun getAllEvents(): Call<Array<EventModel>>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<EventModel>

    @POST("checkin")
    fun sendDataCheckIn(@Body checkIn: CheckIn): Call<Any>


    companion object {
        const val BASE_URL = "https://5f5a8f24d44d640016169133.mockapi.io/api/"
    }
}