package tokyo.aoisupersix.hldisplay

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.google.firebase.database.*
import com.squareup.moshi.Moshi

/**
 * Htmlファイルのスクリプトとの橋渡しを行うクラスです。
 * @param webView HTMLを表示するWebView
 */
class JsBridge(webView: WebView) {

    /**
     * Log表示用の識別子
     */
    val TAG = this.javaClass.simpleName

    /**
     * HTMLを表示するWebView
     */
    val webView = webView

    init {
        //Databaseの取得
        val database = FirebaseDatabase.getInstance()
        val ref = database.reference

        //region 初期化処理
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                val dbModel = DbModel(mutableListOf(), mutableListOf())
                //メンバー情報格納
                for(m in p0!!.child("members").children) {
                    dbModel.members.add(MemberModel(
                            m.child("name").value.toString(),
                            (m.child("status").value as Long).toInt()
                    ))
                }
                //ステータス情報格納
                for(m in p0!!.child("status").children) {
                    dbModel.states.add(StatusModel(
                            m.child("name").value.toString(),
                            m.child("color").value.toString()
                    ))
                }
                //JSONに変換
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(DbModel::class.java)
                val decodeJson = adapter.toJson(dbModel)
                //Jsに送信
                webView.loadUrl("javascript:initMember('$decodeJson')")
            }
            override fun onCancelled(p0: DatabaseError?) {}
        })
        //endregion

        //メンバー情報更新のイベント
        val memRef = database.getReference("members")
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

    /**
     * 引数に与えられたIdのステータス表示を更新します
     * @param userId メンバーのID
     * @param userStateId ステータス情報のID
     */
    @JavascriptInterface
    fun updateState(userId: Int, userStateId: Int) {
        Log.d(TAG, "updateState:userId:$userId,userStateId:$userStateId")
    }
}