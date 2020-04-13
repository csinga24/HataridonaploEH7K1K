package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Note
import kotlinx.android.synthetic.main.card_note.view.*

class NotesAdapter(private val context: Context?) : RecyclerView.Adapter<NotesAdapter.ViewHolder>()  {

    private val notesList: MutableList<Note> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
        val tvDesc: TextView = itemView.tvDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = notesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpNotes = notesList[position]
        holder.tvTitle.text = tmpNotes.title
        holder.tvDesc.text = tmpNotes.description
    }

    fun addNote(note: Note?){
        note ?: return

        notesList.add(note)
        notifyDataSetChanged()
    }
}