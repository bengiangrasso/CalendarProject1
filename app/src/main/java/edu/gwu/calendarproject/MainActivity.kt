package edu.gwu.calendarproject

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText
import android.support.v7.widget.DividerItemDecoration
import android.widget.Button
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val events = mutableListOf<Event>()
    private lateinit var add: ImageButton
    private lateinit var edit: ImageButton
    private lateinit var logout: Button
    private lateinit var calendarButton: ImageButton
    private val weatherManager: WeatherManager = WeatherManager()
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var date: EditText
    private lateinit var startTime: EditText
    private lateinit var endTime: EditText
    private lateinit var temperature: EditText
    private lateinit var precipitation: EditText
    private lateinit var submit: Button
    private lateinit var cancel: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        add = findViewById(R.id.add)
        edit = findViewById(R.id.edit)
        logout = findViewById(R.id.logout)
        calendarButton = findViewById(R.id.calendarButton)
        recyclerView.layoutManager = LinearLayoutManager(this)
        generateEvents()
        val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Welcome!")
            dialog.setMessage("Please schedule any new events. Click the settings button to sync this application with your google calendar.")
            dialog.setPositiveButton("Okay"){dialog, which ->
                dialog.dismiss()
            }
            dialog.show()


        add.setOnClickListener{
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.activity_new_event)
            title = dialog.findViewById(R.id.title)
            description = dialog.findViewById(R.id.description)
            date = dialog.findViewById(R.id.date)
            startTime = dialog.findViewById(R.id.startTime)
            endTime = dialog.findViewById(R.id.endTime)
            //temperature = findViewById(R.id.temperature)
            //precipitation = findViewById(R.id.precipitation)
            submit = dialog.findViewById(R.id.submitButton)
            cancel = dialog.findViewById(R.id.cancelButton)
            submit.setOnClickListener {
                val dateString = date.getText().toString()
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                val dateNew = dateFormat.parse(dateString)
                var millis = dateNew.time
                millis /= 1000
                Toast.makeText(this@MainActivity, "$millis", Toast.LENGTH_SHORT).show()
                weatherManager.retrieveWeather(
                    primaryKey = getString(R.string.key),
                    title = title.getText().toString(),
                    description = description.getText().toString(),
                    date = date.getText().toString(),
                    millis = millis,
                    startTime = startTime.getText().toString(),
                    endTime = endTime.getText().toString(),
                    successCallback = { newEvents ->
                        runOnUiThread{
                            Toast.makeText(this@MainActivity, "It worked!", Toast.LENGTH_SHORT).show()
                            events.addAll(newEvents)
                            recyclerView.adapter = MainAdapter(events)
                            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
                        }
                    },
                    errorCallback = {
                        runOnUiThread{
                            AlertDialog.Builder(this)
                                .setTitle("There was an error creating the event.")
                                .setNegativeButton("Go Back") { dialog, _ ->
                                    finish()
                                }
                                .show()
                        }
                    }
                )
                dialog.dismiss()
                //val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
            }

            cancel.setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }
        calendarButton.setOnClickListener{
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        logout.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = MainAdapter(events)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            //val input = new EditText(this);

    }

    private fun generateEvents(){
        events.add(
            Event(
                title = "Baseball Game",
                description = "Nationals vs. Yankees",
                date = "April 15, 2019",
                startTime = "12:00",
                endTime = "15:00",
                precipitation = "None",
                temperature = "75"
            )
        )

    }

}





