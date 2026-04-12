package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationandcompass.data.LocationSampleDao
import com.example.locationandcompass.data.StepSampleDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class LocationListViewModel(private val dao: LocationSampleDao) : ViewModel() {
    val rowList = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}