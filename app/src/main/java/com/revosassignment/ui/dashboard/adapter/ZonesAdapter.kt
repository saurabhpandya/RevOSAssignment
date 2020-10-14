package com.revosassignment.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ravos.data.model.ZoneModel
import com.revosassignment.R
import com.revosassignment.databinding.RawZoneBinding
import com.revosassignment.utility.OnItemClickListener

class ZonesAdapter(var arylstZones: ArrayList<ZoneModel>) :
    RecyclerView.Adapter<ZonesAdapter.ViewHolder>() {

    lateinit var onItemClickListner: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawZoneBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_zone,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstZones.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstZones.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position, true)
        }

    }

    fun setZones(arylstZones: ArrayList<ZoneModel>) {
        this.arylstZones = arylstZones
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawZoneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(zone: ZoneModel) {
            binding.txtvwLocation.text = zone.location

            val numOfVehicleNType = "${zone.no_of_vehicles} ${zone.vehicle_type}"
            binding.txtvwNoOfVehicle.text = numOfVehicleNType

            if (!zone.distance.isNullOrEmpty()) {
                val longDistance = zone.distance!!.toLong()
                var distance = ""
                if (longDistance < 1000) {
                    distance = "$longDistance m"
                } else {
                    val doubleDistance = longDistance / 1000
                    distance = "$doubleDistance km"
                }
                binding.txtvwDistance.text = distance
            } else {
                binding.txtvwDistance.text = ""
            }


        }
    }

}