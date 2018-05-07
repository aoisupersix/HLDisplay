package tokyo.aoisupersix.hldisplay

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFcmListenerService: FirebaseMessagingService() {
    //private val TAG = MyInstanceIdService::class.simpleName!!

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.d("firebase", "Message Received!")
        val broadcast = Intent()
        broadcast.action = "RELOAD"
        baseContext.sendBroadcast(broadcast)
    }
}