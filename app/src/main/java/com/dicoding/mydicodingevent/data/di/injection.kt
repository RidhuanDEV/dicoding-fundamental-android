package com.dicoding.mydicodingevent.data.di

import android.content.Context
import com.dicoding.mydicodingevent.data.EventRepository
import com.dicoding.mydicodingevent.data.local.room.EventDatabase
import com.dicoding.mydicodingevent.data.retrofit.ApiConfig
import com.dicoding.mydicodingevent.utils.AppExecutors


object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}