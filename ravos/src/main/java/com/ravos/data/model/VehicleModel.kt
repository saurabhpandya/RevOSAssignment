package com.ravos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize


@JsonIgnoreProperties(ignoreUnknown = true)
data class ZoneVehicleModel(
    @JsonProperty("zone_vehicles") var vehicles: ArrayList<VehicleModel>
)

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
@Entity
data class VehicleModel(
    @JsonProperty("id")
    @PrimaryKey
    var id: String,
    @JsonProperty("zone_id")
    var zone_id: String? = null,
    @JsonProperty("battery_per")
    var battery_per: Int? = null,
    @JsonProperty("km_range")
    var km_range: Long? = null,
    @JsonProperty("vehicle_type")
    var vehicle_type: String? = null
) : Parcelable