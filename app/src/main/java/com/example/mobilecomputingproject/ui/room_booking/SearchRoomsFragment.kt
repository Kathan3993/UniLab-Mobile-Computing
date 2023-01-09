package com.example.mobilecomputingproject.ui.room_booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentSearchRoomsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class SearchRoomsFragment : Fragment() {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _binding: FragmentSearchRoomsBinding? = null
    private val binding get() = _binding!!
    private lateinit var library:String
    private lateinit var participants:String
    private lateinit var date:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSearch = binding.buttonSearch1
        buttonSearch.setOnClickListener(View.OnClickListener {
            val dateTxt = binding.TextInputLayoutDate as DatePicker
            val libraryTxt = binding.AutoCompleteTextviewLibrary as TextView
            val participantsTxt = binding.TextInputLayoutPeoples2 as EditText
            library = libraryTxt.text.toString().trim()
            participants = participantsTxt.text.toString().trim()
            val year: Int = dateTxt.getYear()
            val month: Int = dateTxt.getMonth()
            val day: Int = dateTxt.getDayOfMonth()
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val format = SimpleDateFormat("dd-MM-yyyy")
            date = format.format(calendar.getTime())
            dataValidation()
        })
        var libraryName = ArrayList<String>()

        db.collection("library_room").get().addOnSuccessListener {
                result-> for (task in result){
            val library = task.data?.getValue("name").toString()
            if(library !in libraryName){
                libraryName.add(task.data?.getValue("name").toString())
            }

        }
        }
        val autoCompleteTextviewLaptopType = binding.AutoCompleteTextviewLibrary
        val adapterLaptopType =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, libraryName)
        autoCompleteTextviewLaptopType.setAdapter(adapterLaptopType)
    }

    private fun dataValidation(){

        if(library.isEmpty() ||participants.isEmpty() ){
            Toast.makeText(activity,"Field's can't be empty",Toast.LENGTH_SHORT).show()
        }else{
            var occupants = participants.toInt()
            if (occupants > 6) {
                Toast.makeText(
                    activity,
                    "Number of participants should be less than  6",
                    Toast.LENGTH_SHORT
                ).show()


            } else{
                dataNavigation()

            }
        }
}
    private fun dataNavigation(){
        var bundle = Bundle()
        bundle.putString("date", date)
        bundle.putString("library", library)
        bundle.putString("participants", participants)
        findNavController().navigate(R.id.action_searchRoomsFragment_to_roomsFragment, bundle)

    }
}