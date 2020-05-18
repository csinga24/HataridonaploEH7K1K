package hu.bme.aut.android.hataridonaploeh7k1k.extension

import android.content.Context
import android.widget.Toast

fun String.showText(context: Context?){
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}