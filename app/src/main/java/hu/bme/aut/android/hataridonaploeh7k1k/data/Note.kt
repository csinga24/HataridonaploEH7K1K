package hu.bme.aut.android.hataridonaploeh7k1k.data

class Note (
    var key: String?,
    var uid: String?,
    var title: String,
    var priority: Priority,
    var description: String?,
    var imageUrl: String?
    ): Comparable<Note> {

    constructor() : this(null, null, "", Priority.LOW, null, null)

    override fun compareTo(other: Note): Int {
        if(priority == other.priority) {
            return 0
        }
        else if(priority == Priority.HIGH && other.priority != Priority.HIGH){
            return -1
        }
        else if(priority == Priority.LOW && other.priority != Priority.LOW){
            return 1
        }
        else if(priority == Priority.MEDIUM){
            if(other.priority == Priority.LOW){
                return -1

            }else if(other.priority == Priority.HIGH){
                return 1
            }
        }
        return 0
    }

    enum class Priority {
        LOW, MEDIUM, HIGH
    }
}