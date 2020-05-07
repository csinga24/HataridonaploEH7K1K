package hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import hu.bme.aut.android.hataridonaploeh7k1k.R


class HabitTrackerFragment : Fragment() { //TODO

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_habittracker, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        textView.text = "Habbit Tracker \n Comming Soon...."

        return root
    }
}
