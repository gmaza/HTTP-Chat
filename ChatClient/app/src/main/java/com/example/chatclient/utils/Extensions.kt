package com.example.chatclient.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.lastMessageDate():String {
    val tmp = this // 'this' corresponds to the list

    val lastMsgDate = Date(this)

    val currentDate = Date()

    val diff: Long = currentDate.getTime() - lastMsgDate.getTime()
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    if (hours<24) {
        if (hours<1)  {
            if (minutes < 2) return "1 min"
            return (minutes.toString()+ " min")
        }
        return  (hours.toString()+ " hour")

    }

    val pattern = "dd/MM/yyyy"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return (simpleDateFormat.format(lastMsgDate))





}