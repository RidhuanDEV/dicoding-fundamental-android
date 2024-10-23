package com.dicoding.mydicodingevent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mydicodingevent.data.response.ListEventsItem
import com.dicoding.mydicodingevent.databinding.FragmentEventBinding
import com.dicoding.mydicodingevent.ui.EventAdapter
import com.dicoding.mydicodingevent.ui.EventViewModel
import com.dicoding.mydicodingevent.ui.ViewModelFactory

class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding
    private lateinit var bookmarkAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels {
            factory
        }

        bookmarkAdapter = EventAdapter { isLoading ->
            viewModel.setLoading(isLoading)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.getBookmarkedEvent().observe(viewLifecycleOwner) { users ->
            viewModel.setLoading(false)
            if (users != null) {
                val items = arrayListOf<ListEventsItem>()
                users.map {
                    val item = ListEventsItem(
                        id = it.id.toInt(),
                        name = it.name,
                        mediaCover = it.mediaCover.toString(),
                    )
                    items.add(item)
                }
                bookmarkAdapter.submitList(items)
            }

        }

        binding?.rvEvent?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = bookmarkAdapter
        }

        viewModel.setLoading(true)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
