package com.venicio.eventgo.view.ui.framgents

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.venicio.eventgo.R
import com.venicio.eventgo.data.model.EventModel
import com.venicio.eventgo.databinding.FragmentHomeBinding
import com.venicio.eventgo.view.adapter.EventGoAdapter
import com.venicio.eventgo.viewmodel.EventGoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerAdapter: EventGoAdapter
    private val eventGoViewModel: EventGoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        setupObservers()

        return (binding.root)
    }


    private fun setupObservers() {

        eventGoViewModel.progressBar.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
        })

        eventGoViewModel.eventsAll.observe(viewLifecycleOwner, Observer { eventsResponse ->
            if (eventsResponse != null) {
                val listEvent = eventsResponse.toList()
                setupRecyclerEvents(listEvent)
            }
        })

        eventGoViewModel.errorRequest.observe(viewLifecycleOwner, Observer {error ->
            if (error) {
                Toast.makeText(requireContext(), getString(R.string.error_request), Toast.LENGTH_SHORT).show()
            }
            eventGoViewModel.resetError()
        })
    }

    private fun setupRecyclerEvents(listEvent: List<EventModel>) {
        val recycler = binding.recyclerEvents
        recycler.setHasFixedSize(true)

        recyclerAdapter = EventGoAdapter(listEvent) { itemID ->

            val bundle = Bundle()
            bundle.putString("itemId", itemID)

            val fragDetail = EventDetailsFragment()
            fragDetail.arguments = bundle

            val fragManager = requireActivity().supportFragmentManager
            fragManager.beginTransaction()
                .replace(R.id.container_root, fragDetail)
                .addToBackStack(null)
                .commit()
        }

            recycler.adapter = recyclerAdapter
    }

}
