package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.AltitudeSampleDao
import com.example.locationandcompass.data.AltitudeSessionDao

class AltitudeDeleteViewModel(
    val sampleDao: AltitudeSampleDao,
    val sessionDao: AltitudeSessionDao) : ViewModel() {

    fun deleteBySessionId(sessionId: Long) {
        sampleDao.deleteBySessionId(sessionId)
        sessionDao.deleteBySessionId(sessionId)
    }
}