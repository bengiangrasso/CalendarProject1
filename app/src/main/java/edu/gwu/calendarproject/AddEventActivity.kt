package edu.gwu.calendarproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText


class AddEventActivity : AppCompatActivity()  {
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
        setContentView(R.layout.activity_new_event)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        date = findViewById(R.id.date)
        startTime = findViewById(R.id.startTime)
        endTime = findViewById(R.id.endTime)
        //temperature = findViewById(R.id.temperature)
        //precipitation = findViewById(R.id.precipitation)
        submit = findViewById(R.id.submitButton)
        cancel = findViewById(R.id.cancelButton)
        submit.setOnClickListener {
            val events = mutableListOf<Event>()
            events.add(
                Event(
                    title = title.getText().toString(),
                    description = description.getText().toString(),
                    date = date.getText().toString(),
                    startTime = startTime.getText().toString(),
                    endTime = endTime.getText().toString(),
                    precipitation = "0% Chance of Precipitation",
                    temperature = "70 F"
                )
            )
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        cancel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}