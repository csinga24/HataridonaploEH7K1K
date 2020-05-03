package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import kotlinx.android.synthetic.main.activity_create_note.*
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*

class CreateEventActivity : AppCompatActivity(), DatePickerDialogFragment.DateListener {

    var user: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        button_add_new_event.setOnClickListener { sendClick() }

        event_date.text = "  -  "
        event_date.setOnClickListener { showDatePickerDialog() }
    }

    private fun validateForm() = event_title.validateNonEmpty() && event_desc.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        val key = FirebaseDatabase.getInstance().reference.child("events").push().key ?: return
        val newEvent =
            Event(user?.uid, event_title.text.toString(), null, event_date.text.toString(), event_desc.text.toString())
        FirebaseDatabase.getInstance().reference
            .child("events")
            .child(key)
            .setValue(newEvent)
            .addOnCompleteListener {
                Toast.makeText(this, "Új esemény hozzáadva", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialogFragment()
        datePicker.show(supportFragmentManager, "TAG")
    }

    override fun onDateSelected(date: Calendar) {
        event_date.text = date.dateToText()
    }
}
