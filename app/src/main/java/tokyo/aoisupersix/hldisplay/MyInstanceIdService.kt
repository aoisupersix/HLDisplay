package tokyo.aoisupersix.hldisplay

import com.google.firebase.iid.FirebaseInstanceIdService

class MyInstanceIdService: FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
    }
}