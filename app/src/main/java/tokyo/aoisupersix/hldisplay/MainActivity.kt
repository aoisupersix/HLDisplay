package tokyo.aoisupersix.hldisplay

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * 在室情報を表示するアクティビティクラスです。
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //フルスクリーン化
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = window.decorView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        setContentView(R.layout.activity_main)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return super.dispatchKeyEvent(event)
    }
}
