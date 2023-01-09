package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.LibraryEvent


class LibraryEventAdapter(
    private val libraryEventList: ArrayList<LibraryEvent>,
    var context: Context
) : RecyclerView.Adapter<LibraryEventAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.event_item,
            parent, false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val libraryEvent: LibraryEvent = libraryEventList[position]

        holder.eventTitle.text = libraryEvent.eventTitle
        holder.eventDescription.text = libraryEvent.eventDescription
        holder.libraryID.text = libraryEvent.libraryID
        holder.targetedAudience.text = libraryEvent.targetedAudience


    }

    override fun getItemCount(): Int {
        return libraryEventList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val eventTitle: TextView = itemView.findViewById(R.id.eventTitleID)
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescriptionID)
        val libraryID: TextView = itemView.findViewById(R.id.libraryID)
        val targetedAudience: TextView = itemView.findViewById(R.id.targetedAudienceID)

    }

}