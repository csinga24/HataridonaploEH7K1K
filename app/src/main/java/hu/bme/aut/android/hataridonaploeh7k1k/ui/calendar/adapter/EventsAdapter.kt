package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import kotlinx.android.synthetic.main.card_event.view.*

class EventsAdapter(private val context: Context?) : RecyclerView.Adapter<EventsAdapter.ViewHolder>()  {

    private val eventsList: MutableList<Event> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
        val tvDesc: TextView = itemView.tvDescription
        val tvDate: TextView = itemView.tvDate
        val tvTime: TextView = itemView.tvTime
        val tvLocation: TextView = itemView.tvLocation

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = eventsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpEvent = eventsList[position]
        holder.tvTitle.text = tmpEvent.title
        holder.tvDesc.text = tmpEvent.description
        holder.tvDate.text = tmpEvent.date
        holder.tvTime.text = tmpEvent.time
        holder.tvLocation.text =tmpEvent.location
    }

    fun addEvent(event: Event?){
        event ?: return
        eventsList.add(event)
        notifyDataSetChanged()
    }

    fun deleteEvent(position: Int){
        eventsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getEvent(position: Int): Event{
        return eventsList[position]
    }

    fun deleteAll(){
        eventsList.clear()
        notifyDataSetChanged();
    }
}