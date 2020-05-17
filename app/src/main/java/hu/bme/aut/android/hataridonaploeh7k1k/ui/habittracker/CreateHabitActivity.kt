package hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker

import android.app.Activity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Habit
import hu.bme.aut.android.hataridonaploeh7k1k.extension.dateToText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_new_habit.*
import java.util.*

class CreateHabitActivity: Activity() {

    private var userId: String = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_habit)

        btnAddNewHabit.setOnClickListener { sendClick() }
    }

    private fun validateForm() = habit_title.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        val today = Calendar.getInstance()

        val key = FirebaseDatabase.getInstance().reference.child("habits").push().key ?: return
        val newHabit = Habit(key, userId, habit_title.text.toString(), today.dateToText(), false)
        FirebaseDatabase.getInstance().reference
            .child("habits")
            .child(key)
            .setValue(newHabit)
            .addOnCompleteListener {
                "Új szokás hozzáadva".showText(this)
                finish()
            }
    }
}