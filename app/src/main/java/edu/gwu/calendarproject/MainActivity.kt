package edu.gwu.calendarproject

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



class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val events = mutableListOf<Event>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        generateEvents()
        val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Welcome!")
            dialog.setMessage("Please schedule any new events. Click the settings button to sync this application with your google calendar.")
            dialog.setPositiveButton("Okay"){dialog, which ->
                dialog.dismiss()
            }
            dialog.show()
        val add = findViewById<ImageButton>(R.id.add)
        add.setOnClickListener{
            val intent = Intent(this, AddEventActivity::class.java)
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
                startTime = "12:00 pm",
                endTime = "3:00 pm",
                precipitation = "0% Chance of Precipitation",
                temperature = "75 F"
            )
        )
        events.add(
            Event(
                title = "Baseball Game",
                description = "Nationals vs. Yankees",
                date = "April 15, 2019",
                startTime = "12:00 pm",
                endTime = "3:00 pm",
                precipitation = "0% Chance of Precipitation",
                temperature = "75 F"
            )
        )
        events.add(
            Event(
                title = "Baseball Game",
                description = "Nationals vs. Yankees",
                date = "April 15, 2019",
                startTime = "12:00 pm",
                endTime = "3:00 pm",
                precipitation = "0% Chance of Precipitation",
                temperature = "75 F"
            )
        )
        events.add(
            Event(
                title = "Baseball Game",
                description = "Nationals vs. Yankees",
                date = "April 15, 2019",
                startTime = "12:00 pm",
                endTime = "3:00 pm",
                precipitation = "0% Chance of Precipitation",
                temperature = "75 F"
            )
        )
        events.add(
            Event(
                title = "Baseball Game",
                description = "Nationals vs. Yankees",
                date = "April 15, 2019",
                startTime = "12:00 pm",
                endTime = "3:00 pm",
                precipitation = "0% Chance of Precipitation",
                temperature = "75 F"
            )
        )
        events.add(
            Event(
                title = "Baseball Game",
                description = "Nationals vs. Yankees",
                date = "April 15, 2019",
                startTime = "12:00 pm",
                endTime = "3:00 pm",
                precipitation = "0% Chance of Precipitation",
                temperature = "75 F"
            )
        )

    }

}





