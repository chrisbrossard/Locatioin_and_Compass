package com.example.locationandcompass.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.locationandcompass.viewmodel.GPSAltitudeSessionDao
import com.example.locationandcompass.data.AltitudeSample
import com.example.locationandcompass.data.StepSample
import com.example.locationandcompass.data.StepSampleDao
import java.util.concurrent.Executors

@Database(
    entities = [
        StepSample::class,
        AltitudeSample::class,
        GPSAltitudeSample::class,
        StepSession::class,
        AltitudeSession::class,
        GPSAltitudeSession::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stepSampleDao(): StepSampleDao
    abstract fun altitudeSampleDao(): AltitudeSampleDao
    abstract fun gPSAltitudeSampleDao(): GPSAltitudeSampleDao
    abstract fun stepSessionDao(): StepSessionDao
    abstract fun altitudeSessionDao(): AltitudeSessionDao
    abstract fun gPSAltitudeSessionDao(): GPSAltitudeSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lc_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}