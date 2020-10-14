package com.revosassignment.ui.booking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.mobiquityassignment.base.TextChangeComponent
import com.ravos.RevOS
import com.ravos.data.booking.BOOKING_STATUS
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.TariffModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.revosassignment.utility.Resource
import com.revosassignment.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    var bookingInfoModel = BookingInfoModel()

    var zoneModel = ZoneModel()
    var vehicleModel = VehicleModel("")
    var tariffModel = TariffModel("")

    var errUserName = MutableLiveData<String>("")
    var errMobile = MutableLiveData<String>("")
    var errDob = MutableLiveData<String>("")

    var saveBookingInfoLiveData = MutableLiveData<Resource<Long>>()

    fun validateUserData() {
        if (!bookingInfoModel.userName.isNullOrEmpty()) {
            if (!bookingInfoModel.mobileNumber.isNullOrEmpty()) {
                if (!bookingInfoModel.dob.isNullOrEmpty()) {
                    bookingInfoModel.status = BOOKING_STATUS.INITIATED.name
                    bookingInfoModel.ride_start_time = Utility.getCurrentDate()
                    saveBooking()
                } else {
                    errDob.value = "Enter Date of Birth"
                }
            } else {
                errMobile.value = "Enter Mobile"
            }
        } else {
            errUserName.value = "Enter Name"
        }
    }

    private fun saveBooking() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveBookingInfoLiveData.value = Resource.loading(null)
        }
        try {

            val insertedId = RevOS.saveBooking(bookingInfoModel)
            Log.d(TAG, "saveBooking:insertedId::$insertedId")
            bookingInfoModel.bookingId = insertedId[0]
            withContext(Dispatchers.Main) {
                saveBookingInfoLiveData.value = Resource.success(insertedId[0])
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                saveBookingInfoLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {
        when (textChangeComponent) {
            TextChangeComponent.BOOK_RIDE_USER_NAME -> {
                errUserName.postValue("")
            }
            TextChangeComponent.BOOK_RIDE_MOBILE -> {
                errMobile.postValue("")
            }
            TextChangeComponent.BOOK_RIDE_DOB -> {
                errDob.postValue("")
            }
        }

    }

}
