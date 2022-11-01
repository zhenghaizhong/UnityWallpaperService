package com.flyme.auto.wallpaperEngine;

import android.content.Context;
import android.service.wallpaper.WallpaperService;

/**
 * package name need same as component
 */

public interface IProvider {

    WallpaperService provideProxy(Context host);
}