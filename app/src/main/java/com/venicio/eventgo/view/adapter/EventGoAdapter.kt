package com.venicio.eventgo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.venicio.eventgo.R
import com.venicio.eventgo.data.model.EventModel
import com.venicio.eventgo.databinding.EventItemBinding
import java.text.SimpleDateFormat
import java.util.*

class EventGoAdapter(
    private val listEvent: List<EventModel>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<EventGoAdapter.EventVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(inflater, parent, false)

        return EventVH(binding)
    }

    override fun onBindViewHolder(holder: EventVH, position: Int) {

        val item = listEvent[position]
        holder.bindEvent(listEvent[position])

        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }
    }

    override fun getItemCount(): Int = listEvent.size

    inner class EventVH(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindEvent(event: EventModel) {
            binding.tvTitleEvent.text = event.title


            Glide.with(binding.ivEventBg)
                .load(event.image)
                .error(R.drawable.not_load_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivEventBg)

            val timestamp = event.date
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp

            val year = calendar.get(Calendar.YEAR)
            val month = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            binding.tvDay.text = dayOfMonth.toString()
            binding.tvMonth.text = month.toString()
            binding.tvYear.text = year.toString()

        }
    }


}
