package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import kotlinx.android.synthetic.main.activity_create_note.*
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_event.*

class CreateEventActivity : AppCompatActivity() {

    var user: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        button_add_new_event.setOnClickListener {sendClick()}
    }

    private fun validateForm() = event_title.validateNonEmpty() && event_desc.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        val key = FirebaseDatabase.getInstance().reference.child("events").push().key ?: return
        val newEvent = Event(user?.uid, event_title.text.toString(),null, null, event_desc.text.toString())
        FirebaseDatabase.getInstance().reference
            .child("events")
            .child(key)
            .setValue(newEvent)
            .addOnCompleteListener {
                Toast.makeText(this, "Új esemény hozzáadva", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

}
