package com.example.mobilecomputingproject.ui.room_booking

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentProfileBinding
import com.example.mobilecomputingproject.databinding.FragmentRoomPreviewBinding
import com.google.firebase.firestore.FirebaseFirestore


class RoomPreviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentRoomPreviewBinding? = null
    private val binding get() = _binding!!
    private var _binding1: FragmentProfileBinding? = null
    private val binding1 get() = _binding1!!
    var database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoomPreviewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _binding1 = FragmentProfileBinding.inflate(inflater, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val submitbutton: Button = binding.buttonPreviewBook
        val editbutton: Button = binding.buttonPreviewEdit
        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var fname = sharedPref.getString("firstname", "xyz")
        var lname = sharedPref.getString("lastname", "1")
        var purpose = requireArguments().get("purpose").toString()
        var listOfParticipants = requireArguments().get("listOfParticipants").toString()
        var telephone = requireArguments().get("telephone").toString()
        var email = sharedPref.getString("email", "abc")
        var date = requireArguments().get("date").toString()
        var library = requireArguments().get("library").toString()
        var participants = requireArguments().get("participants").toString()
        var room = requireArguments().get("room").toString()
        var slot = requireArguments().get("slot").toString()

        binding.textViewPreviewFirstNameVal.text = fname
        binding.simpleTextViewPreviewLastNameVal.text = lname
        binding.simpleTextViewPreviewPurposeVal.text = purpose
        binding.simpleTextViewPreviewParticipantsVal.text = listOfParticipants
        binding.simpleTextViewPreviewTelephoneVal.text = telephone
        binding.simpleTextViewPreviewEmailVal.text = email
        binding.simpleTextViewPreviewPhoneLibraryVal.text = library
        binding.simpleTextViewPreviewRoomVal.text = room
        binding.simpleTextViewPreviewBookingTimeVal.text = slot
        binding.simpleTextViewPreviewBookingDateVal.text = date


        submitbutton.setOnClickListener {
            val items = HashMap<String, Any>()
            items.put("firstName", fname!!)
            items.put("lastName", lname!!)
            items.put("purpose", purpose)
            items.put("listOfParticipants", listOfParticipants)
            items.put("telephone", telephone)
            items.put("userId", email!!)
            items.put("checkedIn", false)
            items.put("status", "Booked")
            items.put("date", date)
            items.put("library", library)
            items.put("participants", participants)
            items.put("room", room)
            items.put("slot", slot)
            database.collection("RoomBooking").document().set(items).addOnSuccessListener {
                findNavController().navigate(R.id.action_roomPreviewFragment_to_roomBookingHomeFragment)
            }.addOnFailureListener {
                Log.d("fail", "fail")
            }
        }
        editbutton.setOnClickListener {

            var bundle = Bundle()
            bundle.putString("date", date)
            bundle.putString("library", library)
            bundle.putString("participants", participants)
            bundle.putString("room", room)
            bundle.putString("slot", slot)

            findNavController().navigate(
                R.id.action_roomPreviewFragment_to_roomFormFragment,
                bundle
            )
        }

    }


}