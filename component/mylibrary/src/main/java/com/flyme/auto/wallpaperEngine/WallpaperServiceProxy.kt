package com.flyme.auto.wallpaperEngine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import androidx.core.os.UserManagerCompat
import com.flyme.auto.wallpaperEngine.WallpaperActiveCallback

/**
 * @author jinyalin
 * *
 * @since 2017/7/27.
 */
open class WallpaperServiceProxy(var host: Context) : WallpaperService() {

    private val activateCallback: WallpaperActiveCallback?

    init {
        @Suppress("LeakingThis")
        attachBaseContext(host)

        activateCallback = if (host is WallpaperActiveCallback)
            host as WallpaperActiveCallback else null
    }

    override fun onCreateEngine(): WallpaperService.Engine? {
        return null
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    open inner class ActiveEngine : Engine() {
        private var mWallpaperActivate = false

        private var mEngineUnlockReceiver: BroadcastReceiver? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            if (!isPreview) {
                if (UserManagerCompat.isUserUnlocked(this@WallpaperServiceProxy)) {
                    activateWallpaper()
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mEngineUnlockReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            activateWallpaper()
                            context.unregisterReceiver(this)
                        }
                    }
                    val filter = IntentFilter(Intent.ACTION_USER_UNLOCKED)
                    this@WallpaperServiceProxy.registerReceiver(mEngineUnlockReceiver, filter)
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            deactivateWallpaper()
        }

        private fun activateWallpaper() {
            mWallpaperActivate = true
            activateCallback?.onWallpaperActivate()
        }

        private fun deactivateWallpaper() {
            if (mWallpaperActivate) {
                activateCallback?.onWallpaperDeactivate()
            }
        }
    }
}
