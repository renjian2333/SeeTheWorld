package com.example.seetheworld.ui.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.seetheworld.R;
import com.example.seetheworld.data.Message;
import com.example.seetheworld.data.PartNews;
import com.example.seetheworld.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

public class ShowNewsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView title;
    private TextView media;
    private TextView date;
    private TextView content;
    private ImageButton read_btn;
    private TextToSpeech engine;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                PartNews data = (PartNews) msg.obj;
                title.setText(data.getTitle());
                media.setText(data.getMedia());
                date.setText(data.getPubTime());
                content.setText(data.getContent());
                content.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        this.getSupportActionBar().hide();

        engine = new TextToSpeech(this, this);

        read_btn = (ImageButton) findViewById(R.id.read_news);
        read_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.speak(content.getText(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        int newsid = getIntent().getIntExtra("newsid",25752);
        getData(newsid);
        title = (TextView) findViewById(R.id.news_detail_title);
        media = (TextView) findViewById(R.id.news_detail_media);
        date = (TextView) findViewById(R.id.news_detail_date);
        content = (TextView) findViewById(R.id.news_detail_content);

    }

    public void GoBack(View view){
        finish();
    }

    /**
     * @description: 获取新闻数据
     * @params: [newsid]
     * @return: void
     * @author: renjian
     * @dateTime: 2022/5/31 18:48
     */

    private void getData(int newsid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpUtils.get("/news/detail?newsid="+ newsid +"&userid=1952723");
                Gson gson=new Gson();
                Type newsType = new TypeToken<Message<PartNews>>(){}.getType();
                Message<PartNews> t=gson.fromJson(result,newsType);

                android.os.Message msg = new android.os.Message();
                msg.what = 0;
                msg.obj = t.getResult();

                handler.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            engine.setLanguage(Locale.CHINA);
            engine.setPitch(1.0f);//方法用来控制音调
            engine.setSpeechRate(1.0f);//用来控制语速
        }
    }
}