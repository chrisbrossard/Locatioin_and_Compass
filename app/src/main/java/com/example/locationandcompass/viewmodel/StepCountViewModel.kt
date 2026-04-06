package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.StepSampleDao

class StepCountViewModel(private val dao: StepSampleDao) : ViewModel() {
    val rowCount = dao.getRowCount()
}