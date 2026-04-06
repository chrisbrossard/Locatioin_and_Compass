package com.example.locationandcompass.viewmodel

import androidx.lifecycle.ViewModel
import com.example.locationandcompass.data.StepSessionDao

class StepSessionCountViewModel(private val dao: StepSessionDao) : ViewModel() {
    val rowCount = dao.getRowCount()
}