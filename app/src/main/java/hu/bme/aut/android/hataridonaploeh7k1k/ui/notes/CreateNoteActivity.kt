package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Note
import kotlinx.android.synthetic.main.activity_create_note.*
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty

class CreateNoteActivity : AppCompatActivity() {

    var user: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        button_add_new_note.setOnClickListener {sendClick()}
    }

    private fun validateForm() = note_title.validateNonEmpty() && note_desc.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        val key = FirebaseDatabase.getInstance().reference.child("notes").push().key ?: return
        val newNote = Note(user?.uid, note_title.text.toString(), Note.Priority.LOW, note_desc.text.toString())
        FirebaseDatabase.getInstance().reference
            .child("notes")
            .child(key)
            .setValue(newNote)
            .addOnCompleteListener {
                Toast.makeText(this, "Új jegyzet hozzáadva", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

}
