package com.point.wallpaper;

import android.content.Context;
import android.service.wallpaper.WallpaperService;

import com.flyme.auto.wallpaperEngine.IProvider;

/**
 * Created by zhenghaizhong on 2022/10/28.
 */
public class ProviderImpl implements IProvider {
    @Override
    public WallpaperService provideProxy(Context host) {
        return new PointWallpaperServiceProxy(host);
    }
}