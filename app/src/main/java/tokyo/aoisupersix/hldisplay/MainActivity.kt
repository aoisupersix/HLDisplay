package tokyo.aoisupersix.hldisplay

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

/**
 * 在室情報を表示するアクティビティクラスです。
 */
class MainActivity : Activity() {

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //フルスクリーン化
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val view = window.decorView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        setContentView(R.layout.activity_main)

        //WebViewにディスプレイを表示
        val webView = findViewById<WebView>(R.id.mainWebView)
        webView.loadUrl("https://aoisupersix.github.io/HLDisplayWebPage/")
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JsBridge(this), "android")

        //通知設定
        val updateReceiver = UpdateReceiver()
        val filter = IntentFilter()
        filter.addAction("RELOAD")
        registerReceiver(updateReceiver, filter)

        //データベース
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("members")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                Log.d(TAG, "FirebaseRealtimeDatabase: ChildAdded!")
                //TODO メンバー追加時の処理
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                Log.d(TAG, "FirebaseRealtimeDatabase: ChildChanged!")
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

    override fun onResume() {
        super.onResume()

        val view = window.decorView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE

        val webView = findViewById<WebView>(R.id.mainWebView)
        webView.loadUrl("javascript:getStatus();")
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d(TAG, "dispatchKeyEvent-call")
        if(event?.action == KeyEvent.ACTION_DOWN) {
            if(event.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                //終了
                finish()
            }else if(event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                //WebViewの更新をおこなう
                val webView = findViewById<WebView>(R.id.mainWebView)
                webView.reload()
            }
        }
        return false
    }

    inner class UpdateReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "RELOAD From broadCast")
            val webView = findViewById<WebView>(R.id.mainWebView)
            webView.loadUrl("javascript:getStatus();")
        }
    }
}
