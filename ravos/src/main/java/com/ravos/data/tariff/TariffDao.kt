package com.ravos.data.tariff

import androidx.room.*
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
@Dao
interface TariffDao {
    @Query("SELECT * FROM tariffmodel")
    suspend fun getAll(): List<TariffModel>

    @Query("SELECT * FROM tariffmodel where id = :tariffId")
    suspend fun getById(tariffId: String): List<TariffModel>

    @Insert
    suspend fun insertAll(arylstTariffs: List<TariffModel>)

    @Delete
    suspend fun delete(deleteTariff: TariffModel)

    @Query("DELETE FROM tariffmodel")
    suspend fun deleteAll()

    @Update
    suspend fun update(updateTariff: TariffModel)
}