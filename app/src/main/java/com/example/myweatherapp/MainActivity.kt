package com.example.myweatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.viewmodels.WeatherViewmodel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
private val viewmodel:WeatherViewmodel by viewModels()
    var lat:Double = 0.0
    var lon:Double= 0.0
    var REQUEST_CODE =0

private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
              ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
            getweatherdataOfCurrentLocation(lon,lat)
            Log.d("MyWeather2222","$lat and $lon")
        }else{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
                lon = location.latitude
                lat = location.longitude
                getweatherdataOfCurrentLocation(lon,lat)

            }
        }


        serachcity()



    }
    fun dayName(timestamp:Long):String{
        val sdf= SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
    fun date():String{
        val sdf= SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format(Date())
    }

    fun serachcity(){
        binding.serchview.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getweatherdata(query)
                    binding.serchview.setQuery("",false)
                    binding.serchview.clearFocus()

                }else{
                    getweatherdataOfCurrentLocation(lat,lon)
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    fun getweatherdata(cityname:String){
        viewmodel.getweather(cityname)
        viewmodel.response.observe(this,{res->
            binding.humidity.text = res.main.humidity.toString()
            binding.windSpeed.text = res.wind.speed.toString()
            binding.condition.text = res.weather[0].main
            Glide.with(this).load(res.weather[0].icon).into(binding.weatherIcon)
            binding.sunrise.text = res.sys.sunrise.toString()
            binding.sunset.text = res.sys.sunset.toString()
            binding.sealevel.text = res.main.feels_like.toString()

            binding.temp.text = "${res.main.temp.toString()}ºC"
            binding.minTemp.text = "Min:${res.main.temp_min.toString()}ºC"
            binding.maxTemp.text = "Min:${res.main.temp_max.toString()}ºC"
            binding.dscr.text = res.weather[0].description
            binding.cityName.text = res.name
            binding.day.text=dayName(System.currentTimeMillis())
            binding.date.text = date()
            ChangeBackgroundWeatherAccordingtoCondition(res.weather[0].main)
        })

    }    fun getweatherdataOfCurrentLocation(lat:Double?,lon:Double?){
        viewmodel.getweatherResponseOfCurrentLocation(lat,lon)
        viewmodel.response.observe(this,{res->
            binding.humidity.text = res.main.humidity.toString()
            binding.windSpeed.text = res.wind.speed.toString()
            binding.condition.text = res.weather[0].main
            // Glide.with(this).load("https:${res.weather[0].icon}").into(binding.weatherIcon)
            binding.sunrise.text = res.sys.sunrise.toString()
            binding.sunset.text = res.sys.sunset.toString()
            binding.sealevel.text = res.main.feels_like.toString()

            binding.temp.text = "${res.main.temp.toString()}ºC"
            binding.minTemp.text = "Min:${res.main.temp_min.toString()}ºC"
            binding.maxTemp.text = "Min:${res.main.temp_max.toString()}ºC"
            binding.dscr.text = res.weather[0].description
            binding.cityName.text = res.name
            binding.day.text=dayName(System.currentTimeMillis())
            binding.date.text = date()
            ChangeBackgroundWeatherAccordingtoCondition(res.weather[0].main)
        })

    }

    private fun ChangeBackgroundWeatherAccordingtoCondition(condition:String) {
        when(condition){
            "Clear Sky","Sunny","Clear" ->{
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.lottiAnimationView.setAnimation(R.raw.start_weather)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy rain","Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain_icon)
                binding.lottiAnimationView.setAnimation(R.raw.rain)
            }
            "Party Clouds","Clouds","Overcast","Mist","Fog" ->{
                binding.root.setBackgroundResource(R.drawable.cloud)
                binding.lottiAnimationView.setAnimation(R.raw.fog)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow)
                binding.lottiAnimationView.setAnimation(R.raw.snow)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.lottiAnimationView.setAnimation(R.raw.start_weather)
            }

        }
        binding.lottiAnimationView.playAnimation()


    }


}