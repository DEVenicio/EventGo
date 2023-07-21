package com.venicio.eventgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venicio.eventgo.data.model.EventModel
import com.venicio.eventgo.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventGoViewModel : ViewModel() {

    private val _eventsAll = MutableLiveData<Array<EventModel>>()
    val eventsAll: LiveData<Array<EventModel>> get() = _eventsAll

    private val _errorRequest = MutableLiveData<Boolean>()
    val errorRequest: LiveData<Boolean> get() = _errorRequest

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> get() = _progressBar

    init {
        getAllEventsData()
    }

    private fun getAllEventsData() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val requestEvents = Repository().fetchAllEvents()
                requestEvents.enqueue(object : Callback<Array<EventModel>> {
                    override fun onResponse(
                        call: Call<Array<EventModel>>,
                        response: Response<Array<EventModel>>
                    ) {

                        if (response.isSuccessful && response.code() == 200) {
                            _eventsAll.postValue(response.body())
                            _progressBar.value = true
                        }
                        if (response.code() >= 400) {
                            _progressBar.value = true
                            _errorRequest.postValue(true)
                        }
                    }

                    override fun onFailure(call: Call<Array<EventModel>>, t: Throwable) {
                        _progressBar.value = true
                        _errorRequest.postValue(true)
                    }
                })
            }
        }catch (e: Exception){
            _errorRequest.postValue(true)
        }
    }

    fun resetError() {
        _errorRequest.value = false
    }

}