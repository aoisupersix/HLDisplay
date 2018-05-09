package tokyo.aoisupersix.hldisplay

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.firebase.database.*

/**
 * Htmlファイルのスクリプトとの橋渡しを行うクラスです。
 * @param context Activityのコンテキスト
 */
class JsBridge(context: Context) {

    val TAG = this.javaClass.simpleName

    init {
        //Firebase Realtime Databaseのイベント記述
        val database = FirebaseDatabase.getInstance()
        val memRef = database.getReference("members")
        val stateRef = database.getReference("status")

        //メンバー情報初期化イベント
        memRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                Log.d(TAG, "Initialize members")

                //メンバー情報取得
                val members: Array<MemberModel?> = arrayOfNulls(p0!!.childrenCount.toInt())
                for(m in p0.children) {
                    members[m.key.toInt()] = MemberModel(
                            m.child("name").value.toString(),
                            (m.child("status").value as Long).toInt())
                }
            }

            override fun onCancelled(p0: DatabaseError?) {}
        })

        //メンバー情報更新のイベント
        memRef.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                Log.d(TAG, "Firebase-RealtimeDatabase: ChildAdded!")
                //TODO メンバー追加時の処理
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                Log.d(TAG, "Firebase-RealtimeDatabase: ChildChanged!")
                Log.d(TAG, p0.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {}
            override fun onCancelled(p0: DatabaseError?) {}
        })
    }

    private fun initHtmlView() {

    }

    /**
     * 引数に与えられたIdのステータス表示を更新します
     * @param userId メンバーのID
     * @param userStateId ステータス情報のID
     */
    @JavascriptInterface
    fun updateState(userId: Int, userStateId: Int) {
        Log.d("JsBridge", "updateState:userId:$userId,userStateId:$userStateId")
    }
}