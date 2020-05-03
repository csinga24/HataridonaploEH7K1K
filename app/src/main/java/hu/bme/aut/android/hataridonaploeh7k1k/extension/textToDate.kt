@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package hu.bme.aut.android.hataridonaploeh7k1k.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.textToDate(): Calendar {

    val cal = Calendar.getInstance ();
    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy.mm.dd", Locale.ENGLISH);
    cal.time = sdf.parse(this);
    return cal
}