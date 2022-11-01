package com.flyme.auto.wallpaperEngine;

import android.service.wallpaper.WallpaperService;
import android.util.Log;

import com.flyme.auto.wallpaperEngine.proxy.ProxyProvider;

/**
 * Created by zhenghaizhong on 2022/10/28.
 */
public class WallpaperEngineService extends WallpaperService implements WallpaperActiveCallback{
    private ProxyProvider proxyProvider = new ProxyProvider();
    private WallpaperService proxy;
    @Override
    public void onCreate() {
        super.onCreate();
        proxy = proxyProvider.provideProxy(this);
        if (proxy != null) {
            proxy.onCreate();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (proxy != null) {
            proxy.onDestroy();
        }
    }
    @Override
    public Engine onCreateEngine() {
        if (proxy != null) {
            return proxy.onCreateEngine();
        } else {
            return new Engine();
        }
    }

    @Override
    public void onWallpaperActivate() {
        Log.e("@@@@@","onWallpaperActivate");
    }

    @Override
    public void onWallpaperDeactivate() {
        Log.e("@@@@@","onWallpaperDeactivate");
    }
}