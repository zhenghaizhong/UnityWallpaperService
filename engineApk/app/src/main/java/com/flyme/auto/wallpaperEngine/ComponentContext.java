package com.flyme.auto.wallpaperEngine;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.flyme.auto.wallpaperEngine.resource.ResourcesManager;
import dalvik.system.DexClassLoader;


/**
 *
 */

public class ComponentContext extends ContextWrapper implements WallpaperActiveCallback {
    private String componentPath;
    private WallpaperActiveCallback origin;

    private Resources mResources;
    private ClassLoader mClassLoader;

    public ComponentContext(Context base, String componentPath) {
        super(base.getApplicationContext());
        this.componentPath = componentPath;
        if (base instanceof WallpaperActiveCallback) {
            origin = (WallpaperActiveCallback) base;
        }
    }

    @Override
    public Resources getResources() {
        if (mResources == null) {
            mResources = ResourcesManager.createResources(getBaseContext(), componentPath);
        }
        return mResources;
    }

    @Override
    public ClassLoader getClassLoader() {
        return getClassLoader(componentPath);
    }


    private ClassLoader getClassLoader(String componentFilePath) {
        if (mClassLoader == null) {
            mClassLoader = new DexClassLoader(componentFilePath,
                    getCacheDir().getAbsolutePath(), null, getBaseContext().getClassLoader());
        }
        return mClassLoader;
    }

    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public void onWallpaperActivate() {
        if (origin != null) {
            origin.onWallpaperActivate();
        }
    }

    @Override
    public void onWallpaperDeactivate() {
        if (origin != null) {
            origin.onWallpaperDeactivate();
        }
    }
}

