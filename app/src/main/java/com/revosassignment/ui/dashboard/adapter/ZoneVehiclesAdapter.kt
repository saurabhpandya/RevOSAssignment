package com.revosassignment.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ravos.data.model.VehicleModel
import com.revosassignment.R
import com.revosassignment.databinding.RawZoneVehicleBinding
import com.revosassignment.utility.OnItemClickListener

class ZoneVehiclesAdapter(var arylstZoneVehicles: ArrayList<VehicleModel>) :
    RecyclerView.Adapter<ZoneVehiclesAdapter.ViewHolder>() {

    lateinit var onItemClickListner: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawZoneVehicleBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_zone_vehicle,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstZoneVehicles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstZoneVehicles.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position, false)
        }

    }

    fun setZoneVehicles(arylstZoneVehicles: ArrayList<VehicleModel>) {
        this.arylstZoneVehicles = arylstZoneVehicles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawZoneVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(zoneVehicle: VehicleModel) {
            binding.txtvwVehicledId.text = zoneVehicle.id
            var kmRange = ""
            if (zoneVehicle.km_range!! > 999) {
                var kmRangeDouble = zoneVehicle.km_range!! / 1000
                kmRange = "${kmRangeDouble} km"
            } else {
                kmRange = "${zoneVehicle.km_range} m"
            }
            binding.txtvwRangeKm.text = kmRange

            binding.txtvwBattery.text = "${zoneVehicle.battery_per} %"

        }
    }

}