package com.ravos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
data class TariffListModel(@JsonProperty("tariff") var tariffs: ArrayList<TariffModel>)

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
@Entity
data class TariffModel(
    @JsonProperty("id")
    @PrimaryKey
    var id: String,
    @JsonProperty("security_deposit")
    var security_deposit: Double? = null,
    @JsonProperty("currency")
    var currency: String? = null,
    @JsonProperty("base_fare")
    var base_fare: Double? = null,
    @JsonProperty("per_minute")
    var per_minute: Double? = null,
    @JsonProperty("pause_charge")
    var pause_charge: Double? = null,
    @JsonProperty("pause_charge_per")
    var pause_charge_per: String? = null
) : Parcelable