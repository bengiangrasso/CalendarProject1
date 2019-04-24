package edu.gwu.calendarproject

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView

class MainAdapter constructor(private val metroAlerts: List<Event>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.event_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = metroAlerts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEvent = metroAlerts[position]

        holder.titleTextView.text = currentEvent.title
        holder.descriptionTextView.text = currentEvent.description
        holder.dateTextView.text = currentEvent.date
        holder.startTimeTextView.text = currentEvent.startTime
        holder.endTimeTextView.text = currentEvent.endTime
        holder.precipitationTextView.text = currentEvent.precipitation
        holder.temperatureTextView.text = currentEvent.temperature

    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        val titleTextView: TextView = view.findViewById(R.id.title)

        val descriptionTextView: TextView = view.findViewById(R.id.description)

        val dateTextView: TextView = view.findViewById(R.id.date)

        val startTimeTextView: TextView = view.findViewById(R.id.startTime)

        val endTimeTextView: TextView = view.findViewById(R.id.endTime)

        val precipitationTextView: TextView = view.findViewById(R.id.precipitation)

        val temperatureTextView: TextView = view.findViewById(R.id.temperature)
    }
}