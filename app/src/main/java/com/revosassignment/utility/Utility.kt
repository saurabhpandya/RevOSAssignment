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

        fun convertStringToDate(date: String): Date =
            SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault()).parse(date)!!

        fun getMinutesFromMills(millis: Long): Int {
            val seconds = millis / 1000
            val minutes = seconds / 60
            return minutes.toInt()
        }

    }

}