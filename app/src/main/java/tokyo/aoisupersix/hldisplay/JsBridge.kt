package tokyo.aoisupersix.hldisplay

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class JsBridge(context: Context) {

    val TAG = this.javaClass.simpleName

    init {
        //データベース
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("members")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                Log.d(TAG, "Firebase-RealtimeDatabase: ChildAdded!")
                //TODO メンバー追加時の処理
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                Log.d(TAG, "Firebase-RealtimeDatabase: ChildChanged!")
                Log.d(TAG, p0.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    @JavascriptInterface
    fun updateState(userId: Int, userStateId: Int) {
        Log.d("JsBridge", "updateState:userId:$userId,userStateId:$userStateId")
    }
}