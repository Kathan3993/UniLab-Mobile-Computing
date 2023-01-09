package com.example.mobilecomputingproject.ui.library


import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentLibraryListBinding
import com.example.mobilecomputingproject.models.LibraryDetails
import com.example.mobilecomputingproject.adapters.LibraryAdapter
import com.google.firebase.firestore.*

class LibraryListFragment : Fragment() {

    private var _binding: FragmentLibraryListBinding? = null
    private val binding get() = _binding!!

    lateinit var libraryListRecyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private lateinit var libraryList:ArrayList<LibraryDetails>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        libraryListRecyclerView=view.findViewById(R.id.recyclerViewLibraryList)
        libraryListRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        libraryListRecyclerView.setHasFixedSize(true)
        libraryList= arrayListOf<LibraryDetails>()

        var database = FirebaseFirestore.getInstance()
        database.collection("library").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                for(dc : DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type == DocumentChange.Type.ADDED){
                        //Log.d(TAG,"Document id ----------> ${dc.document.id}")
                        libraryList.add(dc.document.toObject(LibraryDetails::class.java))
                    }
                }
                val adaptor = LibraryAdapter(libraryList, requireContext())
                libraryListRecyclerView.adapter=adaptor

                adaptor.setOnItemClickListner(object : LibraryAdapter.onItemClickListner{
                    override fun onItemClick(position: Int) {
                         findNavController().navigate(R.id.action_libraryListFragment_to_libraryMapFragment, Bundle().apply {
                            putString("libraryId",libraryList[position].libId) })

                    }
                })
            }


        })

        }
}