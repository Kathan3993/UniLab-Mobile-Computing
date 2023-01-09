package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.CurrentDevices

/**
 * Adapter class for data binding of Current Devices
 */
class CurrentDevicesAdapter(
    private var currentDevicesList: List<CurrentDevices>,
    var context: Context
) :
    RecyclerView.Adapter<CurrentDevicesAdapter.CurrentDevicesViewHolder>() {

    /**
     * Provides reference to the type of view variables used.
     */
    class CurrentDevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewCDLaptopTypeVal: TextView = itemView.findViewById(R.id.textViewCDLaptopTypeVal)
        var textViewCDConfigurationVal: TextView =
            itemView.findViewById(R.id.textViewCDConfigurationVal)
        var textViewCDLoanTypeVal: TextView = itemView.findViewById(R.id.textViewCDLoanTypeVal)
        var textViewCDLibraryVal: TextView = itemView.findViewById(R.id.textViewCDLibraryVal)
        var textViewCDBorrowDateVal: TextView = itemView.findViewById(R.id.textViewCDBorrowDateVal)
        var textViewCDReturnDateVal: TextView = itemView.findViewById(R.id.textViewCDReturnDateVal)
    }

    // Creates a new view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentDevicesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_devices_card_design, parent, false)
        return CurrentDevicesViewHolder(view)
    }

    // Replaces the content of the view with data entered by the user
    override fun onBindViewHolder(holder: CurrentDevicesViewHolder, position: Int) {
        holder.textViewCDLaptopTypeVal.text = currentDevicesList[position].laptopType
        holder.textViewCDConfigurationVal.text = currentDevicesList[position].configuration
        holder.textViewCDLoanTypeVal.text = currentDevicesList[position].loanType
        holder.textViewCDLibraryVal.text = currentDevicesList[position].library
        holder.textViewCDBorrowDateVal.text = currentDevicesList[position].borrowedDate
        holder.textViewCDReturnDateVal.text = currentDevicesList[position].returnDate
    }

    // Returns the size of the data
    override fun getItemCount(): Int {
        return currentDevicesList.size
    }
}