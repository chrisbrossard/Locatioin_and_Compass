package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.AltitudeSessionDao
import kotlinx.coroutines.flow.Flow

class AltitudeSessionCountViewModel(private val dao: AltitudeSessionDao) : ViewModel() {
    val rowCount = dao.getRowCount()

    fun getCount(): Flow<Int> {
        return dao.getRowCount()
    }
}
