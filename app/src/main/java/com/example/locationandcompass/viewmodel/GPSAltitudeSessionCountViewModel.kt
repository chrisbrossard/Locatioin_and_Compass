package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeSessionDao
import kotlinx.coroutines.flow.Flow

class GPSAltitudeSessionCountViewModel(private val dao: GPSAltitudeSessionDao) : ViewModel() {
    val rowCount = dao.getRowCount()

    fun getCount(): Flow<Int> {
        return dao.getRowCount()
    }
}