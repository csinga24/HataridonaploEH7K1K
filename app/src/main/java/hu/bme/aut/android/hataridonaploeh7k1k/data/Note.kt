package hu.bme.aut.android.hataridonaploeh7k1k.data


class Note (
    var title: String,
    var priority: Priority,
    var description: String
    ) {

    enum class Priority {
        LOW, MEDIUM, HIGH
    }
}