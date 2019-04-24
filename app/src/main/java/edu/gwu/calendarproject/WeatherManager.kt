package edu.gwu.calendarproject

import android.location.Address
import android.widget.Toast
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class WeatherManager {
    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }

    fun retrieveWeather(
        primaryKey: String,
        title: String,
        description: String,
        date: String,
        millis: Long,
        startTime: String,
        endTime: String,
        successCallback: (List<Event>) -> Unit,
        errorCallback: (Exception) -> Unit
    ){
        val request = Request.Builder()
            // foggy bottom lat and lon
            .url("https://api.darksky.net/forecast/$primaryKey/38.9007,-77.0518,$millis")
            .header("api_key", primaryKey)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }
            override fun onResponse(call: Call, response: Response) {
                val events = mutableListOf<Event>()
                val responseString = response.body()?.string()
                if (response.isSuccessful && responseString != null) {
                    val getWeather = JSONObject(responseString).getJSONObject("daily").getJSONArray("data")
                    for (i in 0 until getWeather.length()) {
                        val weather = getWeather.getJSONObject(i)
                        val tempAPI = weather.getString("temperatureHigh")
                        val weatherAPI = weather.getString("precipType")
                        events.add(
                            Event(
                                title = title,
                                description = description,
                                date = date,
                                startTime = startTime,
                                endTime = endTime,
                                temperature = tempAPI,
                                precipitation = weatherAPI
                            )
                        )
                    }
                    successCallback(events)

                } else {
                    errorCallback(Exception("Search weather call failed."))
                }
            }
        })
    }
}