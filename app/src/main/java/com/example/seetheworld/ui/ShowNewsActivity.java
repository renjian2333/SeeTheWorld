package com.example.seetheworld.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class ShowNewsActivity extends AppCompatActivity {
    private TextView title;
    private TextView media;
    private TextView date;
    private TextView content;

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
                Log.d("shownews", t.getResult().toString());

                android.os.Message msg = new android.os.Message();
                msg.what = 0;
                msg.obj = t.getResult();

                handler.sendMessage(msg);
            }
        }).start();

    }

}