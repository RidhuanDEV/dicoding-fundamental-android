package com.dicoding.mydicodingevent.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mydicodingevent.data.response.ListEventsItem
import com.dicoding.mydicodingevent.databinding.ItemEventBinding

class EventAdapter(

    private val onLoading: (Boolean) -> Unit
) : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem, onLoading: (Boolean) -> Unit) {
            binding.tvItemName.text = event.name
            Glide.with(binding.imgItemPhoto.context)
                .load(event.mediaCover)
                .into(binding.imgItemPhoto)

            val itemDataList = "event_data_list"
            binding.cardView.setOnClickListener {
                onLoading(true)
                val context = itemView.context
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra(itemDataList, event)
                context.startActivity(intent)
                onLoading(false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), onLoading)
    }


}