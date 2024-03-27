package com.example.myweatherapp.api

import com.example.myweatherapp.Utils
import com.example.myweatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun  getWeatherResponse(
        @Query("q")
        city:String,
        @Query("appid")
        appid:String=Utils.Api_Key,
        @Query("units")
        units:String
    ):Response<WeatherResponse>

    @GET("weather")
    suspend fun  getWeatherResponseOfCurrentLocation(
        @Query("lat")
        lat:Double?,
        @Query("lon")
        lon:Double?,
        @Query("appid")
        appid:String=Utils.Api_Key,
        @Query("units")
        units:String
    ):Response<WeatherResponse>
}