package com.ravos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
data class ZonesModel(@JsonProperty("zones") var zones: ArrayList<ZoneModel>)

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
@Entity
data class ZoneModel(
    @PrimaryKey
    @JsonProperty("id")
    var id: String = "",
    @JsonProperty("location")
    var location: String? = null,
    @JsonProperty("distance")
    var distance: String? = null,
    @JsonProperty("no_of_vehicles")
    var no_of_vehicles: String? = null,
    @JsonProperty("vehicle_type")
    var vehicle_type: String? = null,
    @JsonProperty("latitude")
    var latitude: Double? = null,
    @JsonProperty("longitude")
    var longitude: Double? = null
) : Parcelable