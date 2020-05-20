package hu.bme.aut.android.hataridonaploeh7k1k.receiver

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.bme.aut.android.hataridonaploeh7k1k.LoginActivity
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText

class NotificationReceiver: BroadcastReceiver() {

    private var notificationManager: NotificationManagerCompat? = null

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        notificationManager = NotificationManagerCompat.from(context);

        val title: String = context.getString(R.string.app_name)
        val message: String = "Nézd meg, hogy milyen programok vagy feladatok várnak rád ma!"

        val notificationIntent = Intent(context, LoginActivity::class.java)
        val intent = PendingIntent.getActivity(
            context, 0,
            notificationIntent, 0
        )

        val notification: Notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(intent)
            .build()
        notificationManager!!.notify(1, notification)

    }
}