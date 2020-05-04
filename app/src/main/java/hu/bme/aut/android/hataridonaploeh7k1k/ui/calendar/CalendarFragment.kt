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

    private fun initialView(view: View) {
        swapFragmnet(CalCalendarFragment())

        val buttonCal:Button = view.findViewById(R.id.button_cal)
        buttonCal.setOnClickListener {
            val newFragment = CalCalendarFragment()
            swapFragmnet(newFragment)
        }
        val buttonList:Button = view.findViewById(R.id.button_list)
        buttonList.setOnClickListener {
            val newFragment = ListCalendarFragment()
            swapFragmnet(newFragment)
        }
    }

    private fun swapFragmnet(newFragment: Fragment) {
        val fragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, newFragment)
        fragmentTransaction.commit()
    }
}