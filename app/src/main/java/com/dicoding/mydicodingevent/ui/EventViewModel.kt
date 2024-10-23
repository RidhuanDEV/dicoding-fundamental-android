package com.dicoding.mydicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mydicodingevent.data.EventRepository
import com.dicoding.mydicodingevent.data.Result
import com.dicoding.mydicodingevent.data.local.entity.EventEntity
import com.dicoding.mydicodingevent.data.response.EventApiResponse
import com.dicoding.mydicodingevent.data.response.ListEventsItem
import com.dicoding.mydicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _eventDetail = MutableLiveData<Result<ListEventsItem>>()
    val eventDetail: LiveData<Result<ListEventsItem>> = _eventDetail

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun fetchEventDetail(eventId: String) {
        _eventDetail.postValue(Result.Loading)

        val client = ApiConfig.getApiService().getEventId(eventId)
        client.enqueue(object : Callback<EventApiResponse> {
            override fun onResponse(call: Call<EventApiResponse>, response: Response<EventApiResponse>) {
                val event = response.body()?.event
                if (response.isSuccessful && event != null) {
                    _eventDetail.postValue(Result.Success(event))
                } else {
                    _eventDetail.postValue(Result.Error("Failed to get event detail: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<EventApiResponse>, t: Throwable) {
                _eventDetail.postValue(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun getBookmarkedEvent() = eventRepository.getBookmarkedEvent()


    fun getBookmark(id: String): LiveData<EventEntity?> {
        return eventRepository.getBookmark(id)
    }

    fun saveEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.insertEvent(event)
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.deleteEvent(event)
        }
    }



}
