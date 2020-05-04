package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Note
import hu.bme.aut.android.hataridonaploeh7k1k.extension.RecyclerViewItemClickListener
import hu.bme.aut.android.hataridonaploeh7k1k.ui.notes.adapter.NotesAdapter
import kotlinx.android.synthetic.main.activity_create_note.*

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

    private fun initialView(view: View){
        val addButton= view.findViewById<Button>(R.id.button_add_note)
        addButton.setOnClickListener {
            val createNoteIntent = Intent(context, CreateNoteActivity::class.java)
            startActivity(createNoteIntent)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rvNotes)
        recyclerView.addOnItemTouchListener(
            RecyclerViewItemClickListener(context, recyclerView, object : RecyclerViewItemClickListener.OnItemClickListener{
                override fun onItemClick(view: View?, position: Int) {}

                override fun onLongItemClick(view: View?, position: Int) {
                    val popup = PopupMenu(view!!.context, view)
                    popup.inflate(R.menu.menu_note)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                FirebaseDatabase.getInstance().reference.child("notes").child(notesAdapter.getNote(position).key.toString()).removeValue()
                                notesAdapter.deleteNote(position)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.modify -> {
                                val modifyNoteIntent = Intent(context, CreateNoteActivity::class.java)
                                modifyNoteIntent.putExtra("note title",  notesAdapter.getNote(position).title)
                                modifyNoteIntent.putExtra("note priority",  notesAdapter.getNote(position).priority.name)
                                modifyNoteIntent.putExtra("note desc",  notesAdapter.getNote(position).description)
                                modifyNoteIntent.putExtra("note img",  notesAdapter.getNote(position).imageUrl)
                                FirebaseDatabase.getInstance().reference.child("notes").child(notesAdapter.getNote(position).key.toString()).setValue(null)
                                notesAdapter.deleteNote(position)  //TODO
                                startActivity(modifyNoteIntent)
                                return@setOnMenuItemClickListener true
                            }
                        }
                        false
                    }
                    popup.show()
                }
            })
        )

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
                    val changedNote = dataSnapshot.getValue<Note>(Note::class.java)
                    Toast.makeText(context, changedNote!!.title + "megváltozott", Toast.LENGTH_SHORT).show()
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val removedNote = dataSnapshot.getValue<Note>(Note::class.java)
                    Toast.makeText(context, removedNote!!.title + "törölve", Toast.LENGTH_SHORT).show()
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

}
