package com.humansuit.foody.utils

import android.util.Log

object Constants {

    const val API_KEY = "723aa747c8b14ae0853f6f96e40567d6"

    fun OnExceptionLog(tag: String, message: String) {
        Log.e(tag, "ExceptionLog: $message")
    }

    fun OnSuccessLog(tag: String, message: String) {
        Log.e(tag, "SuccessLog: $message")
    }

}