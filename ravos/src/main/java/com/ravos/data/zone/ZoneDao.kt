package com.ravos.data.zone

import androidx.room.*
import com.ravos.data.model.ZoneModel

@Dao
interface ZoneDao {
    @Query("SELECT * FROM zonemodel")
    suspend fun getAll(): List<ZoneModel>

    @Insert
    suspend fun insertAll(zones: List<ZoneModel>)

    @Delete
    suspend fun delete(deleteZone: ZoneModel)

    @Query("DELETE FROM zonemodel")
    suspend fun deleteAll()

    @Update
    suspend fun update(updateZone: ZoneModel)
}