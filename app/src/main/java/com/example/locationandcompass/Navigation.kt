package com.example.locationandcompass

import android.annotation.SuppressLint
import android.location.GnssStatus
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.locationandcompass.data.AltitudeSampleDao
import com.example.locationandcompass.data.AltitudeSessionDao
import com.example.locationandcompass.data.LocationSampleDao
import com.example.locationandcompass.data.LocationSessionDao
import com.example.locationandcompass.data.StepSampleDao
import com.example.locationandcompass.data.StepSessionDao
import com.example.locationandcompass.screens.AltitudeProfileRecordingScreen
import com.example.locationandcompass.screens.AltitudeProfileViewingScreen
import com.example.locationandcompass.screens.CompassScreen
import com.example.locationandcompass.screens.DistanceProfileRecordingScreen
import com.example.locationandcompass.screens.DistanceProfileViewingScreen
import com.example.locationandcompass.screens.GNSSScreen
import com.example.locationandcompass.screens.OverviewScreen
import com.example.locationandcompass.screens.StepsProfileRecordingScreen
import com.example.locationandcompass.screens.StepsProfileViewingScreen
import com.example.locationandcompass.screens.SunMoonScreen
import com.example.locationandcompass.screens.VerticalSpeedScreen
import com.example.locationandcompass.viewmodel.AltitudeDeleteViewModel
import com.example.locationandcompass.viewmodel.AltitudeListViewModel
import com.example.locationandcompass.viewmodel.AltitudeRecordingViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionCountViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionIdViewModel
import com.example.locationandcompass.viewmodel.AltitudeSessionListViewModel
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
import com.example.locationandcompass.viewmodel.DistanceViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeListViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeRecordingViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeSessionDao
import com.example.locationandcompass.viewmodel.GPSAltitudeSessionIdViewModel
import com.example.locationandcompass.viewmodel.GPSAltitudeViewModel
import com.example.locationandcompass.viewmodel.LocationListViewModel
import com.example.locationandcompass.viewmodel.LocationRecordingViewModel
import com.example.locationandcompass.viewmodel.LocationSampleViewModel
import com.example.locationandcompass.viewmodel.LocationSessionCountViewModel
import com.example.locationandcompass.viewmodel.LocationSessionIdViewModel
import com.example.locationandcompass.viewmodel.LocationSessionListViewModel
import com.example.locationandcompass.viewmodel.NavigationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.time.ExperimentalTime

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@SuppressLint()
@Composable
fun Navigation(
    client: FusedLocationProviderClient,
    //azimuth: Float,
    //pressure: Float,
    gnssStatus: GnssStatus?,
    magnetometerAccuracy: Int,
    //altitudeSlope: Double,
    //sampledAltitudes: ArrayDeque<Int>,
    //stepsDeque: ArrayDeque<Long>,
    //stepsTimesDeque: ArrayDeque<Long>,
    //stepsSpeed: Float,
    altitudeSampleDao: AltitudeSampleDao,
    stepSampleDao: StepSampleDao,
    stepSessionDao: StepSessionDao,
    altitudeSessionDao: AltitudeSessionDao,
    //steps: Int,
    stepCountViewModel: StepCountViewModel,
    stepListViewModel: StepListViewModel,
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
    navController: NavHostController,
    headingViewModel: HeadingViewModel,
    stepViewModel: StepViewModel,
    verticalSpeedViewModel: VerticalSpeedViewModel,
    pressureViewModel: PressureViewModel,
    distanceViewModel: DistanceViewModel,
    gPSAltitudeViewModel: GPSAltitudeViewModel,
    gPSAltitudeSessionDao: GPSAltitudeSessionDao,
    gPSAltitudeSessionIdViewModel: GPSAltitudeSessionIdViewModel,
    gPSAltitudeListViewModel: GPSAltitudeListViewModel,
    gPSAltitudeRecordingViewModel: GPSAltitudeRecordingViewModel,
    locationListViewModel: LocationListViewModel,
    locationRecordingViewModel: LocationRecordingViewModel,
    locationSessionIdViewModel: LocationSessionIdViewModel,
    locationSessionDao: LocationSessionDao,
    locationSessionListViewModel: LocationSessionListViewModel,
    locationSampleDao: LocationSampleDao,
    locationSessionCountViewModel: LocationSessionCountViewModel,
    locationSampleViewModel: LocationSampleViewModel,
    navigationViewModel: NavigationViewModel
    ) {
    var location1 by remember { mutableStateOf(Location("")) }

    Log.d("Location and Compass", "Navigation() called")

    LaunchedEffect(Unit) {
        requestCurrentLocation(
            client
        ) { location ->
            location1 = location
        }
    }
    NavHost(
        navController = navController,
        startDestination = "overview"
    ) {
        composable("overview") {
            OverviewScreen(
                client,
                navController,
                //altitudeSlope,
                //pressure,
                //azimuth,
                magnetometerAccuracy,
                //sampledAltitudes,
                //stepsDeque,
                //stepsSpeed,
                altitudeSampleDao,
                stepSampleDao,
                stepSessionDao,
                altitudeSessionDao,
                //steps,
                stepCountViewModel,
                stepListViewModel,
                stepSessionCountViewModel,
                stepSessionListViewModel,
                stepSessionIdViewModel,
                stepRecordingViewModel,
                stepDeleteViewModel,
                altitudeListViewModel,
                altitudeSessionCountViewModel,
                altitudeSessionListViewModel,
                altitudeSessionIdViewModel,
                altitudeRecordingViewModel,
                altitudeDeleteViewModel,
                onNavigateToAltitudeRecording = {
                    navController.navigate("altitude_profile_recording") },
                headingViewModel = headingViewModel,
                stepViewModel = stepViewModel,
                verticalSpeedViewModel = verticalSpeedViewModel,
                pressureViewModel = pressureViewModel,
                distanceViewModel = distanceViewModel,
                gPSAltitudeViewModel = gPSAltitudeViewModel,
                gPSAltitudeSessionDao,
                gPSAltitudeSessionIdViewModel,
                gPSAltitudeRecordingViewModel,
                locationListViewModel,
                locationRecordingViewModel,
                locationSessionIdViewModel,
                locationSessionDao,
                locationSessionListViewModel,
                locationSampleDao,
                locationSessionCountViewModel,
                navigationViewModel
            )
        }
        composable("sun_moon") {
            SunMoonScreen(
                location1
            )
        }
        composable("compass") {
            CompassScreen(
                navController,
                location1,
                //azimuth,
                magnetometerAccuracy,
                headingViewModel
            )
        }
        composable("gnss") {
            GNSSScreen(gnssStatus)
        }
        composable("altitude_profile_recording") {
            AltitudeProfileRecordingScreen(
                //sampledAltitudes,
                altitudeListViewModel,
                altitudeRecordingViewModel,
                navController,
                altitudeSessionIdViewModel,
                gPSAltitudeListViewModel = gPSAltitudeListViewModel,
                gPSAltitudeSessionIdViewModel,
                gPSAltitudeRecordingViewModel
            )
        }
        composable("altitude_profile_viewing") {
            AltitudeProfileViewingScreen(
                //sampledAltitudes,
                altitudeListViewModel,
                altitudeSessionIdViewModel,
                gPSAltitudeListViewModel,
                gPSAltitudeSessionIdViewModel
            )
        }
        composable("steps_profile_recording") {
            StepsProfileRecordingScreen(
                //stepsDeque,
                //stepsTimesDeque,
                //stepSampleDao,
                stepListViewModel,
                stepRecordingViewModel,
                navController,
                stepSessionIdViewModel
            )
        }
        composable("steps_profile_viewing") {
            StepsProfileViewingScreen(
                //stepsDeque,
                //stepsTimesDeque,
                //stepSampleDao,
                stepListViewModel,
                //stepRecordingViewModel,
                stepSessionIdViewModel
            )
        }
        composable("distance_profile_recording") {
            DistanceProfileRecordingScreen(
                //stepsDeque,
                //stepsTimesDeque,
                //stepSampleDao,
                locationListViewModel,
                locationRecordingViewModel,
                navController,
                locationSessionIdViewModel,
                locationSampleViewModel
            )
        }
        composable("distance_profile_viewing") {
            DistanceProfileViewingScreen(
                //stepsDeque,
                //stepsTimesDeque,
                //stepSampleDao,
                locationListViewModel,
                //stepRecordingViewModel,
                locationSessionIdViewModel,
                locationSampleViewModel,
                navigationViewModel
            )
        }
        composable("vertical_speed") {
            VerticalSpeedScreen(
                //altitudeSlope,
                verticalSpeedViewModel)
        }
    }

    fun onNavigateToAltitudeRecording() {
        navController.navigate("apr")
    }
}
