package com.example.mobilecomputingproject.adapters

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.LibraryDetails

class LibraryAdapter(private val libraryList: ArrayList<LibraryDetails>, var context: Context) :
    RecyclerView.Adapter<LibraryAdapter.MyViewHolder>() {

    private lateinit var mlistner: onItemClickListner


    fun setOnItemClickListner(listner: onItemClickListner) {
        mlistner = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_list_card_design, parent, false)
        return MyViewHolder(itemView, mlistner)
    }

    override fun onBindViewHolder(holder: LibraryAdapter.MyViewHolder, position: Int) {

        val currentItem = libraryList[position]
        holder.libName.text = currentItem.name
        Log.e("inage link", currentItem.img.toString())
        Glide.with(context)
            .load(currentItem.img.toString())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.img);
    }

    override fun getItemCount(): Int {
        return libraryList.size
    }

    class MyViewHolder(itemView: View, listner: onItemClickListner) :
        RecyclerView.ViewHolder(itemView) {
        val libName: TextView = itemView.findViewById(R.id.library_name)
        val img: ImageView = itemView.findViewById(R.id.library_image)

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }

    }


    interface onItemClickListner {
        fun onItemClick(position: Int)
    }
}