package com.example.myweatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.models.WeatherResponse
import com.example.myweatherapp.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewmodel @Inject constructor(private val repository: WeatherRepository) :ViewModel() {

    private var _response = MutableLiveData<WeatherResponse>()
    val response:LiveData<WeatherResponse> get() = _response


    fun getweather(city:String)=viewModelScope.launch {
        repository.getweatherResponse(city)?.let {response->
            if (response.isSuccessful){
                _response.postValue(response.body())
            }else{
                Log.d("WeatherActivity","${response.message()}")
            }

        }
    }

    fun getweatherResponseOfCurrentLocation(lat:Double?,lon:Double?)=viewModelScope.launch {
        repository.getweatherResponseOfCurrentLocation(lat,lon)?.let { response ->
            if (response.isSuccessful){
                _response.postValue(response.body())
            }else{
                Log.d("WeatherActivity","${response.message()}")
            }
        }
    }
}