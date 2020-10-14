package com.revosassignment

import android.app.Application
import android.content.Context
import com.ravos.RevOS
import java.lang.ref.WeakReference

class RevOSApp: Application() {
    private val TAG = this::class.java.canonicalName

    override fun onCreate() {
        super.onCreate()
        wApp!!.clear()
        wApp = WeakReference(this@RevOSApp)
        RevOS.init(this)

    }

    companion object {
        private var wApp: WeakReference<RevOSApp>? = WeakReference<RevOSApp>(null)!!
        val instance: RevOSApp get() = wApp?.get()!!

        val context: Context
            get() {
                val app = if (null != wApp) wApp!!.get() else RevOSApp()
                return if (app != null) app.applicationContext else RevOSApp().applicationContext
            }

    }
}