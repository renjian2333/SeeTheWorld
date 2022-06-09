package com.example.seetheworld.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.lang.ref.WeakReference;


public class SRUtils implements InitListener, RecognizerListener {
    private static volatile SRUtils instance = null;
    private String result = "";
    private boolean isInitSuccess = false;
    private SpeechRecognizer mRecognizer;
    private StringBuffer buffer = new StringBuffer();
    private Context context;
    private static final String TAG = "SRUtils";
    private boolean resultReady = false;

    public static SRUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SRUtils.class) {
                if (instance == null) {
                    instance = new SRUtils(context);
                }
            }
        }
        return instance;
    }

    private SRUtils(Context context){
        initialize(context);
    }

    /**
     * 获取单次识别结果
     * @return
     */
    public String getResult() {
        synchronized (this) {
            return result;
        }
    }

    public void initialize(Context context){
        this.context = context;
        mRecognizer = SpeechRecognizer.createRecognizer(context, this);
        // 清空参数
        mRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置在线云端
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置语音输入语言，zh_cn为简体中文
        mRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mRecognizer.setParameter(SpeechConstant.VAD_EOS, "2000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mRecognizer.setParameter(SpeechConstant.ASR_PTT,"0");
        // 设置返回结果种类(json,xml,plain)
        mRecognizer.setParameter(SpeechConstant.RESULT_TYPE,"plain");
        // 设置此参数输出数字结果格式 0：倾向于汉字，1：倾向于数字，
        mRecognizer.setParameter("nunum", "1");

    }

    /**
     * 开始监听
     * ErrorCode.SUCCESS 监听成功状态码
     */
    public int startRecognize() {
        this.buffer.delete(0,this.buffer.length());

        if (!isInitSuccess) {
            Log.d(TAG, "startRecognize: Recognizer初始化失败");
        }
        resultReady = false;
        return mRecognizer.startListening(this);
    }

    /**
     * 取消听写
     */
    public void cancelRecognize() {
        if(mRecognizer.isListening()){
            mRecognizer.cancel();
        }
        Log.d(TAG, "cancelRecognize: ");
    }

    public boolean isResultReady(){
        return resultReady;
    }

    @Override
    public void onInit(int i) {
        if (i == ErrorCode.SUCCESS) {
            isInitSuccess = true;
        } else {
            Log.d(TAG, "init error code " + i);
        }
    }

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {

    }

    @Override
    public void onBeginOfSpeech() {
        Log.d(TAG, "onBeginOfSpeech: ");
    }

    @Override
    public void onEndOfSpeech() {
        TTSUtils.getInstance().speak("正在识别",this.context);
    }

    @Override
    public void onError(SpeechError speechError) {
        Log.d(TAG, "onError: "+ speechError.getPlainDescription(true));
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        synchronized (this) {
            buffer.append(recognizerResult.getResultString());
            if (b) {
                Log.d(TAG, "onResult: end");
                // 保存最终结果
                result = buffer.toString();
                resultReady = true;
            }
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
    }
}
