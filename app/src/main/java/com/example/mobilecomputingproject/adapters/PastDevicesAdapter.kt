package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.PastDevices

/**
 * Adapter class for data binding of Past Devices
 */
class PastDevicesAdapter(private var pastDevicesList: List<PastDevices>, var context: Context) :
    RecyclerView.Adapter<PastDevicesAdapter.PastDevicesViewHolder>() {

    /**
     * Provides reference to the type of view variables used.
     */
    class PastDevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewPDLaptopTypeVal: TextView = itemView.findViewById(R.id.textViewPDLaptopTypeVal)
        var textViewPDConfigurationVal: TextView =
            itemView.findViewById(R.id.textViewPDConfigurationVal)
        var textViewPDLoanTypeVal: TextView = itemView.findViewById(R.id.textViewPDLoanTypeVal)
        var textViewPDLibraryVal: TextView = itemView.findViewById(R.id.textViewPDLibraryVal)
        var textViewPDBorrowDateVal: TextView = itemView.findViewById(R.id.textViewPDBorrowDateVal)
        var textViewPDReturnDateVal: TextView = itemView.findViewById(R.id.textViewPDReturnDateVal)
    }

    // Creates a new view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastDevicesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.past_devices_card_design, parent, false)
        return PastDevicesViewHolder(view)
    }

    // Replaces the content of the view with data entered by the user
    override fun onBindViewHolder(holder: PastDevicesViewHolder, position: Int) {
        holder.textViewPDLaptopTypeVal.text = pastDevicesList[position].laptopType
        holder.textViewPDConfigurationVal.text = pastDevicesList[position].configuration
        holder.textViewPDLoanTypeVal.text = pastDevicesList[position].loanType
        holder.textViewPDLibraryVal.text = pastDevicesList[position].library
        holder.textViewPDBorrowDateVal.text = pastDevicesList[position].borrowedDate
        holder.textViewPDReturnDateVal.text = pastDevicesList[position].returnedDate
    }

    // Returns the size of the data
    override fun getItemCount(): Int {
        return pastDevicesList.size
    }

}