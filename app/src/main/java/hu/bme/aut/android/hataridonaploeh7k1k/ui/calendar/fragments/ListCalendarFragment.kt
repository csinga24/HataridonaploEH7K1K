package hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Event
import hu.bme.aut.android.hataridonaploeh7k1k.extension.RecyclerViewItemClickListener
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.textToDate
import hu.bme.aut.android.hataridonaploeh7k1k.ui.calendar.adapter.EventsAdapter
import kotlinx.android.synthetic.main.fragment_calendarlist.*
import java.util.*

class ListCalendarFragment : Fragment() {

    private lateinit var eventsAdapter: EventsAdapter
    private var eventsFilter: String =  ""

    companion object {
        const val REQUEST_NEW_EVENT = 11
        const val REQUEST_MODIFY_EVENT = 21
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_calendarlist, container, false)
        view?.let { initialView(it) }
        return view
    }

    private fun initialView(view: View) {
        val addButton: Button = view.findViewById<Button>(R.id.button_add_event)
        addButton.setOnClickListener {
            val createEventIntent = Intent(context, CreateEventActivity::class.java)
            startActivityForResult(createEventIntent, REQUEST_NEW_EVENT)
        }

        eventsFilter = "minden"
        val filterText = view.findViewById<TextView>(R.id.text_filter_event)
        filterText.text = eventsFilter

        val filterButton = view.findViewById<Button>(R.id.button_filter_event)
        filterButton.setOnClickListener {
            chooseFilterEventsDialog()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rvEvents)

        eventsAdapter = EventsAdapter(activity?.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(activity).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        recyclerView.adapter = eventsAdapter

        recyclerView.addOnItemTouchListener(RecyclerViewItemClickListener(context, recyclerView, object: RecyclerViewItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val showEventIntent = Intent(context, ShowEventActivity::class.java)
                showEventIntent.putExtra("event title",  eventsAdapter.getEvent(position).title)
                showEventIntent.putExtra("event date",  eventsAdapter.getEvent(position).date)
                showEventIntent.putExtra("event time",  eventsAdapter.getEvent(position).time)
                showEventIntent.putExtra("event desc",  eventsAdapter.getEvent(position).description)
                showEventIntent.putExtra("event location",  eventsAdapter.getEvent(position).location)
                startActivity(showEventIntent)
            }

            override fun onLongItemClick(view: View?, position: Int) {
                val popup = PopupMenu(view!!.context, view)
                popup.inflate(R.menu.menu_note)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            FirebaseDatabase.getInstance().reference.child("events").child(eventsAdapter.getEvent(position).key.toString()).removeValue()
                            eventsAdapter.deleteEvent(position)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.modify -> {
                            val modifyEventIntent = Intent(context, CreateEventActivity::class.java)
                            modifyEventIntent.putExtra("event title",  eventsAdapter.getEvent(position).title)
                            modifyEventIntent.putExtra("event date",  eventsAdapter.getEvent(position).date)
                            modifyEventIntent.putExtra("event time",  eventsAdapter.getEvent(position).time)
                            modifyEventIntent.putExtra("event desc",  eventsAdapter.getEvent(position).description)
                            modifyEventIntent.putExtra("event location",  eventsAdapter.getEvent(position).location)
                            modifyEventIntent.putExtra("event key",  eventsAdapter.getEvent(position).key)
                            eventsAdapter.deleteEvent(position)
                            startActivityForResult(modifyEventIntent, REQUEST_MODIFY_EVENT)
                            return@setOnMenuItemClickListener true
                        }
                    }
                    return@setOnMenuItemClickListener false
                }
                popup.show()
            }

        }))

        initEventsListener()
    }

    private fun initEventsListener() {
        FirebaseDatabase.getInstance()
            .getReference("events")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newEvent = dataSnapshot.getValue<Event>(Event::class.java)
                    if (newEvent != null) {
                        filteringEvents(newEvent)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val changedEvent = dataSnapshot.getValue<Event>(Event::class.java)
                    if (changedEvent != null) {
                        filteringEvents(changedEvent)
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    "Esemény törölve!".showText(context)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    fun refreshCalendarList(){
        eventsAdapter.deleteAll()
        initEventsListener()
    }

    private fun chooseFilterEventsDialog(){
        lateinit var dialog: AlertDialog
        val filters = arrayOf("minden","csak aktuális","csak aktuális hónap","csak aktuális hét")
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Ezekre az eseményekre szűrnék: ")
        builder.setSingleChoiceItems(filters,-1) { _, which->
            eventsFilter = filters[which]
            text_filter_event.text = eventsFilter
            refreshCalendarList()
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun filteringEvents(event: Event){
        val today = Calendar.getInstance()
        when (eventsFilter){
            "minden"-> {
                eventsAdapter.addEvent(event)
            }
            "csak aktuális" -> {
                if(event.date.textToDate() >= today){
                    eventsAdapter.addEvent(event)
                }
            }
            "csak aktuális hónap" -> {
                if(event.date.textToDate() >= today && event.date.textToDate()[Calendar.MONTH] == today[Calendar.MONTH]){
                    eventsAdapter.addEvent(event)
                }
            }
            "csak aktuális hét" -> {
                if(event.date.textToDate() >= today && event.date.textToDate()[Calendar.MONTH] == today[Calendar.MONTH] &&
                    event.date.textToDate()[Calendar.DAY_OF_MONTH] <= today[Calendar.DAY_OF_MONTH] + 7){
                    eventsAdapter.addEvent(event)
                }
            }
        }
        eventsAdapter.sortingByDate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            refreshCalendarList()
        }
    }
}
