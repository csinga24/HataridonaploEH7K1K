package hu.bme.aut.android.hataridonaploeh7k1k.data

import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import java.util.*

class Event (
    var key: String?,
    var uid: String?,
    var title: String,
    var location: String?,
    var date: String,
    var time: String?,
    var description: String?
) {
    constructor() : this(null,null, "", null, Calendar.getInstance().dateToText(), null, null)
}
