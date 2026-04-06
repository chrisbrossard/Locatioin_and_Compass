package com.example.locationandcompass.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.locationandcompass.data.AltitudeSample
import com.example.locationandcompass.data.StepSample
import com.example.locationandcompass.data.StepSampleDao
import java.util.concurrent.Executors

@Database(
    entities = [
        StepSample::class,
        AltitudeSample::class,
        StepSession::class,
        AltitudeSession::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stepSampleDao(): StepSampleDao
    abstract fun altitudeSampleDao(): AltitudeSampleDao
    abstract fun stepSessionDao(): StepSessionDao
    abstract fun altitudeSessionDao(): AltitudeSessionDao

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