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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var add: ImageButton
    private lateinit var edit: ImageButton
    private lateinit var logout: Button
    private lateinit var calendarButton: ImageButton

    private val weatherManager: WeatherManager = WeatherManager()
    private val events: MutableList<Event> = mutableListOf()

    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var date: EditText
    private lateinit var startTime: EditText
    private lateinit var endTime: EditText
    private lateinit var submit: Button
    private lateinit var cancel: Button
    private var listSize = 0
    private var eventSize = 0
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val email = intent.getStringExtra("email")
        firebaseDatabase = FirebaseDatabase.getInstance()

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

        edit.setOnClickListener{
            eventSize = events.size
            Toast.makeText(this, "Email: $email, Event List Size: $eventSize", Toast.LENGTH_SHORT).show()
        }
        add.setOnClickListener{
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.activity_new_event)

            title = dialog.findViewById(R.id.title)
            description = dialog.findViewById(R.id.description)
            date = dialog.findViewById(R.id.date)
            startTime = dialog.findViewById(R.id.startTime)
            endTime = dialog.findViewById(R.id.endTime)
            submit = dialog.findViewById(R.id.submitButton)
            cancel = dialog.findViewById(R.id.cancelButton)

            submit.setOnClickListener {
                val dateString = date.getText().toString()
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                val dateNew = dateFormat.parse(dateString)
                var millis = dateNew.time
                millis /= 1000
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
                            var emailNew = email.dropLast(10)
                            val reference = firebaseDatabase.getReference("Users/$emailNew")
                            reference.child("$listSize").setValue(newEvents)
                            listSize++
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
            }

            cancel.setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }
        var emailNew = email.dropLast(10)
        val reference = firebaseDatabase.getReference("Users/$emailNew/$listSize")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to retrieve Firebase! Error: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                events.clear()
                Toast.makeText(this@MainActivity, "Im here!", Toast.LENGTH_SHORT).show()
                dataSnapshot.children.forEach { data ->
                    // Does all the JSON parsing to create a Tweet
                    val titleF = data.child("title").getValue(String::class.java)
                    val descriptionF = data.child("description").getValue(String::class.java)
                    val dateF = data.child("date").getValue(String::class.java)
                    val startTimeF = data.child("startTime").getValue(String::class.java)
                    val endTimeF = data.child("endTime").getValue(String::class.java)
                    val temperatureF = data.child("temperature").getValue(String::class.java)
                    val precipitationF = data.child("precipitation").getValue(String::class.java)

                    if (titleF != null && descriptionF != null && dateF != null && startTimeF != null && endTimeF != null && temperatureF != null && precipitationF != null) {
                        events.add(
                            Event(
                                title = titleF,
                                description = descriptionF,
                                date = dateF,
                                startTime = startTimeF,
                                endTime = endTimeF,
                                temperature = temperatureF,
                                precipitation = precipitationF
                            )
                        )
                        Toast.makeText(this@MainActivity, "$titleF", Toast.LENGTH_SHORT).show()
                    } else{
                        if(titleF == null)
                            Toast.makeText(this@MainActivity, "title is null", Toast.LENGTH_SHORT).show()
                        if(descriptionF == null)
                            Toast.makeText(this@MainActivity, "description is null", Toast.LENGTH_SHORT).show()
                        if(dateF == null)
                            Toast.makeText(this@MainActivity, "date is null", Toast.LENGTH_SHORT).show()
                        if(startTimeF == null)
                            Toast.makeText(this@MainActivity, "startTime is null", Toast.LENGTH_SHORT).show()
                        if(endTimeF == null)
                            Toast.makeText(this@MainActivity, "endTime is null", Toast.LENGTH_SHORT).show()
                        //if(temperatureF == null)
                        //Toast.makeText(this@MainActivity, "temperature is null", Toast.LENGTH_SHORT).show()
                        //if(precipitationF == null)
                        // Toast.makeText(this@MainActivity, "precipitation is null", Toast.LENGTH_SHORT).show()

                    }
                    listSize++
                }

            }
        })
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





