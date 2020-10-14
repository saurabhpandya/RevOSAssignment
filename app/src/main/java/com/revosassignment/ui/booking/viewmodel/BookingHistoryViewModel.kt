package com.revosassignment.ui.booking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.ravos.RevOS
import com.ravos.data.booking.BOOKING_STATUS
import com.ravos.data.model.BookingInfoModel
import com.revosassignment.ui.booking.adapter.BookingHistoryAdapter
import com.revosassignment.utility.Resource
import com.revosassignment.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingHistoryViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    var arylstBookingHistory = ArrayList<BookingInfoModel>()

    val bookingHistoryAdapter = BookingHistoryAdapter(arylstBookingHistory)

    var bookingHistoryInfoLiveData = MutableLiveData<Resource<ArrayList<BookingInfoModel>>>()

    var completeRideLiveData = MutableLiveData<Resource<Boolean>>()

    fun getBooking() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            bookingHistoryInfoLiveData.value = Resource.loading(null)
        }
        try {

            arylstBookingHistory = RevOS.getBookings()

            Log.d(TAG, "getBooking:arylstBookingHistory::$arylstBookingHistory")

            withContext(Dispatchers.Main) {
                bookingHistoryInfoLiveData.value = Resource.success(arylstBookingHistory)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                bookingHistoryInfoLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    fun completeBooking(position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                completeRideLiveData.value = Resource.loading(null)
            }
            try {
                val completeBookingInfoModel = arylstBookingHistory[position]
                completeBookingInfoModel.status = BOOKING_STATUS.COMPLETED.name
                completeBookingInfoModel.ride_end_time = Utility.getCurrentDate()
                RevOS.updateBooking(completeBookingInfoModel)

                withContext(Dispatchers.Main) {
                    completeRideLiveData.value = Resource.success(true)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    completeRideLiveData.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

}