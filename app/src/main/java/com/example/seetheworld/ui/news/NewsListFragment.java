package com.example.seetheworld.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.seetheworld.R;
import com.example.seetheworld.data.Message;
import com.example.seetheworld.data.PartNews;
import com.example.seetheworld.ui.ShowNewsActivity;
import com.example.seetheworld.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    private String newsType;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    NewsListAdapter adapter;
    List<PartNews> dataList;
    int page = 1;
    private final Handler handler = new Handler(Looper.myLooper());
    private TextView footer;

    public NewsListFragment(String newsType) {
        this.newsType = newsType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recyclerView
        recyclerView = view.findViewById(R.id.news_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return true;
            }

        });
        adapter = new NewsListAdapter(new DiffUtilNewsCallBack());
        adapter.setOnNewsItemClickListener(position -> {
            Intent intent = new Intent(getActivity(), ShowNewsActivity.class);
            intent.putExtra("newsid", dataList.get(position).getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        swipeRefresh = view.findViewById(R.id.news_list_swipe);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        initPullFlush();
        initLoadMoreListener();

        getNewsData(page, 20, true);
        footer = view.findViewById(R.id.footer);
    }

    /**
     * @description: 下拉刷新
     * @params: []
     * @return: void
     * @author: renjian
     * @dateTime: 2022/6/1 11:07
     */
    private void initPullFlush() {
        swipeRefresh.setOnRefreshListener(() -> {
            getNewsData(100 - page,10,false);
            page++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                }
            },500);
        });
    }

    /**
     * @description: 上拉加载更多
     * @params: []
     * @return: void
     * @author: renjian
     * @dateTime: 2022/6/1 11:09
     */
    private void initLoadMoreListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动到底部
                if (!recyclerView.canScrollVertically(1)) {
                    footer.setVisibility(View.VISIBLE);
                    getNewsData(page, 20, true);
                    page++;
                }
            }
        });
    }


    /**
     * @description: post请求新闻数据
     * @params: [page, pageSize, back]
     * @return: void
     * @author: renjian
     * @dateTime: 2022/5/31 10:51
     */
    private void getNewsData(int page, int pageSize, boolean back){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpUtils.post("/news/recommend?catagory="+newsType+"&userid=1952723",page,pageSize);
                Gson gson=new Gson();
                Type listType = new TypeToken<Message<List<PartNews>>>(){}.getType();
                Message<List<PartNews>> t=gson.fromJson(result,listType);
                dataList = t.getResult();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<PartNews> newList = new ArrayList<>(adapter.getCurrentList());
                        if(back){
                            newList.addAll(t.getResult());
                            footer.setVisibility(View.GONE);
                            adapter.submitList(newList);
                        } else {
                            newList.addAll(0, t.getResult());
                            adapter.submitList(newList);
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });

            }
        }).start();
    }



}
