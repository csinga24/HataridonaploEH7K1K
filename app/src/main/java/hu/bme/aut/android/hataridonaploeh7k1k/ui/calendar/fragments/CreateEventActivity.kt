package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.locationToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*


class CreateEventActivity : AppCompatActivity() {

    private var user: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    private var readyText = "Új esemény hozzáadva!"
    private var keyOfModifiedEvent: String? = null

    private val MAP_REQUEST = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        btnAddNewEvent.setOnClickListener { sendClick() }

        event_date.text = Calendar.getInstance().dateToText()
        event_date.setOnClickListener { showDatePickerDialog() }

        event_time.text = "  -  "
        event_time.setOnClickListener { showTimePickerDialog() }

        event_location.text = "  -  "
        event_location.setOnClickListener { pickPointOnMap() }

        if(intent.getStringExtra("event title") != null) {
            getExtras(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getExtras(intent: Intent){
        keyOfModifiedEvent = intent.getStringExtra("event key")
        val title: String? = intent.getStringExtra("event title")
        event_title.text = Editable.Factory.getInstance().newEditable(title)
        val desc: String? = intent.getStringExtra("event desc")
        event_desc.text = Editable.Factory.getInstance().newEditable(desc)

        val date: String? = intent.getStringExtra("event date")
        if(date != null){
            event_date.text = date
        }

        val time: String? = intent.getStringExtra("event time")
        if(time != null){
            event_time.text = time
        }

        val location: String? = intent.getStringExtra("event location")
        if(location != null){
            event_location.text = location
        }

        tvAddEvent.text = "Esemény módosítása"
        btnAddNewEvent.text = "Módosítás!"
        readyText = "Esemény módosítva!"
    }

    private fun validateForm() = event_title.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        if(keyOfModifiedEvent == null) {
            val key = FirebaseDatabase.getInstance().reference.child("events").push().key ?: return
            val newEvent =
                Event(key, user?.uid, event_title.text.toString(), event_location.text.toString(),
                    event_date.text.toString(), event_time.text.toString(), event_desc.text.toString()
                )
            FirebaseDatabase.getInstance().reference
                .child("events")
                .child(key)
                .setValue(newEvent)
                .addOnCompleteListener {
                    readyText.showText(this)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
        }
        else{
            val modifiedEvent =
                Event(keyOfModifiedEvent, user?.uid, event_title.text.toString(), event_location.text.toString(),
                    event_date.text.toString(), event_time.text.toString(), event_desc.text.toString()
                )
            FirebaseDatabase.getInstance().reference
                .child("events")
                .child(keyOfModifiedEvent!!)
                .setValue(modifiedEvent)
                .addOnCompleteListener {
                    readyText.showText(this)
                    finish()
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val datePickerListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val monthReal = month + 1
                event_date.text = "$year.$monthReal.$dayOfMonth"
            }
        val c = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this, datePickerListener,
            c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
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

    override fun onBackPressed() {
        if(keyOfModifiedEvent != null) {
            sendClick()
        }
        else{
            super.onBackPressed()
        }
    }
}
