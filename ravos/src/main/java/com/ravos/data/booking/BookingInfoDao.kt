package com.ravos.data.booking

import androidx.room.*
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.VehicleModel

@Dao
interface BookingInfoDao {
    @Query("SELECT * FROM bookingInfoModel order by bookingId DESC ")
    suspend fun getAll(): List<BookingInfoModel>

    @Query("SELECT * FROM bookingInfoModel where status = :status")
    suspend fun getByStatus(status: String): BookingInfoModel

    @Query("SELECT * FROM bookingInfoModel where bookingId = :bookingId")
    suspend fun getByBookingId(bookingId: String): List<BookingInfoModel>

    @Insert
    suspend fun insertAll(bookings: List<BookingInfoModel>): LongArray

    @Delete
    suspend fun delete(deleteBooking: BookingInfoModel)

    @Query("DELETE FROM bookingInfoModel")
    suspend fun deleteAll()

    @Update
    suspend fun update(updateBooking: BookingInfoModel)
}