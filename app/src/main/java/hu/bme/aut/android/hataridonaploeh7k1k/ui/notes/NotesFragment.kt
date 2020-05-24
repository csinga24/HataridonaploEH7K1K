package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
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
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.ui.notes.adapter.NotesAdapter
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter
    private var notesFilter: String = ""

    companion object {
        const val REQUEST_NEW_NOTE = 16
        const val REQUEST_MODIFY_NOTE = 18
        const val REQUEST_ARCHIVE = 22
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_notes, container, false)
        view?.let { initialView(it) }
        return view
    }

    private fun initialView(view: View) {
        val addButton = view.findViewById<Button>(R.id.button_add_note)
        addButton.setOnClickListener {
            val createNoteIntent = Intent(context, CreateNoteActivity::class.java)
            startActivityForResult(createNoteIntent, REQUEST_NEW_NOTE)
        }

        notesFilter = "minden"
        val filterText = view.findViewById<TextView>(R.id.text_filter_notes)
        filterText.text = notesFilter

        val filterButton = view.findViewById<Button>(R.id.button_filter_notes)
        filterButton.setOnClickListener {
            chooseFilterNotesDialog()
        }

        val archiveButton = view.findViewById<Button>(R.id.button_archive)
        archiveButton.setOnClickListener {
            val archiveIntent = Intent(context, ArchiveNoteActivity::class.java)
            startActivityForResult(archiveIntent, REQUEST_ARCHIVE)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rvNotes)
        recyclerView.addOnItemTouchListener(
            RecyclerViewItemClickListener(
                context,
                recyclerView,
                object : RecyclerViewItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) { }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val popup = PopupMenu(view!!.context, view)
                        popup.inflate(R.menu.menu_note)
                        popup.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.delete -> {
                                    //add to archive
                                    FirebaseDatabase.getInstance().reference.child("archive")
                                        .child(notesAdapter.getNote(position).key.toString())
                                        .setValue(notesAdapter.getNote(position))

                                    //delete from notes
                                    FirebaseDatabase.getInstance().reference.child("notes")
                                        .child(notesAdapter.getNote(position).key.toString())
                                        .removeValue()
                                    notesAdapter.deleteNote(position)
                                    return@setOnMenuItemClickListener true
                                }
                                R.id.modify -> {
                                    val modifyNoteIntent =
                                        Intent(context, CreateNoteActivity::class.java)
                                    modifyNoteIntent.putExtra("note title", notesAdapter.getNote(position).title)
                                    modifyNoteIntent.putExtra("note priority", notesAdapter.getNote(position).priority.name)
                                    modifyNoteIntent.putExtra("note desc", notesAdapter.getNote(position).description)
                                    modifyNoteIntent.putExtra("note img", notesAdapter.getNote(position).imageUrl)
                                    modifyNoteIntent.putExtra("note key", notesAdapter.getNote(position).key)
                                    notesAdapter.deleteNote(position)
                                    startActivityForResult(modifyNoteIntent, REQUEST_MODIFY_NOTE)
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
                    if (newNote != null) {
                        filteringNotes(newNote)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val changedNote = dataSnapshot.getValue<Note>(Note::class.java)
                    if (changedNote != null) {
                        filteringNotes(changedNote)
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    "Jegyzet törölve!".showText(context)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    fun refreshNotes(){
        notesAdapter.deleteAll()
        initNotesListener()
    }


    private fun chooseFilterNotesDialog(){
        lateinit var dialog: AlertDialog
        val filters = arrayOf("minden","csak fontos","csak átlagos","csak nem fontos")
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Ezekre a jegyzetekre szűrnék: ")
        builder.setSingleChoiceItems(filters,-1) { _, which->
            notesFilter = filters[which]
            text_filter_notes.text = notesFilter
            refreshNotes()
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }


    private fun filteringNotes(note: Note){
        when (notesFilter){
            "minden"-> {
                notesAdapter.addNote(note)
                notesAdapter.sortingByPriority()
            }
            "csak fontos" -> {
                if(note.priority == Note.Priority.HIGH){
                    notesAdapter.addNote(note)
                }
            }
            "csak átlagos" -> {
                if(note.priority == Note.Priority.MEDIUM){
                    notesAdapter.addNote(note)
                }
            }
            "csak nem fontos" -> {
                if(note.priority == Note.Priority.LOW){
                    notesAdapter.addNote(note)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            refreshNotes()
        }
    }


}
