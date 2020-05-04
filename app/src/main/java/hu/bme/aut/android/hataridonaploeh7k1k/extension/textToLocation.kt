package hu.bme.aut.android.hataridonaploeh7k1k.extension

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun String.textToLocation(context: Context): LatLng{
    val gc = Geocoder(context, Locale.ENGLISH)
    val locations: List<Address>? = gc.getFromLocationName(this, 3)
    val lat = locations!![0].latitude
    val long = locations[0].longitude
    return LatLng(lat, long)
}