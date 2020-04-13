package hu.bme.aut.android.hataridonaploeh7k1k.data

import com.google.firebase.auth.FirebaseUser


class Note (
    var uid: String?,
    var title: String,
    var priority: Priority,
    var description: String
    ) {

    constructor() : this(null, "", Priority.LOW, "")

    enum class Priority {
        LOW, MEDIUM, HIGH
    }
}