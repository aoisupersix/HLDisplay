package tokyo.aoisupersix.hldisplay

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

class JsBridge(context: Context) {

    @JavascriptInterface
    fun updateState(userId: Int, userStateId: Int) {
        Log.d("JsBridge", "updateState:userId:$userId,userStateId:$userStateId")
    }
}