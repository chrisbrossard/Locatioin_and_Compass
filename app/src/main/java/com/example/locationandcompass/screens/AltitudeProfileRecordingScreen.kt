package com.example.locationandcompass.screens

import android.graphics.Typeface
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.locationandcompass.viewmodel.AltitudeListViewModel
import com.example.locationandcompass.viewmodel.AltitudeRecordingViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionIdViewModel
import com.example.locationandcompass.viewmodel.StepRecordingViewModel
import com.example.locationandcompass.viewmodel.StepSessionIdViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

//@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AltitudeProfileRecordingScreen(
    //altitudes: ArrayDeque<Int>,
    altitudeListViewModel: AltitudeListViewModel,
    altitudeRecordingViewModel: AltitudeRecordingViewModel,
    navController: NavHostController,
    altitudeSessionIdViewModel: AltitudeSessionIdViewModel
) {
    val rowList by altitudeListViewModel.rowList.collectAsState(initial = emptyList())
    //var clickedText by remember { mutableStateOf("Stop Recording") }
    //val altitudeViewModel: AltitudeViewModel = viewModel()
    val sessionId = altitudeSessionIdViewModel.getSessionId()

    BackHandler(enabled = true) {
        altitudeRecordingViewModel.updateRecording(false)
        navController.popBackStack()
    }

    /*Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    altitudeRecordingViewModel.updateRecording(false)
                    clickedText =  if (clickedText == "Stop Recording") "Stop Recording" else
                        "Recording Stopped"
                }
            ) {
            }
        }
    ) { innerPadding ->*/
        Column(
            //modifier = Modifier.padding(innerPadding)
        ) {
            Box(
                Modifier
                    .weight(0.1f)
                    .fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        LineChart(context)
                    },
                    update = { chart ->
                        //if (altitudes.isNotEmpty()) {
                        val entries = ArrayList<Entry>()
                        /*var index = 0f
                        for (value in altitudes) {
                            entries.add(Entry(index, value.toFloat()))
                            index++
                        }*/
                        var flag = false
                        for (sample in rowList) {
                            if (sessionId == sample.sessionId) {
                                flag = true
                                break
                            }
                        }
                        if (flag) {
                            for (sample in rowList) { //samples) {
                                val entry = Entry(
                                    sample.time.toFloat() / (1000 * 60),
                                    sample.altitude.toInt().toFloat()
                                )
                                if (sample.sessionId == sessionId) {
                                    entries.add(entry)
                                }
                            }
                            val dataSet = LineDataSet(entries, "altitudes").apply {
                            }
                            chart.data = LineData(dataSet)
                            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                            dataSet.label = "Altitudes (m)"
                            dataSet.setDrawFilled(true)
                            dataSet.fillColor = 0x00FF00
                            dataSet.fillAlpha = 128
                            dataSet.setDrawCircles(false)
                            dataSet.setDrawValues(false)
                            chart.data = LineData(dataSet)
                            chart.setScaleEnabled(true)
                            val description = Description()
                            description.text = "Altitude Profile"
                            chart.description = description
                            /*chart.zoom(
                            1 / altitudes.size.toFloat(),
                            1f,
                            index,
                            altitudes.last().toFloat(),
                            YAxis.AxisDependency.RIGHT
                        )*/
                            chart.xAxis.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            chart.axisLeft.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            chart.axisRight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            chart.legend.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            chart.description.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            chart.invalidate()
                        }
                    }
                )
            }
            Box(
                Modifier
                    .weight(0.1f)
                    .fillMaxSize(),
            )
        }
    }
//}
