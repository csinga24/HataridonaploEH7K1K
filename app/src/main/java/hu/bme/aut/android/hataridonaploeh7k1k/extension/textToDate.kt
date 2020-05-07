@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package hu.bme.aut.android.hataridonaploeh7k1k.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.textToDate(): Calendar {
    val cal = Calendar.getInstance ();
    val list = this.split('.')
    cal.set(Calendar.YEAR, list[0].toInt())
    cal.set(Calendar.MONTH, list[1].toInt()-1)
    cal.set(Calendar.DAY_OF_MONTH, list[2].toInt())
    return cal
}