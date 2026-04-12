package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.AltitudeSessionDao
import com.example.locationandcompass.data.LocationSessionDao
import kotlinx.coroutines.flow.Flow

class LocationSessionCountViewModel(private val dao: LocationSessionDao) : ViewModel() {
    val rowCount = dao.getRowCount()

    fun getCount(): Flow<Int> {
        return dao.getRowCount()
    }
}