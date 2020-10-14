package com.revosassignment.utility

import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object {
        fun getCurrentDate(): String {
            val myCalendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US)
            val timeStamp: String = sdf.format(myCalendar.time)
            return timeStamp
        }
    }

}