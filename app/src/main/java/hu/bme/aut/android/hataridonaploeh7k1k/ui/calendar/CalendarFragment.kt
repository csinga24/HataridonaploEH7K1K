package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.R.layout
import hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments.CalCalendarFragment
import hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments.ListCalendarFragment

class CalendarFragment : Fragment(){
        
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(layout.fragment_calendar, container, false)
        view?.let { initialView(it) }
        return view
    }

    fun initialView(view: View) {
        swapFragmnet(CalCalendarFragment())

        val button_cal:Button = view.findViewById(R.id.button_cal)
        button_cal.setOnClickListener {
            val newFragment = CalCalendarFragment()
            swapFragmnet(newFragment)
        }
        val button_list:Button = view.findViewById(R.id.button_list)
        button_list.setOnClickListener {
            val newFragment = ListCalendarFragment()
            swapFragmnet(newFragment)
        }
    }

    private fun swapFragmnet(newFragment: Fragment) {
        val fragmentTransaction =
            activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, newFragment)
        fragmentTransaction.commit()
    }
}