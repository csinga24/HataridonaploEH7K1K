package hu.bme.aut.android.hataridonaploeh7k1k.data

import android.location.Location
import java.util.*

class Event (
    var uid: String?,
    var title: String,
    var location: String?,
    var date: String?,
    var description: String
) {
    constructor() : this(null, "", null, null, "")
}