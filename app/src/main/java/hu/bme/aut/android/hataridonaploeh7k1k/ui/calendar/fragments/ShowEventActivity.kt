package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.textToDate
import hu.bme.aut.android.hataridonaploeh7k1k.extension.textToLocation
import kotlinx.android.synthetic.main.activity_show_event.*
import java.util.*

class ShowEventActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_event)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val title: String? = intent.getStringExtra("event title")
        tvEventTitle.text = title
        val date: String? = intent.getStringExtra("event date")
        tvEventDate.text = date
        val time: String? = intent.getStringExtra("event time")
        tvEventTime.text = time
        val desc: String? = intent.getStringExtra("event desc")
        tvEventDesc.text = desc

        countDown(date!!, time!!)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location: String? = intent.getStringExtra("event location")
        if(location != "  -  ") {
            val eventLocation = location!!.textToLocation(this)
            mMap.addMarker(MarkerOptions().position(eventLocation).title("Helyszín"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation))
        }
    }

    private fun countDown(date: String, time: String) {
        var message: String? = "Az esemény lezajlott vagy éppen zajlik."
        val now = Calendar.getInstance()
        if (date == now.dateToText()) {
            if(time.contains('-')){
                message = "Az esemény ma van."
            }
            else {
                val timeList = time.split(':')
                val hour = timeList[0].toInt()
                val minute = timeList[1].toInt()
                if (hour == now[Calendar.HOUR_OF_DAY]) {
                    if (minute > now[Calendar.MINUTE]) {
                        val min = minute - now[Calendar.MINUTE]
                        message = "Az esemény $min perc múlva kezdődik."
                    }
                } else if (hour > now[Calendar.HOUR_OF_DAY]) {
                    val h = hour - now[Calendar.HOUR_OF_DAY]
                    message = "Az esemény $h óra múlva kezdődik."
                }
            }
        }
        else if(date.textToDate()[Calendar.YEAR] == now[Calendar.YEAR]) {
            if (date.textToDate()[Calendar.MONTH] == now[Calendar.MONTH]) {
                if (date.textToDate()[Calendar.DATE] > now[Calendar.DATE]) {
                    val day = date.textToDate()[Calendar.DATE] - now[Calendar.DATE]
                    message = "Az esemény $day nap múlva kezdődik."
                }
            } else if (date.textToDate()[Calendar.MONTH] > now[Calendar.MONTH]) {
                val month = date.textToDate()[Calendar.MONTH] - now[Calendar.MONTH]
                message = "Az esemény $month hónap múlva kezdődik."
            }
        }
        else if(date.textToDate()[Calendar.YEAR] > now[Calendar.YEAR]){
                val year = date.textToDate()[Calendar.YEAR] - now[Calendar.YEAR]
                message = "Az esemény $year év múlva kezdődik."
        }
        tvCountDown.text = message
    }
}
