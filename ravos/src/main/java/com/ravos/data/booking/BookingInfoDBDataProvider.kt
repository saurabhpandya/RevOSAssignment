package com.ravos.data.booking

import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.VehicleModel
import com.ravos.db.AppDatabase

class BookingInfoDBDataProvider(private val appDatabase: AppDatabase) : BookingInfoDataProvider {
    private val TAG = this::class.java.canonicalName

    override suspend fun getBookings() =
        appDatabase.bookingInfoDao().getAll() as ArrayList<BookingInfoModel>

    override suspend fun getBookingByStatus(status: String) =
        appDatabase.bookingInfoDao().getByStatus(status)


    override suspend fun getBooking(bookingId: String) =
        appDatabase.bookingInfoDao().getByBookingId(bookingId) as ArrayList<BookingInfoModel>

    override suspend fun insertAll(arylstBookings: ArrayList<BookingInfoModel>) =
        appDatabase.bookingInfoDao().insertAll(arylstBookings)


    override suspend fun udpateBooking(updateBooking: BookingInfoModel) {
        appDatabase.bookingInfoDao().update(updateBooking)
    }

    override suspend fun deleteBooking(deleteBooking: BookingInfoModel) {

    }
}