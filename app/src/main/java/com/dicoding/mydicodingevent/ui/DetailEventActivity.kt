@file:Suppress("DEPRECATION")

package com.dicoding.mydicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.mydicodingevent.R
import com.dicoding.mydicodingevent.data.Result
import com.dicoding.mydicodingevent.data.local.entity.EventEntity
import com.dicoding.mydicodingevent.data.response.ListEventsItem
import com.dicoding.mydicodingevent.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private val itemDataList: String = "event_data_list"
    private lateinit var database: EventEntity
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        val eventItem: ListEventsItem? = intent.getParcelableExtra(itemDataList)

        if (eventItem != null) {
            showLoading(true)
            viewModel.fetchEventDetail(eventItem.id.toString())
            Log.d("DetailEventActivity", "Event data: $eventItem") // log untuk lihat data
            database = EventEntity(
                id = eventItem.id.toString(),
                name = eventItem.name,
                mediaCover = eventItem.mediaCover,
                isBookmarked = true
            )

        } else {
            handleError()
        }

        binding.btnOpenLink.setOnClickListener {
            eventItem?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                startActivity(intent)
            }
        }

        val ivBookmark = binding.fabFavorite
        ivBookmark.setOnClickListener {

            updateBookmarkIcon(database)
        }

        viewModel.eventDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    result.data.let { event ->
                        binding.tvEventName.text = event.name
                        binding.tvEventOrganizer.text =
                            getString(R.string.event_organizer, event.ownerName)
                        binding.tvEventTime.text = getString(R.string.event_time, event.beginTime)
                        binding.tvEventQuota.text =
                            getString(R.string.event_quota, event.quota - event.registrants)
                        binding.tvEventDescription.text = getString(
                            R.string.event_description,
                            HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        )

                        viewModel.getBookmark(event.id.toString()).observe(this) { eventEntity ->
                            if (eventEntity != null) {
                                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                            } else {
                                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                            }
                        }
                        Glide.with(this)
                            .load(event.mediaCover)
                            .into(binding.imgEventCover)
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateBookmarkIcon(event: EventEntity) {
        viewModel.getBookmark(event.id).observeOnce(this) { eventEntity ->
            if (eventEntity != null) {
                viewModel.deleteEvent(event)
                Toast.makeText(
                    this@DetailEventActivity,
                    "Delete Bookmark Event Success",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.saveEvent(event)
                Toast.makeText(
                    this@DetailEventActivity,
                    "Save Bookmark Event Success",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
        val wrapper = object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        }
        observe(owner, wrapper)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleError() {
        val message = "Data Is Missing !"
        showLoading(false)
        binding.tvEventDescription.text = message
        binding.btnOpenLink.isEnabled = false
    }


}