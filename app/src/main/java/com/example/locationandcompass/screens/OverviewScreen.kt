package com.example.locationandcompass.screens

import android.hardware.SensorManager
import android.hardware.SensorManager.PRESSURE_STANDARD_ATMOSPHERE
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.locationandcompass.MainActivity
import com.example.locationandcompass.data.AltitudeSampleDao
import com.example.locationandcompass.data.AltitudeSession
import com.example.locationandcompass.data.AltitudeSessionDao
import com.example.locationandcompass.data.StepSampleDao
import com.example.locationandcompass.data.StepSession
import com.example.locationandcompass.data.StepSessionDao
import com.example.locationandcompass.requestCurrentLocation
import com.example.locationandcompass.viewmodel.AltitudeDeleteViewModel
import com.example.locationandcompass.viewmodel.AltitudeListViewModel
import com.example.locationandcompass.viewmodel.AltitudeRecordingViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionCountViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionIdViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionListViewModel
import com.example.locationandcompass.viewmodel.DistanceViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeViewModel
import com.example.locationandcompass.viewmodel.HeadingViewModel
import com.example.locationandcompass.viewmodel.PressureViewModel
import com.example.locationandcompass.viewmodel.StepRecordingViewModel
import com.example.locationandcompass.viewmodel.StepSessionCountViewModel
import com.example.locationandcompass.viewmodel.StepSessionIdViewModel
import com.example.locationandcompass.viewmodel.StepSessionListViewModel
import com.example.locationandcompass.viewmodel.StepCountViewModel
import com.example.locationandcompass.viewmodel.StepDeleteViewModel
import com.example.locationandcompass.viewmodel.StepListViewModel
import com.example.locationandcompass.viewmodel.StepViewModel
import com.example.locationandcompass.viewmodel.VerticalSpeedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dev.jamesyox.kastro.sol.calculateSolarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Suppress("UNUSED_PARAMETER")
@Composable
fun OverviewScreen(
    client: FusedLocationProviderClient,
    navController: NavHostController,
    //altitudeSlope: Double,
    //pressure: Float,
    //azimuth: Float,
    magnetometerAccuracy: Int,
    //altitudes: ArrayDeque<Int>,
    //stepsDeque: ArrayDeque<Long>,
    //stepsSpeed: Float,
    altitudeSampleDao: AltitudeSampleDao,
    stepSampleDao: StepSampleDao,
    stepSessionDao: StepSessionDao,
    altitudeSessionDao: AltitudeSessionDao,
    //steps: Int,
    stepCountViewModel: StepCountViewModel,
    stepListViewMode: StepListViewModel,
    stepSessionCountViewModel: StepSessionCountViewModel,
    stepSessionListViewModel: StepSessionListViewModel,
    stepSessionIdViewModel: StepSessionIdViewModel,
    stepRecordingViewModel: StepRecordingViewModel,
    stepDeleteViewModel: StepDeleteViewModel,
    altitudeListViewModel: AltitudeListViewModel,
    altitudeSessionCountViewModel: AltitudeSessionCountViewModel,
    altitudeSessionListViewModel: AltitudeSessionListViewModel,
    altitudeSessionIdViewModel: AltitudeSessionIdViewModel,
    altitudeRecordingViewModel: AltitudeRecordingViewModel,
    altitudeDeleteViewModel: AltitudeDeleteViewModel,
    onNavigateToAltitudeRecording: () -> Unit,
    headingViewModel: HeadingViewModel,
    stepViewModel: StepViewModel,
    verticalSpeedViewModel: VerticalSpeedViewModel,
    pressureViewModel: PressureViewModel,
    distanceViewModel: DistanceViewModel,
    gPSAltitudeViewModel: GPSAltitudeViewModel
) {
    var sunMoonOctant by remember { mutableStateOf("-") }
    var compassOctant by remember { mutableStateOf("-") }
    var location1 by remember { mutableStateOf(Location("")) }
    //val stepRowCount by stepCountViewModel.rowCount.collectAsState(initial = 0)
    val stepSessionRowCount by stepSessionCountViewModel.rowCount.collectAsState(initial = 0)
    val altitudeSessionRowCount by altitudeSessionCountViewModel.rowCount.collectAsState(initial = 0)
    val stepSessionList by stepSessionListViewModel.rowList.collectAsState(initial = emptyList())
    val altitudeSessionList by altitudeSessionListViewModel.rowList.collectAsState(initial = emptyList())
    //var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    //val stepSessionId by remember { mutableLongStateOf(stepSessionIdViewModel.stepSessionId) }
    //val altitudeSessionId by remember { mutableLongStateOf(altitudeSessionIdViewModel.getSessionId()) }
    //var stepRecording by remember { mutableStateOf(stepRecordingViewModel.recording) }
    //var altitudeRecording by remember { mutableStateOf(altitudeRecordingViewModel.recording) }
    //val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val heading by headingViewModel.heading.collectAsState()
    val vmSteps by stepViewModel.steps.collectAsState()
    val verticalSpeed by verticalSpeedViewModel
        .verticalSpeed.collectAsState()
    val vmPressure by pressureViewModel.pressure.collectAsState()
    val distance by distanceViewModel.distance.collectAsState()
    val gPSAltitude by gPSAltitudeViewModel.altitude.collectAsState()

    //Log.d("Location and Compass", "altitude sessions: " + altitudeSessionCountViewModel.getCount())
    LaunchedEffect(Unit) {
        requestCurrentLocation(
            client
        ) { location ->
            location1 = location
        }
        //scope.launch() {
        sheetState.partialExpand()
        //}
    }

    /*val stepCount = stepSessionRowCount
    val altitudeCount = altitudeSessionRowCount
    if (stepCount != 0) {
        showBottomSheet = true
    }
    if (altitudeCount != 0) {
        showBottomSheet = true
    }*/

    //showBottomSheet = true

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 40.dp,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle()
        },
        sheetContent = {
            Column {
                if (stepSessionRowCount != 0) {
                    Text(
                        text = "Step Profiles",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                LazyColumn {
                    items(
                        items = stepSessionList,
                        key = { it.sessionId }
                    ) { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            initialValue = SwipeToDismissBoxValue.Settled,
                            positionalThreshold = { totalDistance ->
                                totalDistance * 0.75f
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromEndToStart = false,
                            backgroundContent = {
                            },
                            onDismiss = {
                                val serviceScope =
                                    CoroutineScope(SupervisorJob() + Dispatchers.IO)
                                serviceScope.launch {
                                    try {
                                        stepSampleDao.deleteBySessionId(item.sessionId)
                                        stepSessionDao.deleteBySessionId(item.sessionId)
                                    } catch (e: Exception) {
                                        Log.e(
                                            "Location and Compass",
                                            "step delete failed",
                                            e
                                        )
                                    }
                                }
                            }
                        ) {
                            val formatted = java.time.Instant.ofEpochMilli(item.startTime)
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("MMM d, h.mm a"))
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        stepSessionIdViewModel.setSessionId(item.sessionId)
                                        navController.navigate("steps_profile_viewing")
                                    }
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    //modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(formatted)
                                }
                                /*Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Button(
                                        onClick = {
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }*/
                            }
                        }
                    }
                }
                if (altitudeSessionRowCount != 0) {
                    Text(
                        text = "Altitude Profiles",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                LazyColumn {
                    items(
                        items = altitudeSessionList,
                        key = { it.sessionId }
                    ) { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            initialValue = SwipeToDismissBoxValue.Settled,
                            positionalThreshold = { totalDistance ->
                                totalDistance * 0.75f
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromEndToStart = false,
                            backgroundContent = {
                            },
                            onDismiss = {
                                val serviceScope =
                                    CoroutineScope(SupervisorJob() + Dispatchers.IO)
                                serviceScope.launch {
                                    try {
                                        altitudeSampleDao.deleteBySessionId(item.sessionId)
                                        altitudeSessionDao.deleteBySessionId(item.sessionId)
                                    } catch (e: Exception) {
                                        Log.e(
                                            "Location and Compass",
                                            "altitude delete failed",
                                            e
                                        )
                                    }
                                }
                            }
                        ) {
                            val formatted = java.time.Instant.ofEpochMilli(item.startTime)
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("MMM d, h.mm a"))
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        altitudeSessionIdViewModel.setSessionId(item.sessionId)
                                        navController.navigate("altitude_profile_viewing")
                                    }
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    //modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(formatted)
                                }
                                /*Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Button(
                                        onClick = {
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }*/
                            }
                        }
                    }
                }
                /*LazyColumn {
                    items(altitudeSessionList) { item ->
                        val formatted = java.time.Instant.ofEpochMilli(item.startTime)
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("MMM d, h.mm a"))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    altitudeSessionIdViewModel.setSessionId(item.sessionId)
                                    navController.navigate("altitude_profile_viewing")
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = formatted,
                                )
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Button(
                                    onClick = {
                                        val serviceScope =
                                            CoroutineScope(SupervisorJob() + Dispatchers.IO)
                                        serviceScope.launch {
                                            try {
                                                altitudeSampleDao.deleteBySessionId(item.sessionId)
                                                altitudeSessionDao.deleteBySessionId(item.sessionId)
                                            } catch (e: Exception) {
                                                Log.e("Location and Compass", "altitude delete failed", e)
                                            }
                                        }
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }*/
            }
        }
        // main screen
    ) { innerPadding ->
        Column {
            val grey = Color(250, 250, 250)
            // first row
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .background(grey),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    // steps
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(grey)
                            .fillMaxSize()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BasicText(
                                modifier = Modifier
                                    .clickable {
                                        val serviceScope =
                                            CoroutineScope(SupervisorJob() + Dispatchers.IO)
                                        serviceScope.launch {
                                            try {
                                                val sessionId = stepSessionDao.insert(
                                                    StepSession(
                                                        startTime = System.currentTimeMillis(),
                                                        endTime = 0L
                                                    )
                                                )
                                                stepSessionIdViewModel.setSessionId(sessionId)
                                            } catch (e: Exception) {
                                                Log.e("Location and Compass", "insert failed", e)
                                            }
                                        }
                                        stepRecordingViewModel.updateRecording(true)
                                        navController.navigate("steps_profile_recording")
                                    },
                                text = vmSteps.toInt().toString(),
                                maxLines = 1,
                                autoSize = TextAutoSize.StepBased(),
                            )
                            Text("Steps")
                            //Text("Tap to Record")
                        }
                    }
                    // vertical speed
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(grey)
                            .fillMaxSize()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val s = String.format(
                            Locale.US,
                            "%.0f",
                            //altitudeSlope * 1000 * 60
                            verticalSpeed * 1000 * 60
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BasicText(
                                modifier = Modifier.clickable {
                                    navController.navigate("vertical_speed")
                                },
                                text = s,
                                maxLines = 1,
                                autoSize = TextAutoSize.StepBased()
                            )
                            Text("Vertical Speed m/hr")
                        }
                    }
                }
            }
            // second row
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .background(grey)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    // Altitude
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(grey)
                            .fillMaxSize()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val a = SensorManager.getAltitude(
                            PRESSURE_STANDARD_ATMOSPHERE,
                            vmPressure
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BasicText(
                                modifier = Modifier.clickable {
                                    val serviceScope =
                                        CoroutineScope(SupervisorJob() + Dispatchers.IO)
                                    serviceScope.launch {
                                        try {
                                            val id = altitudeSessionDao.insert(
                                                AltitudeSession(
                                                    //sessionId = altitudeSessionId,
                                                    startTime = System.currentTimeMillis(),
                                                    endTime = 0L
                                                )
                                            )
                                            altitudeSessionIdViewModel.setSessionId(id)
                                        } catch (e: Exception) {
                                            Log.e(
                                                "Location and Compass",
                                                "altitude session insert failed",
                                                e
                                            )
                                        }
                                    }
                                    onNavigateToAltitudeRecording()
                                    //navController.navigate("altitude_profile_recording")
                                    altitudeRecordingViewModel.updateRecording(
                                        MainActivity.Recording.STARTING.ordinal)
                                },
                                text = a.roundToInt().toString(),
                                //maxLines = 1,
                                autoSize = TextAutoSize.StepBased()
                            )
                            Text("Baro Altitude m")
                            //Text("Tap to Record")
                        }
                    }
                    // Sun/Moon
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(grey)
                            .fillMaxSize()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val currentMillis = System.currentTimeMillis()
                        val currentInstant = Instant.fromEpochMilliseconds(
                            currentMillis
                        )
                        val currentSolarState = currentInstant.calculateSolarState(
                            location1.latitude,
                            location1.longitude
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (currentSolarState.altitude < 0) {
                                sunMoonOctant = "-"
                            } else {
                                when (currentSolarState.azimuth.roundToInt()) {
                                    in 0..45 / 2 -> {
                                        sunMoonOctant = "N"
                                    }

                                    in 45 / 2..45 + 45 / 2 -> {
                                        sunMoonOctant = "NE"
                                    }

                                    in 90 - 45 / 2..90 + 45 / 2 -> {
                                        sunMoonOctant = "E"
                                    }

                                    in 135 - 45 / 2..135 + 90 / 2 -> {
                                        sunMoonOctant = "SE"
                                    }

                                    in 180 - 45 / 2..180 + 45 / 2 -> {
                                        sunMoonOctant = "S"
                                    }

                                    in 225 - 45 / 2..225 + 45 / 2 -> {
                                        sunMoonOctant = "SW"
                                    }

                                    in 270 - 45 / 2..270 + 45 / 2 -> {
                                        sunMoonOctant = "W"
                                    }

                                    in 315 - 45 / 2..315 + 45 / 2 -> {
                                        sunMoonOctant = "NW"
                                    }

                                    in 315 + 45 / 2..360 -> {
                                        sunMoonOctant = "N"
                                    }
                                }
                            }
                            BasicText(
                                modifier = Modifier.clickable {
                                    navController.navigate("sun_moon")
                                },
                                text = sunMoonOctant,
                                autoSize = TextAutoSize.StepBased()
                            )
                            Text("Sun Direction")
                        }
                    }
                }
            }
            // third row
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .background(Color.Transparent)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(grey)
                            .fillMaxSize()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BasicText(
                                modifier = Modifier.clickable {
                                    //navController.navigate("compass")
                                },
                                text = distance.toInt().toString(),
                                autoSize = TextAutoSize.StepBased()
                            )
                            Text("GPS Distance m")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(grey)
                            .fillMaxSize()
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BasicText(
                                modifier = Modifier.clickable {
                                    //navController.navigate("compass")
                                },
                                text = gPSAltitude.toInt().toString(),
                                autoSize = TextAutoSize.StepBased()
                            )
                            Text("GPS Altitude m")
                        }
                    }
                }
            }
            // Fourth Row
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .background(grey)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (compassOctant) {
                    "-" -> {
                        when (heading.roundToInt()) {
                            in 0..45 / 2 -> {
                                compassOctant = "N"
                            }

                            in 45 / 2..45 + 45 / 2 -> {
                                compassOctant = "NE"
                            }

                            in 90 - 45 / 2..90 + 45 / 2 -> {
                                compassOctant = "E"
                            }

                            in 135 - 45 / 2..135 + 45 / 2 -> {
                                compassOctant = "SE"
                            }

                            in 180 - 45 / 2..180 + 45 / 2 -> {
                                compassOctant = "S"
                            }

                            in 225 - 45 / 2..225 + 45 / 2 -> {
                                compassOctant = "SW"
                            }

                            in 270 - 45 / 2..270 + 45 / 2 -> {
                                compassOctant = "W"
                            }

                            in 315 - 45 / 2..315 + 45 / 2 -> {
                                compassOctant = "NW"
                            }

                            in 315 + 45 / 2..359 -> {
                                compassOctant = "N"
                            }
                        }
                    }

                    "N" -> {
                        if (heading > 45 / 2 + 10 && heading < 45 + 45 / 2) {
                            compassOctant = "NE"
                        } else if (heading < 360 - 45 / 2 - 10 && heading > 315 - 45 / 2) {
                            compassOctant = "NW"
                        }
                    }

                    "NE" -> {
                        if (heading > 45 + 45 / 2 + 10) {
                            compassOctant = "E"
                        } else if (heading < 45 - 45 / 2 - 10) {
                            compassOctant = "N"
                        }
                    }

                    "E" -> {
                        if (heading > 90 + 45 / 2 + 10) {
                            compassOctant = "SE"
                        } else if (heading < 90 - 45 / 2 - 10) {
                            compassOctant = "NE"
                        }
                    }

                    "SE" -> {
                        if (heading > 135 + 45 / 2 + 10) {
                            compassOctant = "S"
                        } else if (heading < 135 - 45 / 2 - 10) {
                            compassOctant = "E"
                        }
                    }

                    "S" -> {
                        if (heading > 180 + 45 / 2 + 10) {
                            compassOctant = "SW"
                        } else if (heading < 180 - 45 / 2 - 10) {
                            compassOctant = "SE"
                        }
                    }

                    "SW" -> {
                        if (heading > 225 + 45 / 2 + 10) {
                            compassOctant = "W"
                        } else if (heading < 225 - 45 / 2 - 10) {
                            compassOctant = "S"
                        }
                    }

                    "W" -> {
                        if (heading > 270 + 45 / 2 + 10) {
                            compassOctant = "NW"
                        } else if (heading < 270 - 45 / 2 - 10) {
                            compassOctant = "SW"
                        }
                    }

                    "NW" -> {
                        if (heading > 315 + 45 / 2 + 10) {
                            compassOctant = "N"
                        } else if (heading < 315 - 45 / 2 - 10) {
                            compassOctant = "W"
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicText(
                        modifier = Modifier.clickable {
                            navController.navigate("compass")
                        },
                        text = compassOctant,
                        autoSize = TextAutoSize.StepBased()
                    )
                    //Text(azimuth.roundToInt().toString() + "\u00b0")
                    Text(heading.toInt().toString() + "\u00b0")
                    Text("Heading")
                }
            }
        }
    }
}

        //Text("world")
    //}

    /*if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            dragHandle = {
                BottomSheetDefaults.DragHandle()
            }
        ) {
            Text("hello")
            Text("hello")
            Text("hello")
            Text("hello")
            Text("hello")
            Text("hello")
            Text("hello")
            Text("hello")
            Text("hello")


        }
    }*/

/*Column(
horizontalAlignment = Alignment.CenterHorizontally
) {
val s = String.format(Locale.US, "%.1f", stepsSpeed)
BasicText(
    modifier = Modifier
        .clickable {
            //navController.navigate("steps_profile")
        },
    text = s,
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(),
)
Text("Step Speed steps/s")
}*/