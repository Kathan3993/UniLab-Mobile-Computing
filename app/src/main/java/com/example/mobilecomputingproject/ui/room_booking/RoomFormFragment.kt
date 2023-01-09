package com.example.mobilecomputingproject.ui.room_booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentRoomFormBinding
import com.google.firebase.firestore.FirebaseFirestore


class RoomFormFragment : Fragment() {


    private var _binding: FragmentRoomFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var purpose: String
    private lateinit var telephone: String
    private lateinit var date: String
    private lateinit var library: String
    private lateinit var room: String
    private lateinit var slot: String
    private lateinit var occupants:String
    var database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoomFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonContinue: Button = binding.buttonContinue
        var message = binding.simpleTextViewDetails
        room = requireArguments().get("room").toString()
        slot = requireArguments().get("slot").toString()
        occupants = requireArguments().get("participants").toString()
        message.text = ""
        buttonContinue.setOnClickListener(View.OnClickListener {
            var purposeTxt = binding.TextInputPurposeTxt as EditText
            val telephoneTxt = binding.TextInputTelephoneTxt as EditText
            purpose = purposeTxt.text.toString().trim()
            telephone = telephoneTxt.text.toString().trim()
            date = requireArguments().get("date").toString()
            library = requireArguments().get("library").toString()
            dataValidation()

        })
    }

    private fun dataValidation() {
        if (purpose.isEmpty() || telephone.isEmpty() || date.isEmpty() || library.isEmpty()) {
            Toast.makeText(activity, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        } else {
            validateNavigation()
        }

    }

    private fun validateNavigation() {
        var bundle = Bundle()
        bundle.putString("purpose", purpose)
        bundle.putString("telephone", telephone)
        bundle.putString("date", date)
        bundle.putString("library", library)
        bundle.putString("room", room)
        bundle.putString("slot", slot)
        bundle.putString("participants",occupants)
        findNavController().navigate(
            R.id.action_roomFormFragment_to_roomPreviewFragment,
            bundle
        )
    }


}