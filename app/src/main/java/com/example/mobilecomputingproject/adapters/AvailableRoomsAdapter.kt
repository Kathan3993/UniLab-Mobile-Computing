package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.StudyRooms


class AvailableRoomsAdapter(
    private var availableRoomsList: List<StudyRooms>,
    var context: Context,
    var fragmentManager: FragmentManager?,
    var date: String,
    var library: String,
    var participants: String,
    var laptop_type: Array<String>
) : RecyclerView.Adapter<AvailableRoomsAdapter.AvailableRoomsViewHolder>() {
    private lateinit var slotBooked:String
    private lateinit var roomnumber:String
    class AvailableRoomsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewPDRoomNum: TextView = itemView.findViewById(R.id.textViewPDRoomNum)
        var autoCompleteTextviewSlot: AutoCompleteTextView =
            itemView.findViewById(R.id.autoCompleteTextviewSlot)
        var buttonBook: Button = itemView.findViewById(R.id.buttonBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableRoomsViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.rooms_card_design, parent, false)

        return AvailableRoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvailableRoomsViewHolder, position: Int) {
        holder.textViewPDRoomNum.text = availableRoomsList[position].rooms
        holder.buttonBook.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            roomnumber =  holder.textViewPDRoomNum.text.toString().trim()
            slotBooked = holder.autoCompleteTextviewSlot.text.toString().trim()
            if(slotBooked.isEmpty()){
                Toast.makeText(context,"please select the slot",Toast.LENGTH_SHORT).show()
            }else{
                var bundle = Bundle()
                bundle.putString("date", date)
                println("this is available room adapted $date")
                bundle.putString("library", library)
                bundle.putString("participants", participants)
                bundle.putString("room", roomnumber)
                bundle.putString("slot",slotBooked)

                val activity = it.context as AppCompatActivity
                val navHostFragment =
                    activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main_navigation) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_roomsFragment_to_roomFormFragment, bundle)
            }


        }
        val autoCompleteTextviewLaptopType = holder.autoCompleteTextviewSlot
        val adapterLaptopType =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, laptop_type)
        autoCompleteTextviewLaptopType.setAdapter(adapterLaptopType)
    }

    override fun getItemCount(): Int {
        return availableRoomsList.size
    }



}