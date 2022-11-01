package com.flyme.auto.wallpaperEngine.proxy

import android.content.Context
import android.service.wallpaper.WallpaperService


class ProxyProvider{

    fun provideProxy(host: Context): WallpaperService? {
        val proxy = ProxyApi.getProxy(host, "/data/customizecenter/theme/component.apk", "com.point.wallpaper.ProviderImpl")
        if (proxy != null) {
            return proxy
        }

        //本地？？？
//        val constructor = Class.forName(NORMAL_PROXY_CLASS).getConstructor(Context::class.java)
//        return constructor.newInstance(host) as WallpaperService
        return null
    }
}
