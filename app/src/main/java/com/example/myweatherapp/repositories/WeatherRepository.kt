package com.example.myweatherapp.repositories

import com.example.myweatherapp.api.WeatherApi
import javax.inject.Inject

class WeatherRepository  @Inject constructor(private val apiservice:WeatherApi){

    suspend fun getweatherResponse(city:String)= apiservice.getWeatherResponse(city, units = "metric")
    suspend fun getweatherResponseOfCurrentLocation(lat:Double?,lon:Double?) =
        apiservice.getWeatherResponseOfCurrentLocation(lat,lon,units = "metric")
}