package hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Habit
import hu.bme.aut.android.hataridonaploeh7k1k.extension.RecyclerViewItemClickListener
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.textToDate
import hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker.adapter.HabitsAdapter
import kotlinx.android.synthetic.main.fragment_habittracker.*
import java.util.*


class HabitTrackerFragment : Fragment() {

    private lateinit var habitsAdapter: HabitsAdapter
    private var today = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_habittracker, container, false)
        view?.let { initialView(it) }
        return view
    }

    private fun initialView(view: View) {
        val addButton = view.findViewById<Button>(R.id.button_add_habit)
        addButton.setOnClickListener {
           showNewHabitDialog()
        }

        val todayDate = view.findViewById<TextView>(R.id.todayDate)
        todayDate.text = today.dateToText()
        todayDate.setOnClickListener{
            showDatePickerDialogToSetToday()
        }


        val recyclerView: RecyclerView = view.findViewById(R.id.rvHabits)
        recyclerView.addOnItemTouchListener(
            RecyclerViewItemClickListener(
                context,
                recyclerView,
                object : RecyclerViewItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        habitsAdapter.isReady(position)
                        FirebaseDatabase.getInstance().reference.child("habits")
                            .child(habitsAdapter.getHabit(position).key.toString())
                            .setValue(habitsAdapter.getHabit(position))
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val popup = PopupMenu(view!!.context, view)
                        popup.inflate(R.menu.menu_habit)
                        popup.setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.hmodifydate -> {
                                    showDatePickerDialog(position)
                                }
                                R.id.hdelete -> {
                                    FirebaseDatabase.getInstance().reference.child("habits")
                                        .child(habitsAdapter.getHabit(position).key.toString())
                                        .removeValue()
                                    habitsAdapter.deleteHabit(position)
                                    return@setOnMenuItemClickListener true
                                }
                            }
                            false
                        }
                        popup.show()
                    }
                })
        )

        habitsAdapter = HabitsAdapter(activity?.applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(activity).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        recyclerView.adapter = habitsAdapter

        initHabitsListener()
    }

    private fun initHabitsListener() {
        FirebaseDatabase.getInstance()
            .getReference("habits")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newHabit = dataSnapshot.getValue<Habit>(Habit::class.java)
                    if(newHabit!!.date!!.equals(today.dateToText())) {
                        habitsAdapter.addHabit(newHabit)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    "Szokás módosítva!".showText(context)
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    "Szokás törölve!".showText(context)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    private fun showNewHabitDialog() {
        val intent = Intent(context, CreateHabitActivity::class.java)
        startActivity(intent)
    }

    private fun showDatePickerDialog(position: Int) {
        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val monthReal = month + 1
            habitsAdapter.setDate("$year.$monthReal.$dayOfMonth", position)
            FirebaseDatabase.getInstance().reference.child("habits")
                .child(habitsAdapter.getHabit(position).key.toString())
                .setValue(habitsAdapter.getHabit(position))
            refresh()
        }
        val c = Calendar.getInstance()
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, datePickerListener,
                c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
            )
        }
        datePickerDialog?.show()
    }

    private fun showDatePickerDialogToSetToday() {
        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val monthReal = month + 1
            val s: String = "$year.$monthReal.$dayOfMonth"
            today = s.textToDate()
            todayDate.text = today.dateToText()
            refresh()
        }
        val c = Calendar.getInstance()
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, datePickerListener,
                c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
            )
        }
        datePickerDialog?.show()
    }

    fun refresh(){
        getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }
}
