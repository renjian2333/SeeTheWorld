package com.example.seetheworld.util;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class SpeechApplication extends Application {
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=8c276a2b");
        super.onCreate();
    }
}
