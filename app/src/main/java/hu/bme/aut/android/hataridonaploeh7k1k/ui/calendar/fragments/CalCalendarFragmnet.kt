package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import hu.bme.aut.android.hataridonaploeh7k1k.R


class CalCalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_calendarcal, container, false)
        view?.let { initView(it) }
        return view
    }

    fun initView(view: View){
        val simpleCalendarView =
            view.findViewById(R.id.CalendarView) as CalendarView

    }
}