package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//732ebb982c949e6881e3fe8a642c3473


class MainActivity : AppCompatActivity() {


    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchWeatherData("Meerut") // fun call

        SearchCity()


    }

    private fun SearchCity() {
        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName:String){

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterFace::class.java) //here API interface Called

        val response = retrofit.getWeatherData(cityName,"732ebb982c949e6881e3fe8a642c3473", "metric")
        response.enqueue(object : Callback<WeatherApp>{

            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
               val  responseBody = response.body()

                if(response.isSuccessful && responseBody != null){

                    val temperature = responseBody.main.temp.toString()

                    val sunrise = responseBody.sys.sunrise.toLong()

                    val sunset = responseBody.sys.sunset.toLong()

                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"

                    val minTemp = responseBody.main.temp_min

                    val maxTemp = responseBody.main.temp_max




                    binding.date.text = dateName()
                    binding.textView6.text=dayName(System.currentTimeMillis())
                    binding.condition.text = condition
                    binding.textView2.text = condition
                    binding.textView.text = "$cityName"

                    binding.sunset.text = "${timeName(sunset)}"

                    binding.sunrise.text = "${timeName(sunrise)}"

                    binding.textView7.text = "@Max Temp: $maxTemp °C"
                    binding.textView3.text = "@Min Temp: $minTemp °C"

                    binding.temparature.text = "$temperature °C"


                    //Log.d("TAG", "onResponse: $temperature")


                    changeImageAccordingWeather(condition)



                }


            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun changeImageAccordingWeather(conditions:String) {
        when (conditions){
            "Sunny","sunrise","sunset", "Clear", "Good Weather","clear" ->{
                binding.root.setBackgroundResource(R.drawable.suny)
                binding.lottieAnimationView.setAnimation(R.raw.animation_sun)
            }
            "Party clouds", "Clouds","Overcast", "Mist", "Foggy" ->{
                binding.root.setBackgroundResource(R.drawable.cloudscreen)
                binding.lottieAnimationView.setAnimation(R.raw.animation_cloud)
            }
            "Light Rain", "Drizzle","Moderate Rain", "Heavy Rain", "Slow Rain", "Raining"->{
                binding.root.setBackgroundResource(R.drawable.raining)
                binding.lottieAnimationView.setAnimation(R.raw.animation_raining)
            }
            "Snow", "Snows", "ice","Snowfall","fall of snow","chalk","whiteness","white","hoar" ,"hoariness"->{
                binding.root.setBackgroundResource(R.drawable.snows)
                binding.lottieAnimationView.setAnimation(R.raw.animation_snow)
            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.suny)
            binding.lottieAnimationView.setAnimation(R.raw.animation_sun)
            }


        }
        binding.lottieAnimationView.playAnimation()
    }

    fun dateName():String{
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format((Date()))
    }


    fun timeName(timestamp: Long):String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }



    fun dayName(timestamp: Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

}