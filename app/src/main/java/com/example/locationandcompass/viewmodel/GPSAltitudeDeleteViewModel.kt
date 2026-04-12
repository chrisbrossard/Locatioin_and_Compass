package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeSessionDao
import com.example.locationandcompass.data.GPSAltitudeSampleDao

class GPSAltitudeDeleteViewModel(
    val sampleDao: GPSAltitudeSampleDao,
    val sessionDao: GPSAltitudeSessionDao
) : ViewModel() {

    fun deleteBySessionId(sessionId: Long) {
        sampleDao.deleteBySessionId(sessionId)
        sessionDao.deleteBySessionId(sessionId)
    }
}