package hu.bme.aut.android.hataridonaploeh7k1k.data

import com.google.firebase.auth.FirebaseUser


class Note (
    var uid: String?,
    var title: String,
    var priority: Priority,
    var description: String,
    var imageUrl: String?
    ) {

    constructor() : this(null, "", Priority.LOW, "", null)

    enum class Priority {
        LOW, MEDIUM, HIGH
    }
}