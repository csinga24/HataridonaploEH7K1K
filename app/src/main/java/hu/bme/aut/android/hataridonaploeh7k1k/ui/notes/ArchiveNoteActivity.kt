package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
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

class ArchiveNoteActivity: AppCompatActivity() {

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_notes)

        val delAllButton = findViewById<Button>(R.id.button_delete_all)
        delAllButton.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("archive").removeValue()
            notesAdapter.deleteAll()
        }

        val backButton = findViewById<Button>(R.id.back_to_notes)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val recyclerView: RecyclerView = findViewById(R.id.rvNotesArchive)
        recyclerView.addOnItemTouchListener(
            RecyclerViewItemClickListener(this, recyclerView,
                object : RecyclerViewItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) { }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val popup = PopupMenu(view!!.context, view)
                        popup.inflate(R.menu.menu_archive)
                        popup.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.adelete -> {
                                    //delete from archive
                                    FirebaseDatabase.getInstance().reference.child("archive")
                                        .child(notesAdapter.getNote(position).key.toString())
                                        .removeValue()
                                    notesAdapter.deleteNote(position)
                                    return@setOnMenuItemClickListener true
                                }
                                R.id.renew -> {
                                    //add to notes
                                    FirebaseDatabase.getInstance().reference.child("notes")
                                        .child(notesAdapter.getNote(position).key.toString())
                                        .setValue(notesAdapter.getNote(position))

                                    //delete from archive
                                    FirebaseDatabase.getInstance().reference.child("archive")
                                        .child(notesAdapter.getNote(position).key.toString())
                                        .removeValue()
                                    notesAdapter.deleteNote(position)
                                    return@setOnMenuItemClickListener true
                                }
                            }
                            false
                        }
                        popup.show()
                    }
                })
        )

        notesAdapter = NotesAdapter(this.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        recyclerView.adapter = notesAdapter

        initNotesListener()
    }

    private fun initNotesListener() {
        FirebaseDatabase.getInstance()
            .getReference("archive")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newNote = dataSnapshot.getValue<Note>(Note::class.java)
                    notesAdapter.addNote(newNote)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    "Jegyzet törölve!".showText(this@ArchiveNoteActivity)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}