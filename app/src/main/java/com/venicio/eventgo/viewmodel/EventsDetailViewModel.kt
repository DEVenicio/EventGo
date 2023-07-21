package com.venicio.eventgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venicio.eventgo.data.model.CheckIn
import com.venicio.eventgo.data.model.EventModel
import com.venicio.eventgo.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsDetailViewModel : ViewModel() {

    private val _eventsDetailData = MutableLiveData<EventModel>()
    val eventDetailData: LiveData<EventModel> get() = _eventsDetailData

    private val _checkInSuccess = MutableLiveData<Boolean>()
    val checkInSuccess: LiveData<Boolean> get() = _checkInSuccess

    private val _errorCheckIn = MutableLiveData<Boolean>()
    val errorCheckIn: LiveData<Boolean> get() = _errorCheckIn

    fun getDetailEvent(id: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val requestDetail = Repository().fetchEventDetail(id)
                requestDetail.enqueue(object : Callback<EventModel> {
                    override fun onResponse(
                        call: Call<EventModel>,
                        response: Response<EventModel>
                    ) {
                        if (response.isSuccessful && response.code() == 200) {
                            _eventsDetailData.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<EventModel>, t: Throwable) {
                        //TODO Fazer o tratramento em caso de falha
                    }
                })
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun sendDataCheckIn(checkInData: CheckIn) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val requestCheckIn = Repository().sendDataCheckIn(checkInData)
                requestCheckIn.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful && response.code() == 201) {
                            _checkInSuccess.postValue(true)
                        } else {
                            if (!response.isSuccessful) {
                                _errorCheckIn.postValue(true)
                            }
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        _errorCheckIn.postValue(true)
                    }
                })
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun resetCheckInSuccess() {
        _checkInSuccess.value = false
    }

}