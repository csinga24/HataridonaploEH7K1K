package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import hu.bme.aut.android.hataridonaploeh7k1k.R

class CalendarFragment : Fragment(){
        
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_calendar, container, false)
        view?.let { initialView(it) }
        return view
    }

    fun initialView(view: View) {
        val text:TextView = view.findViewById(R.id.text_calendar_view)
        val button_cal:Button = view.findViewById(R.id.button_cal)
        button_cal.setOnClickListener { text.text = "Cal" }
        val button_list:Button = view.findViewById(R.id.button_list)
        button_list.setOnClickListener { text.text = "List" }
    }
}