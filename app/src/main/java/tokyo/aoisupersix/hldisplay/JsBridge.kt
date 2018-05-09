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

    val TAG = this.javaClass.simpleName
    val webView = webView

    init {
        //Databaseの取得
        val database = FirebaseDatabase.getInstance()
        val memRef = database.getReference("members")
        val stateRef = database.getReference("status")

        //初期化処理
        //ステータスの初期化を先に行う
        initStatus(stateRef)
        initMember(memRef)

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

    /**
     * メンバー情報をデータベースから取得してJSに送信します
     * @param memRef データベースのmemberリファレンス
     */
    private fun initMember(memRef: DatabaseReference) {
        memRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                //メンバー情報格納
                val members = MembersModel(mutableListOf())
                for(m in p0!!.children) {
                    members.members.add(MemberModel(
                            m.child("name").value.toString(),
                            (m.child("status").value as Long).toInt()
                    ))
                }
                //JSONに変換
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(MembersModel::class.java)
                val decodeJson = adapter.toJson(members)
                //Jsに送信
                webView.loadUrl("javascript:initCard('$decodeJson')")
            }
            override fun onCancelled(p0: DatabaseError?) {}
        })
    }

    /**
     * ステータス情報をデータベースから取得してJSに送信します
     * @param stateRef データベースのstatusリファレンス
     */
    private fun initStatus(stateRef: DatabaseReference) {
        stateRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                //ステータス情報格納
                val states = StatesModel(mutableListOf())
                for(m in p0!!.children) {
                    states.states.add(StatusModel(
                            m.child("name").value.toString(),
                            m.child("color").value.toString()
                    ))
                }
                //JSONに変換
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(StatesModel::class.java)
                val decodeJson = adapter.toJson(states)
                //Jsに送信
                webView.loadUrl("javascript:initState('$decodeJson')")
            }
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