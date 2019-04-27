package edu.gwu.calendarproject

import java.io.Serializable

data class Event (
    val title: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val temperature: String,
    val precipitation: String
) : Serializable {
    // Required by Firebase to cast to a custom object
    constructor() : this("","","","", "", "", "")
}

