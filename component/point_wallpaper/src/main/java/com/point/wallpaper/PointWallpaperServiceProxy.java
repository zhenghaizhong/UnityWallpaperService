package com.point.wallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.flyme.auto.wallpaperEngine.WallpaperServiceProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenghaizhong on 2022/10/28.
 */
public class PointWallpaperServiceProxy extends WallpaperServiceProxy {
    public PointWallpaperServiceProxy(Context host) {
        super(host);
    }
    @Override
    public Engine onCreateEngine() {
        Log.e("@@@@@@","onCreateEngine success wow wow");
        return new MyWallpaperEngine();
    }
    private class MyWallpaperEngine extends WallpaperServiceProxy.ActiveEngine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        public MyWallpaperEngine() {
            maxNumber = 4;
            touchEnabled = true;
            circles = new ArrayList<>();
            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }
        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (touchEnabled) {
                float x = event.getX();
                float y = event.getY();
                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(Color.WHITE);
                        circles.clear();
                        circles.add(new MyPoint(
                                String.valueOf(circles.size() + 1), (int) x, (int) y));
                        drawCircles(canvas, circles);
                    }
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }
                super.onTouchEvent(event);
            }
        }
        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    if (circles.size() >= maxNumber) {
                        circles.clear();
                    }
                    int x = (int) (width * Math.random());
                    int y = (int) (height * Math.random());
                    circles.add(new MyPoint(String.valueOf(circles.size() + 1),
                            x, y));
                    drawCircles(canvas, circles);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 1000);
            }
        }
        // Surface view requires that all elements are drawn completely
        private void drawCircles(Canvas canvas, List<MyPoint> circles) {
            canvas.drawColor(Color.RED);
            for (MyPoint point : circles) {
                canvas.drawCircle(point.x, point.y, 20.0f, paint);
            }
        }

        @Override
        public void onDestroy() {
            Log.e("@@@@@@","onDestroy" );
            super.onDestroy();
        }
    }

    public class MyPoint {
        String text;
        int x;
        int y;
        public MyPoint(String text, int x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }
}