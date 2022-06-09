package com.example.seetheworld.ui.search;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.seetheworld.R;
import com.example.seetheworld.data.Data;
import com.example.seetheworld.data.Message;
import com.example.seetheworld.data.PartNews;
import com.example.seetheworld.ui.floatwindow.FloatWindow;
import com.example.seetheworld.ui.news.DiffUtilNewsCallBack;
import com.example.seetheworld.ui.news.NewsListAdapter;
import com.example.seetheworld.ui.news.ShowNewsActivity;
import com.example.seetheworld.util.HttpUtils;
import com.example.seetheworld.util.TTSUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    NewsListAdapter adapter;
    private final Handler handler = new Handler(Looper.myLooper());
    private SearchView searchView;
    private TextView warning_tv;
    private static final String TAG = "SRUtils";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        String hint = bundle.getString("hint");

        searchView = (SearchView) findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        // 设置搜索提示
        searchView.setQueryHint(hint);

        warning_tv = (TextView) findViewById(R.id.no_result_info);

        // recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.result_list_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return true;
            }

        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(abs(dy) > 2){
                    searchView.clearFocus();
                }
            }
        });
        adapter = new NewsListAdapter(new DiffUtilNewsCallBack());
        adapter.setOnNewsItemClickListener(position -> {
            Intent intent = new Intent(this, ShowNewsActivity.class);
            intent.putExtra("newsid", Data.searchResultList.get(position).getId());
            Data.speakStartID = position;
            Data.dataType = 1;
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // 是否读新闻
        if(bundle.getBoolean("read")){
            TTSUtils.getInstance().speak("正在查找相关新闻",this);
            getData(hint,50);
            Log.d(TAG, "SearchActivity: begin to read");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(Data.searchResultList.size() == 0){
                        TTSUtils.getInstance().speak("暂无相关新闻",SearchActivity.this);
                        return;
                    }
                    Data.speakList =Data.searchResultList;
                    Data.speakStartID = 0;
                    Data.dataType = 1;

                    Intent intent = new Intent(SearchActivity.this, FloatWindow.class);

                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName())), 0);
                    } else {
                        startService(intent);
                    }
                }
            }).start();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        getData(query, 50);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getData(newText, 50);
        return true;
    }

    /**
     * 查找实现
     * @param keyword
     * @param size
     */
    private void getData(String keyword, int size){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpUtils.get("/news/find?keyword="+keyword+"&top="+size);
                Gson gson=new Gson();
                Type listType = new TypeToken<Message<List<PartNews>>>(){}.getType();
                Message<List<PartNews>> t=gson.fromJson(result,listType);
                Data.searchResultList = t.getResult();
                Data.dataType = 1;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(Data.searchResultList.size() == 0){
                            warning_tv.setVisibility(View.VISIBLE);
                        } else {
                            warning_tv.setVisibility(View.GONE);
                            adapter.submitList(Data.searchResultList);
                        }
                    }
                });

            }
        }).start();
    }
}