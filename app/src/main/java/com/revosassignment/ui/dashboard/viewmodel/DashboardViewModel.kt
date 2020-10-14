package com.revosassignment.ui.dashboard.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobiquityassignment.base.BaseViewModel
import com.ravos.RevOS
import com.ravos.data.booking.BOOKING_STATUS
import com.ravos.data.model.BookingInfoModel
import com.ravos.data.model.VehicleModel
import com.ravos.data.model.ZoneModel
import com.revosassignment.data.dashboard.DashboardRepository
import com.revosassignment.ui.dashboard.adapter.ZoneVehiclesAdapter
import com.revosassignment.ui.dashboard.adapter.ZonesAdapter
import com.revosassignment.utility.Resource
import com.revosassignment.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel(
    application: Application,
    dashboardRepository: DashboardRepository
) : BaseViewModel(application) {
    private val TAG = this::class.java.canonicalName

    private var arylstZones = ArrayList<ZoneModel>()
    val zonesAdapter = ZonesAdapter(arylstZones)

    private var arylstZoneVehicles = ArrayList<VehicleModel>()
    val zoneVehiclesAdapter = ZoneVehiclesAdapter(arylstZoneVehicles)

    var selectedZoneModel = ZoneModel()

    var activeBookingInfoLiveData = MutableLiveData<Resource<Boolean>>()

    var activeBookingInfoModel: BookingInfoModel? = null

    var completeRideLiveData = MutableLiveData<Resource<Boolean>>()

    var zoneLiveData = MutableLiveData<Resource<Boolean>>()

    var moveCamera = false

    fun getZones() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            activeBookingInfoLiveData.value = Resource.loading(null)
        }
        try {
            arylstZones = RevOS.getZones()
            Log.d(TAG, "Zones : $arylstZones")
            withContext(Dispatchers.Main) {
                zonesAdapter.setZones(arylstZones)
                zoneLiveData.value = Resource.success(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                zoneLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }

    }

    fun getZoneList() = arylstZones

    fun getVehicles(zoneId: String) = viewModelScope.launch(Dispatchers.IO) {
        arylstZoneVehicles = RevOS.getVehicles(zoneId)
        Log.d(TAG, "Zone Vehicles : $arylstZoneVehicles")
        withContext(Dispatchers.Main) {
            zoneVehiclesAdapter.setZoneVehicles(arylstZoneVehicles)
        }
    }

    fun getZoneVehicleList() = arylstZoneVehicles

    fun getActiveBooking() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            activeBookingInfoLiveData.value = Resource.loading(null)
        }
        try {

            activeBookingInfoModel = RevOS.getActiveBooking()
            Log.d(TAG, "getActiveBooking:activeBookingInfo::$activeBookingInfoModel")

            withContext(Dispatchers.Main) {
                if (activeBookingInfoModel == null) {
                    activeBookingInfoLiveData.value = Resource.success(false)
                } else {
                    activeBookingInfoLiveData.value = Resource.success(true)
                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                activeBookingInfoLiveData.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    fun completeBooking() =
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                completeRideLiveData.value = Resource.loading(null)
            }
            try {
                activeBookingInfoModel?.status = BOOKING_STATUS.COMPLETED.name
                activeBookingInfoModel?.ride_end_time = Utility.getCurrentDate()
                RevOS.updateBooking(activeBookingInfoModel!!)

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