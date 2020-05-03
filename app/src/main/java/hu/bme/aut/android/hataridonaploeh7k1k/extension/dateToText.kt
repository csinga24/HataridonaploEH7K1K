package hu.bme.aut.android.hataridonaploeh7k1k.extension

import java.util.*


    fun Calendar.dateToText(): String {

        val dateString = StringBuilder()
        dateString.append(this.get(Calendar.YEAR))
        dateString.append(".")
        dateString.append(this.get(Calendar.MONTH) + 1)
        dateString.append(".")
        dateString.append(this.get(Calendar.DAY_OF_MONTH))

        return dateString.toString()
    }


