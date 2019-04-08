package edu.gwu.calendarproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ImageButton
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private var choice = ""
    private val choices = arrayOf("Indoor Activity", "Outdoor Activity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Welcome!")
            dialog.setMessage("Please schedule any new events. Click the settings button to sync this application with your google calendar.")
            dialog.setPositiveButton("Okay"){dialog, which ->
                dialog.dismiss()
            }
            dialog.show()
        val add = findViewById<ImageButton>(R.id.add)
        add.setOnClickListener{
            val addDialog = AlertDialog.Builder(this@MainActivity)
            addDialog.setTitle("Add an Event")
            addDialog.setSingleChoiceItems(choices, -1){ _, which  ->
                choice = choices[which]
            }
            addDialog.setNegativeButton("Cancel"){addDialog, which ->
                addDialog.dismiss()
            }
            addDialog.setPositiveButton("Okay"){addDialog, which ->

            }

            addDialog.create()
            addDialog.show()
        }

            //val input = new EditText(this);
    }

}





