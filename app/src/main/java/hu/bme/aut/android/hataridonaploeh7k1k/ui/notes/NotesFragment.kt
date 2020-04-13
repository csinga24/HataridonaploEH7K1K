package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Note
import hu.bme.aut.android.hataridonaploeh7k1k.ui.notes.adapter.NotesAdapter
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_notes, container, false)
        view?.let { initialView(it) }
        return view
    }

    fun initialView(view: View){
        val add_button : Button = view.findViewById<Button>(R.id.button_add_note)
        add_button.setOnClickListener {
            val createNoteIntent = Intent(context, CreateNoteActivity::class.java)
            startActivity(createNoteIntent)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rvNotes)

        notesAdapter = NotesAdapter(activity?.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(activity).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        recyclerView.adapter = notesAdapter

        initNotesListener()
    }

    private fun initNotesListener() {
        FirebaseDatabase.getInstance()
            .getReference("notes")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newNote = dataSnapshot.getValue<Note>(Note::class.java)
                    notesAdapter.addNote(newNote)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }
}
