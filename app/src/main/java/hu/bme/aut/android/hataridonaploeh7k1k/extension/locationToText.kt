package hu.bme.aut.android.hataridonaploeh7k1k.extension

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun LatLng.locationToText(context: Context): String {
    val gc = Geocoder(context, Locale.getDefault())
    val locations: List<Address>? = gc.getFromLocation(this.latitude, this.longitude, 3)
    return locations!![0].getAddressLine(0)
}