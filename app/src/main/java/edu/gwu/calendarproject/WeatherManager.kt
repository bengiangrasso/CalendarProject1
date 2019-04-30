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
                        val tempHigh = weather.getString("temperatureHigh")
                        val weatherAPI = weather.getString("summary")
                        events.add(
                            Event(
                                title = title,
                                description = description,
                                date = date,
                                startTime = startTime,
                                endTime = endTime,
                                temperature = tempHigh,
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

    fun retrieveGame(
        successCallback: (List<String>) -> Unit,
        errorCallback: (Exception) -> Unit
    ){
        val request = Request.Builder()
            // foggy bottom lat and lon
            .url("http://www.mocky.io/v2/5cc7da603000006500055d8d")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }
            override fun onResponse(call: Call, response: Response) {
                val events = mutableListOf<String>()
                val responseString = response.body()?.string()
                if (response.isSuccessful && responseString != null) {
                    val getData = JSONObject(responseString).getJSONArray("games")
                    for (i in 0 until getData.length()) {
                        val data = getData.getJSONObject(i)
                        val city = data.getString("city")
                        val state = data.getString("state")
                        val stadium = data.getString("stadium")
                        val startTime = data.getString("startTime")
                        val endTime = data.getString("endTime")
                        val year = data.getString("year")
                        val month = data.getString("month")
                        val day = data.getString("day")
                        val home = data.getString("home")
                        val away = data.getString("away")
                        events.add("$home vs. $away. $startTime to $endTime, $year/$month/$day")
                    }
                    successCallback(events)

                } else {
                    errorCallback(Exception("Search weather call failed."))
                }
            }
        })
    }
}