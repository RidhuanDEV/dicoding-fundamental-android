package com.dicoding.mydicodingevent.data.retrofit

import com.dicoding.mydicodingevent.data.response.EventApiResponse
import com.dicoding.mydicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
            @Query("active") active: String
    ) : Call<EventResponse>
    @GET("/events/{id}")
    fun getEventId(
        @Path("id") eventId: String
    ): Call<EventApiResponse>

}