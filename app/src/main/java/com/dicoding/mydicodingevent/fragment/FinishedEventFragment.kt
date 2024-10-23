package com.dicoding.mydicodingevent.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mydicodingevent.databinding.FragmentFinishedEventBinding
import com.dicoding.mydicodingevent.ui.EventAdapter
import com.dicoding.mydicodingevent.ui.MainViewModel

class FinishedEventFragment : Fragment() {

    private lateinit var binding: FragmentFinishedEventBinding
    private lateinit var eventAdapter: EventAdapter
    private val viewModel: MainViewModel by viewModels()

    companion object {
        private const val TAG = "FinishedEventFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.findListEventsItem("0")

        viewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                eventAdapter.submitList(events)
            } else {
                Log.e(TAG, "Failed to load event data")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { isLoading ->
            showLoading(isLoading)
        }

        binding.rvEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
            val itemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            addItemDecoration(itemDecoration)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
