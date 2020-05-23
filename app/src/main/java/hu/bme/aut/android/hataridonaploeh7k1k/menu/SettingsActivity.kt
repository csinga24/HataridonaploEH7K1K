package hu.bme.aut.android.hataridonaploeh7k1k.menu

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.receiver.NotificationReceiver
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private val firebaseUser: FirebaseUser? =
        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

    val CHANNEL_ID = "channel"
    private var pendingIntent: PendingIntent? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val alarmIntent = Intent(this@SettingsActivity, NotificationReceiver::class.java)
        alarmIntent.setAction("alarm");

        pendingIntent = PendingIntent.getBroadcast(this@SettingsActivity, 0, alarmIntent, 0)

        val userName = findViewById<TextView>(R.id.profil_name)
        userName.text = "Név:  " + firebaseUser?.displayName

        val userEmail = findViewById<TextView>(R.id.profil_email)
        userEmail.text = "E-mail: " + firebaseUser?.email

        val setAlarmSwitch = findViewById<Switch>(R.id.switchAlarm)
        setAlarmSwitch.setOnCheckedChangeListener { view, isChecked ->
            if(isChecked) {
                val time: Calendar = getSelectedDate()
                startAlarm(time)
            }
            else{
                cancelAlarm()
            }
        }

        createNotificationChannel()
    }
    private fun getSelectedDate(): Calendar {
        val radioMorning: RadioButton = findViewById(R.id.radioButtonMorning)
        val radioNoon: RadioButton = findViewById(R.id.radioButtonNoon)
        val radioEvening: RadioButton = findViewById(R.id.radioButtonEvening)

        val time = Calendar.getInstance()

        when(true) {
            radioMorning.isChecked -> {
                time[Calendar.HOUR_OF_DAY] = 8
                time[Calendar.MINUTE] = 0
            }
            radioNoon.isChecked -> {
                time[Calendar.HOUR_OF_DAY] = 12
                time[Calendar.MINUTE] = 0
            }
            radioEvening.isChecked -> {
                time[Calendar.HOUR_OF_DAY] = 18
                time[Calendar.MINUTE] = 0
            }
            else -> {
                time[Calendar.HOUR_OF_DAY] = 8
                time[Calendar.MINUTE] = 0
            }
        }
        return time

    }


    fun startAlarm(selectedTime: Calendar) {
        val manager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val interval = 24 * 60 * 60 * 1_000L //every day

        manager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            selectedTime.timeInMillis + 5_000L,
            interval.toLong(),
            pendingIntent
        )

        "Értesítés beállítva (${selectedTime[Calendar.HOUR_OF_DAY]}:${selectedTime[Calendar.MINUTE]})".showText(this)
    }

    fun cancelAlarm() {
        val manager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pendingIntent)

        "Értesítés törölve".showText(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_ID,
                "Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is a Notification Channel"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel1)
        }
    }

}
