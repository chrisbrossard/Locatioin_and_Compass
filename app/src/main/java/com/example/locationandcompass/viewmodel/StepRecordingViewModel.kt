package com.example.locationandcompass.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel

class StepRecordingViewModel(private val application: Application) : ViewModel() {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("my_app", Context.MODE_PRIVATE)
    var recording = sharedPreferences.getBoolean("step_recording", false)

    fun updateRecording(value: Boolean) {
        val sharedPreferences: SharedPreferences =
            application.getSharedPreferences("my_app", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean("step_recording", value)
        }
    }
}