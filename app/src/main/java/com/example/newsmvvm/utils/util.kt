package com.example.newsmvvm.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(strCurrentDate: String): String {
    var convertedDate = ""
    try {
        if (strCurrentDate.isNotEmpty() && strCurrentDate.contains("T")) {
            val local = Locale("US")
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", local)
            val newDate: Date? = format.parse(strCurrentDate)

            format = SimpleDateFormat("MMM dd, yyyy hh:mm a", local)
            newDate?.let {
                convertedDate = format.format(it)
            }
        } else {
            convertedDate = strCurrentDate
        }
    } catch (e: Exception) {
        e.message?.let {
            Log.e("Message", it)
        }
        convertedDate = strCurrentDate
    }
    return convertedDate
}