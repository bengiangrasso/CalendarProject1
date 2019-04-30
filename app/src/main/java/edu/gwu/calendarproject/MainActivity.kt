package edu.gwu.calendarproject

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.widget.DividerItemDecoration
import android.widget.*
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
    private lateinit var option: Spinner
    private lateinit var result: TextView
    private lateinit var optionS: Spinner
    private lateinit var resultS: TextView
    private lateinit var optionE: Spinner
    private lateinit var resultE: TextView
    private lateinit var optionYear: Spinner
    private lateinit var resultYear: TextView
    private lateinit var optionMonth: Spinner
    private lateinit var resultMonth: TextView
    private lateinit var optionDay: Spinner
    private lateinit var resultDay: TextView
    private val weatherManager: WeatherManager = WeatherManager()
    private val events: MutableList<Event> = mutableListOf()
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var startTime: EditText
    private lateinit var endTime: EditText
    private lateinit var submit: Button
    private lateinit var cancel: Button
    private lateinit var submit2: Button
    private lateinit var cancel2: Button
    private lateinit var sTime : String
    private lateinit var eTime : String
    private lateinit var year : String
    private lateinit var month : String
    private lateinit var day : String

    private lateinit var gHome : String
    private lateinit var gAway : String
    private lateinit var gStartTime : String
    private lateinit var gEndTime : String
    private lateinit var gYear : String
    private lateinit var gMonth : String
    private lateinit var gDay : String
    private lateinit var gStadium : String
    private lateinit var gCity : String
    private lateinit var gState : String
    var pos = 0
    private lateinit var options : Array<String?>
    private lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get email from login page
        val email = intent.getStringExtra("email")
        firebaseDatabase = FirebaseDatabase.getInstance()

        // initialize objects from activity_main
        recyclerView = findViewById(R.id.recyclerView)
        add = findViewById(R.id.add)
        edit = findViewById(R.id.edit)
        logout = findViewById(R.id.logout)
        calendarButton = findViewById(R.id.calendarButton)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // welcome dialog
        val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Welcome!")
            dialog.setMessage("Please schedule any new events. Click the settings button to sync this application with your google calendar.")
            dialog.setPositiveButton("Okay"){dialog, which ->
                dialog.dismiss()
            }
            dialog.show()

        // button to add an MLB event through a dialog
        edit.setOnClickListener{
            // initialize objects from activity_new_event
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.activity_new_sport)
            submit2 = dialog.findViewById(R.id.submitButton)
            cancel2 = dialog.findViewById(R.id.cancelButton)
            option = dialog.findViewById(R.id.spinner)
            result = dialog.findViewById(R.id.result)

            // use weather api call
            weatherManager.retrieveGame(
                successCallback = { newEvents ->
                    runOnUiThread {

                        // add event strings into string array

                        options = arrayOfNulls(newEvents.size / 10)
                        var count = 0;
                        for (i in 0..(newEvents.size-1) step 10){
                            gHome = newEvents.get(i)
                            gAway = newEvents.get(i+1)
                            gStartTime = newEvents.get(i+2)
                            gEndTime = newEvents.get(i+3)
                            gCity = newEvents.get(i+4)
                            gState = newEvents.get(i+5)
                            gStadium = newEvents.get(i+6)
                            gYear = newEvents.get(i+7)
                            gMonth = newEvents.get(i+8)
                            gDay = newEvents.get(i+9)

                            var str = "$gHome vs. $gAway. $gStartTime to $gEndTime, $gYear/$gMonth/$gDay"
                            if(i == 0)
                                options[i] = str
                            else
                                options[i-(count*9)] = str
                            count++
                        }

                        // spinner used to display upcoming game information
                        option.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, options)
                        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                result.text = "Please Select a Game."
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                result.text = options.get(position)
                                pos = position
                                gHome = newEvents.get(pos * 10)
                                gAway = newEvents.get(pos * 10 + 1)
                                gStartTime = newEvents.get(pos * 10 + 2)
                                gEndTime = newEvents.get(pos * 10 + 3)
                                gCity = newEvents.get(pos * 10 + 4)
                                gState = newEvents.get(pos * 10 + 5)
                                gStadium = newEvents.get(pos * 10 + 6)
                                gYear = newEvents.get(pos * 10 + 7)
                                gMonth = newEvents.get(pos * 10 + 8)
                                gDay = newEvents.get(pos * 10 + 9)
                                Toast.makeText(this@MainActivity, "$gHome, $gAway, $gStartTime, $gEndTime, $gCity, $gState, $gStadium, $gYear, $gMonth, $gDay", Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                },
                errorCallback = {
                    runOnUiThread{
                        AlertDialog.Builder(this)
                            .setTitle("There was an error displaying games.")
                            .setNegativeButton("Go Back") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            )
            submit2.setOnClickListener {
                val dateFinal = "$gYear/$gMonth/$gDay"

                // convert date string to UNIX time
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                val dateNew = dateFormat.parse(dateFinal)
                var millis = dateNew.time
                millis /= 1000
                weatherManager.retrieveWeather(
                    primaryKey = getString(R.string.key),
                    title = "$gHome vs. $gAway",
                    description = "$gStadium, $gCity, $gState",
                    date = dateFinal,
                    millis = millis,
                    startTime = gStartTime,
                    endTime = gEndTime,
                    successCallback = { newEvents ->
                        runOnUiThread{
                            // remove unwanted chars from email and push events in that object on Firebase
                            var emailNew = email.dropLast(10)
                            val reference = firebaseDatabase.getReference("Users/$emailNew")
                            for (i in 0..newEvents.size-1)
                                reference.push().setValue(newEvents.get(i))
                        }
                    },
                    errorCallback = {
                        runOnUiThread{
                            AlertDialog.Builder(this)
                                .setTitle("There was an error creating the event.")
                                .setNegativeButton("Go Back") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
                )
                dialog.dismiss()
            }

            cancel2.setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }

        // button to add your own event with a dialog
        add.setOnClickListener{
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.activity_new_event)

            // initialize objects from activity_new_event
            title = dialog.findViewById(R.id.title)
            description = dialog.findViewById(R.id.description)
            startTime = dialog.findViewById(R.id.startTime)
            endTime = dialog.findViewById(R.id.endTime)
            submit = dialog.findViewById(R.id.submitButton)
            cancel = dialog.findViewById(R.id.cancelButton)
            optionS = dialog.findViewById(R.id.spinnerS)
            resultS = dialog.findViewById(R.id.resultS)
            optionE = dialog.findViewById(R.id.spinnerE)
            resultE = dialog.findViewById(R.id.resultE)
            optionYear = dialog.findViewById(R.id.spinnerYear)
            resultYear = dialog.findViewById(R.id.resultYear)
            optionMonth = dialog.findViewById(R.id.spinnerMonth)
            resultMonth = dialog.findViewById(R.id.resultMonth)
            optionDay = dialog.findViewById(R.id.spinnerDay)
            resultDay = dialog.findViewById(R.id.resultDay)

            // arrays that hold spinner data
            val yearOptions = arrayOf("2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")
            val monthOptions = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
            val dayOptions = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
            val options = arrayOf("am", "pm")

            // spinner for selecting year
            optionYear.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, yearOptions)
            optionYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    resultYear.text = "2019"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    resultYear.text = yearOptions.get(position)
                    year = yearOptions.get(position)
                }

            }

            // spinner for selecting month
            optionMonth.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, monthOptions)
            optionMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    resultMonth.text = "01"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    resultMonth.text = monthOptions.get(position)
                    month = monthOptions.get(position)
                }

            }

            // spinner for selecting day
            optionDay.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dayOptions)
            optionDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    resultDay.text = "01"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    resultDay.text = dayOptions.get(position)
                    day = dayOptions.get(position)
                }

            }

            // spinner for selecting start time
            optionS.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, options)
            optionS.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    resultS.text = "pm"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    resultS.text = options.get(position)
                    sTime = options.get(position)
                }

            }

            // spinner for selecting end time
            optionE.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, options)
            optionE.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    resultE.text = "pm"
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    resultE.text = options.get(position)
                    eTime = options.get(position)
                }

            }

            // submit data entered in the form
            submit.setOnClickListener {
                var sNum = startTime.getText().toString()
                var eNum = endTime.getText().toString()
                var finalS = "$sNum $sTime"
                var finalE = "$eNum $eTime"
                val dateFinal = "$year/$month/$day"

                // convert date string to UNIX time
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                val dateNew = dateFormat.parse(dateFinal)
                var millis = dateNew.time
                millis /= 1000
                weatherManager.retrieveWeather(
                    primaryKey = getString(R.string.key),
                    title = title.getText().toString(),
                    description = description.getText().toString(),
                    date = dateFinal,
                    millis = millis,
                    startTime = finalS,
                    endTime = finalE,
                    successCallback = { newEvents ->
                        runOnUiThread{
                            // remove unwanted chars from email and push events in that object on Firebase
                            var emailNew = email.dropLast(10)
                            val reference = firebaseDatabase.getReference("Users/$emailNew")
                            for (i in 0..newEvents.size-1)
                                reference.push().setValue(newEvents.get(i))
                        }
                    },
                    errorCallback = {
                        runOnUiThread{
                            AlertDialog.Builder(this)
                                .setTitle("There was an error creating the event.")
                                .setNegativeButton("Go Back") { dialog, _ ->
                                    dialog.dismiss()
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

        // retrieve events from Firebase
        var emailNew = email.dropLast(10)
        val reference = firebaseDatabase.getReference("Users/$emailNew")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to retrieve Firebase! Error: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                events.clear()
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
                    }
                }
                recyclerView.adapter = MainAdapter(events)
                recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            }
        })

        // intent to calendar activity
        calendarButton.setOnClickListener{
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // intent back to the login page
        logout.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // display all events in the recyclerView
        recyclerView.adapter = MainAdapter(events)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

    }
}





