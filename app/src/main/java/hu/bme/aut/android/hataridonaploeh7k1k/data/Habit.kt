package hu.bme.aut.android.hataridonaploeh7k1k.data

class Habit (
    var key: String?,
    var uid: String?,
    var title: String,
    var date: String?,
    var ready: Boolean
    ) {
    constructor() : this(null, null, "", null, false)
}