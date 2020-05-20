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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.receiver.NotificationReceiver

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

        val setAlarmButton = findViewById<Button>(R.id.startAlarm)
        setAlarmButton.setOnClickListener {
            startAlarm()
        }

        val cancelAlarmButton = findViewById<Button>(R.id.cancelAlarm)
        cancelAlarmButton.setOnClickListener {
            cancelAlarm()
        }

        createNotificationChannel()
    }

    fun startAlarm() {
        val manager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val interval = 60 * 1_000L //every minute

        manager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 5_000L,
            interval.toLong(),
            pendingIntent
        )

        "Értesítés beállítva".showText(this)
    }

    fun cancelAlarm() {
        val manager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pendingIntent)

        "Értesítések törölve".showText(this)
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
