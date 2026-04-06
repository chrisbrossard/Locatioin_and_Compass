package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.StepSampleDao
import com.example.locationandcompass.data.StepSessionDao

class StepDeleteViewModel(
    val sampleDao: StepSampleDao,
    val sessionDao: StepSessionDao) : ViewModel() {

    fun deleteBySessionId(sessionId: Long) {
        sampleDao.deleteBySessionId(sessionId)
        sessionDao.deleteBySessionId(sessionId)
    }
}