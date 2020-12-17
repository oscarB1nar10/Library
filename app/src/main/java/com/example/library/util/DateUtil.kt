package com.example.library.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    // Date format: "2019-07-23 HH:mm:ss"
    private val genderTimeFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US)

    fun removeTimeFromDateString(sd: String): String{
        return sd.substring(0, sd.indexOf(" "))
    }

    fun convertFirebaseTimestampToStringData(timestamp: Timestamp): String{
        return genderTimeFormat.format(timestamp.toDate())
    }

    fun convertStringDateToFirebaseTimestamp(date: String): Timestamp {
        return Timestamp(genderTimeFormat.parse(date))
    }

    fun getCurrentTimestamp(): String {
        return genderTimeFormat.format(Date())
    }
}

