package repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var Intent = Intent(context, ConnectivityReceiver::class.java)
        Intent.putExtra("INTENT_HANDLE_CONNECTIVITY_CHANGE", "");
        context?.startService(Intent)
    }

}