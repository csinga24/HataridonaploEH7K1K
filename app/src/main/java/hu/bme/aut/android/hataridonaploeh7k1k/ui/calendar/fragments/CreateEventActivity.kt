package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.locationToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*


class CreateEventActivity : AppCompatActivity(), DatePickerDialogFragment.DateListener {

    var user: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    private val MAP_REQUEST = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        btnAddNewEvent.setOnClickListener { sendClick() }

        event_date.text = "  -  "
        event_date.setOnClickListener { showDatePickerDialog() }

        event_time.text = "  -  "
        event_time.setOnClickListener { showTimePickerDialog() }

        event_location.text = "  -  "
        event_location.setOnClickListener { pickPointOnMap() }
    }

    private fun validateForm() = event_title.validateNonEmpty() && event_desc.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        val key = FirebaseDatabase.getInstance().reference.child("events").push().key ?: return
        val newEvent =
            Event(key, user?.uid, event_title.text.toString(), event_location.text.toString(), event_date.text.toString(), event_time.text.toString(), event_desc.text.toString())
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
        datePicker.show(supportFragmentManager, "Date")
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePickerDialog() {
        val timePickerListener = OnTimeSetListener { view, hour, minute ->
            event_time.text = "$hour:$minute"
        }
        val c = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this, timePickerListener,
            c[Calendar.HOUR_OF_DAY], c[Calendar.MINUTE] + 5, true
        )
        timePickerDialog.show()
    }

    override fun onDateSelected(date: Calendar) {
        event_date.text = date.dateToText()
    }

    private fun pickPointOnMap() {
        val pickPointIntent = Intent(this, MapsActivity::class.java)
        startActivityForResult(pickPointIntent, MAP_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MAP_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val latLng: LatLng = data?.getParcelableExtra<Parcelable>("picked_point") as LatLng
                event_location.text = latLng.locationToText(this)
            }
        }
    }
}
