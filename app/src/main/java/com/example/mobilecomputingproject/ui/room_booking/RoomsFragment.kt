package com.example.mobilecomputingproject.ui.room_booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.adapters.AvailableRoomsAdapter
import com.example.mobilecomputingproject.databinding.FragmentRoomsBinding
import com.example.mobilecomputingproject.models.StudyRooms
import com.google.firebase.firestore.FirebaseFirestore


class RoomsFragment : Fragment() {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var _binding: FragmentRoomsBinding? = null
    private val binding get() = _binding!!

    lateinit var recyclerViewAvailableRooms: RecyclerView

    lateinit var adapter: AvailableRoomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAvailableRooms = binding.recyclerViewAvailableRooms

        // Set the layout to Linear Layout
        recyclerViewAvailableRooms.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)

        val availableRoomsList = ArrayList<StudyRooms>()
        var date = requireArguments().get("date").toString()
        var library = requireArguments().get("library").toString()
        var participants = requireArguments().get("participants").toString()
        var roomBooking = resources.getStringArray((R.array.slots))



        db.collection("library_room").whereEqualTo("name",library).get().addOnSuccessListener { result ->
            for (task in result) {
                var room = StudyRooms(
                    task.data?.getValue("room").toString(),
                    task.data?.getValue("occupants").toString(),
                    task.data?.getValue("max_time_limit").toString()
                )
                availableRoomsList.add(room)
            }

            // Initialize the adapter for data binding
            var fragmentManager: FragmentManager? = this.fragmentManager
            adapter = AvailableRoomsAdapter(
                availableRoomsList,
                requireActivity().applicationContext,
                fragmentManager,
                date,
                library,
                participants,
                roomBooking
            )

            recyclerViewAvailableRooms.adapter = adapter

        }


    }

}