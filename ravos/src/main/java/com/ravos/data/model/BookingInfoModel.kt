package com.ravos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
@Entity
data class BookingInfoModel(
    @JsonProperty("bookingId")
    @PrimaryKey(autoGenerate = true)
    var bookingId: Long? = null,
    @JsonProperty("zoneId")
    var zoneId: String? = null,
    @JsonProperty("vehicleId")
    var vehicleId: String? = null,
    @JsonProperty("tariffId")
    var tariffId: String? = null,
    @JsonProperty("userName")
    var userName: String? = null,
    @JsonProperty("mobileNumber")
    var mobileNumber: String? = null,
    @JsonProperty("dob")
    var dob: String? = null,
    @JsonProperty("status")
    var status: String? = null,
    @JsonProperty("ride_start_time")
    var ride_start_time: String? = null,
    @JsonProperty("ride_end_time")
    var ride_end_time: String? = null
) : Parcelable