package hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import hu.bme.aut.android.hataridonaploeh7k1k.R

class HabitTrackerFragment : Fragment() {

    private lateinit var habitTrackerViewModel: HabitTrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        habitTrackerViewModel =
            ViewModelProviders.of(this).get(HabitTrackerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_habittracker, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        habitTrackerViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
