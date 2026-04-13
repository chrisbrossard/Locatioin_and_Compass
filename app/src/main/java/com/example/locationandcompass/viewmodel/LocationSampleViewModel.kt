package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.AltitudeSample
import com.example.locationandcompass.data.LocationSample
import com.example.locationandcompass.data.LocationSampleDao

class LocationSampleViewModel(private val dao: LocationSampleDao): ViewModel() {
    fun getSample(locationId: Int): LocationSample {
        return dao.findById(locationId)
    }

    fun findByX(x: Float): LocationSample {
        return dao.findByX(x)
    }

    fun setX(sampleId: Int, x: Float) {
        dao.updateX(sampleId, x)
    }
}