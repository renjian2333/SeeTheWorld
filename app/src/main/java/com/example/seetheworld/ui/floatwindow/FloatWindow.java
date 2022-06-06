package com.example.seetheworld.ui.floatwindow;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.seetheworld.R;
import com.example.seetheworld.data.Data;
import com.example.seetheworld.util.TTSUtils;


public class FloatWindow extends Service {
    public static boolean isPaused = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View floatWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        floatWindow = LayoutInflater.from(this).inflate(R.layout.activity_float_window, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.gravity = Gravity.END | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WRAP_CONTENT;
        layoutParams.height = WRAP_CONTENT;
        layoutParams.y = 700;
        windowManager.addView(floatWindow, layoutParams);

        ImageButton close_btn = floatWindow.findViewById(R.id.float_window_close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtils.getInstance().stop();
                stopSelf();
                windowManager.removeView(floatWindow);
            }
        });

        ImageButton pause_btn = floatWindow.findViewById(R.id.pause_btn);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(isPaused) {
                    TTSUtils.getInstance().resume();
                    isPaused = false;
                    pause_btn.setImageDrawable(getDrawable(R.drawable.ic_pause_30));
                } else {
                    TTSUtils.getInstance().pause();
                    pause_btn.setImageDrawable(getDrawable(R.drawable.ic_play_30));
                    isPaused = true;
                }
            }
        });

        ImageButton next_btn = floatWindow.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySpeak();
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mySpeak();
        return super.onStartCommand(intent, flags, startId);
    }


    private void mySpeak(){
        if(Data.speakStartID < Data.dataList.size()) {
            TTSUtils.getInstance().speak(Data.dataList.get(Data.speakStartID++).getContent(), this);
        } else {
            TTSUtils.getInstance().release();
            stopSelf();
            windowManager.removeView(floatWindow);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
