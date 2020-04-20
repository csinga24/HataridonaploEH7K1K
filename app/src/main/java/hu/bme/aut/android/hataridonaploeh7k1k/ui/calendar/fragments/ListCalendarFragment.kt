package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.adapter.EventsAdapter

class ListCalendarFragment : Fragment() {

    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_calendarlist, container, false)
        view?.let { initialView(it) }
        return view
    }

    fun initialView(view: View) {
        val add_button: Button = view.findViewById<Button>(R.id.button_add_event)
        add_button.setOnClickListener {
            val createEventIntent = Intent(context, CreateEventActivity::class.java)
            startActivity(createEventIntent)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rvEvents)

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
                    eventsAdapter.addEvent(newEvent)
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
