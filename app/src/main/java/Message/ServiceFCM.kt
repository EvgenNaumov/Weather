package Message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.appweather.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import view.main.MainActivity

class ServiceFCM: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(utils.TAG, "onNewToken: ${message.toString()}")
        if (!message.data.isNullOrEmpty()){
            val title = message.data.get(KEY_TITLE).toString()
            val message = message.data[KEY_MESSAGE].toString()
            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()){
                push(title, message)
            }
        }

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(utils.TAG, "onNewToken: $token")
    }

    companion object{
        private const val NOTIFICATION_ID_LOW = 1
        private const val NOTIFICATION_ID_HIGH = 2
        private const val CHANNEL_LOW_ID = "channel_id_1"
        private const val CHANNEL_HIGH_ID = "channel_id_2"

        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }

    private fun push(title:String, message:String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java)
        val contextIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //getString(R.string.title_notification_low), getString(R.string.text_notofication_low)
        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_LOW_ID).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(title)
            setContentText(message)
            setContentIntent(contextIntent)
            priority = NotificationManager.IMPORTANCE_LOW
        }

        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(getString(R.string.title_notification_high))
            setContentText(getString(R.string.text_notofication_high))
            priority = NotificationManager.IMPORTANCE_HIGH
        }



        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameLow = "Name $CHANNEL_LOW_ID"
            val channelDescriptionLow = "Description: $CHANNEL_LOW_ID"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_LOW_ID, channelNameLow,channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }

        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderLow.build())

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameHigh = "Name $CHANNEL_HIGH_ID"
            val channelDescriptionHigh = "Description: $CHANNEL_HIGH_ID"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh = NotificationChannel(CHANNEL_HIGH_ID, channelNameHigh,channelPriorityHigh).apply {
                description = channelDescriptionHigh
            }
            notificationManager.createNotificationChannel(channelHigh)
        }

        notificationManager.notify(NOTIFICATION_ID_HIGH, notificationBuilderHigh.build() )

    }
}