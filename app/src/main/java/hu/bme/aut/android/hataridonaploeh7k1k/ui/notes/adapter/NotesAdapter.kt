package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Note
import kotlinx.android.synthetic.main.card_note.view.*
import com.bumptech.glide.Glide

class NotesAdapter(private val context: Context?) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val notesList: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_note, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
        val tvDesc: TextView = itemView.tvDescription
        val imgNote: ImageView = itemView.imgNote
        val tvPriority: TextView = itemView.tvPriority
        val view: View = itemView

        var note: Note? = null;
    }

    override fun getItemCount() = notesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpNotes = notesList[position]
        holder.tvTitle.text = tmpNotes.title
        holder.tvDesc.text = tmpNotes.description
        holder.note = tmpNotes;

        if (tmpNotes.imageUrl.isNullOrBlank()) {
            holder.imgNote.visibility = View.GONE
        } else {
            context?.let { Glide.with(it).load(tmpNotes.imageUrl).into(holder.imgNote) }
            holder.imgNote.visibility = View.VISIBLE
        }
        when (tmpNotes.priority) {
            Note.Priority.LOW -> {
                holder.tvPriority.text = "Nem Fontos"
                holder.view.setBackgroundColor(Color.parseColor("#F1E263")) //<color name="colorLow">#F1E263</color>
            }
            Note.Priority.MEDIUM -> {
                holder.tvPriority.text = "Átlagos"
                holder.view.setBackgroundColor(Color.parseColor("#EF734C")) //<color name="colorMedium">#EF734C</color>
            }
            Note.Priority.HIGH -> {
                holder.tvPriority.text = "Fontos"
                holder.view.setBackgroundColor(Color.parseColor("#EF3232")) //<color name="colorHigh">#EF3232</color>
            }
        }
    }

    fun addNote(note: Note?) {
        note ?: return
        notesList.add(note)
        notifyDataSetChanged()
    }

    fun deleteNote(position: Int) {
        notesList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getNote(position: Int): Note{
        return notesList.get(position)
    }

}