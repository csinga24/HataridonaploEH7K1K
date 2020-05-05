package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.adapter.EventsAdapter
import java.util.*


class CalCalendarFragment : Fragment() {

    private var calendarView: CalendarView? = null
    private lateinit var eventsAdapter: EventsAdapter

    private var selectedDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_calendarcal, container, false)
        view?.let { initView(it) }
        return view
    }

    fun initView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvEventsInCal)
        selectedDate.time = Date(System.currentTimeMillis())

        calendarView = view.findViewById(R.id.CalendarView) as CalendarView
        calendarView!!.setOnDateChangeListener(CalendarView.OnDateChangeListener {
                calendarView: CalendarView, year: Int, month: Int, day: Int ->
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, day)

            eventsAdapter.deleteAll()
            initEventsListener()
        })

        eventsAdapter = EventsAdapter(activity?.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(activity).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        recyclerView.adapter = eventsAdapter
        initEventsListener()
    }

    private fun initEventsListener() {
        FirebaseDatabase.getInstance()
            .getReference("events")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newEvent = dataSnapshot.getValue<Event>(Event::class.java)
                    if(newEvent!!.date == selectedDate.dateToText()) {
                        eventsAdapter.addEvent(newEvent)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

}