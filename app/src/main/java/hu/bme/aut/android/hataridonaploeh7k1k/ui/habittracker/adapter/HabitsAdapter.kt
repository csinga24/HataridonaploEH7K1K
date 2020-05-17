package hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Habit
import kotlinx.android.synthetic.main.card_habit.view.*

class HabitsAdapter(private val context: Context?) : RecyclerView.Adapter<HabitsAdapter.ViewHolder>()  {

    private val habitsList: MutableList<Habit> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_habit, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val habitTitle: TextView = itemView.habitTitle
        val habitReady: CheckBox = itemView.habitReady

        var habit: Habit? = null;
    }

    override fun getItemCount() = habitsList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpHabits = habitsList[position]
        holder.habitTitle.text = tmpHabits.title
        holder.habitReady.isChecked = tmpHabits.ready
        holder.habit = tmpHabits;
    }

    fun addHabit(habit: Habit?) {
        habit ?: return
        habitsList.add(habit)
        notifyDataSetChanged()
    }

    fun deleteHabit(position: Int) {
        habitsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getHabit(position: Int): Habit {
        return habitsList.get(position)
    }

    fun isReady(position: Int){
        habitsList[position].ready = !habitsList[position].ready
        notifyDataSetChanged()
    }

    fun setDate(newDate: String, position: Int){
        habitsList[position].date = newDate
        notifyDataSetChanged()
    }

}
