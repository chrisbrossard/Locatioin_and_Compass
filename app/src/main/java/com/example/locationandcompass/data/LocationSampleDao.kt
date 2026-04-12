package com.example.locationandcompass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationSampleDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(location: LocationSample)

    @Query("SELECT * FROM locations WHERE locationId = (:locationSampleId)")
    fun findById(locationSampleId: Int): LocationSample

    @Query("SELECT * FROM locations ORDER BY time")
    fun getAll(): Flow<List<LocationSample>>

    @Query("SELECT * FROM locations ORDER BY time ASC LIMIT 1")
    fun getFirst(): Flow<LocationSample>

    @Query("SELECT COUNT(*) FROM locations")
    fun getRowCount(): Flow<Int>

    @Query("DELETE FROM locations WHERE sessionId = :sessionId")
    fun deleteBySessionId(sessionId: Long)

    @Delete
    fun delete(locationSample: LocationSample)
}