package com.dicoding.mydicodingevent.data

import androidx.lifecycle.LiveData
import com.dicoding.mydicodingevent.data.local.entity.EventEntity
import com.dicoding.mydicodingevent.data.local.room.EventDao
import com.dicoding.mydicodingevent.data.retrofit.ApiService
import com.dicoding.mydicodingevent.utils.AppExecutors

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun insertEvent(event: EventEntity) {
        appExecutors.diskIO.execute {
            eventDao.insertEvent(event)
        }
    }
    fun getBookmark(id: String): LiveData<EventEntity?> {
        return eventDao.getFavoriteEventById(id)
    }
    fun deleteEvent(event: EventEntity) {
        appExecutors.diskIO.execute {
            eventDao.deleteEvent(event)
        }
    }
    fun getBookmarkedEvent(): LiveData<List<EventEntity>> {
        return eventDao.getBookmarkedEvent()
    }


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}