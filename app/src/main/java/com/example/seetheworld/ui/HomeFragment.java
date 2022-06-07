package com.example.seetheworld.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.seetheworld.R;
import com.example.seetheworld.data.Data;
import com.example.seetheworld.ui.floatwindow.FloatWindow;
import com.example.seetheworld.ui.news.NewsListFragment;
import com.example.seetheworld.ui.news.ShowNewsActivity;
import com.example.seetheworld.ui.search.SearchActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ImageButton search_btn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<NewsListFragment> fragmentList = new ArrayList<>();
    private ArrayList<String> newsType = new ArrayList<>();
    private ImageButton listen_all_btn;
    private MyAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search_btn = view.findViewById(R.id.news_search_btn);
        listen_all_btn = view.findViewById(R.id.listen_news_btn);
        tabLayout = view.findViewById(R.id.news_tab_layout);
        viewPager = view.findViewById(R.id.news_view_pager);
        initData();
        adapter = new MyAdapter(getActivity().getSupportFragmentManager(), 0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        addListener();
    }

    /**
     * @description: 设置各种监听
     * @params: []
     * @return: void
     * @author: renjian
     * @dateTime: 2022/6/5 17:19
     */

    private void addListener() {
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        listen_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.speakStartID = 0;
                Data.speakList = Data.dataList;

                Intent intent = new Intent(getActivity(), FloatWindow.class);

                if (!Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getActivity().getPackageName())), 0);
                } else {
                    getActivity().startService(intent);
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Data.dataList = ((NewsListFragment) adapter.getItem(position)).dataList;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData(){
        newsType.add("热门");
        newsType.add("科技");
        newsType.add("社会");
        newsType.add("教育");
        newsType.add("娱乐");
        newsType.add("时政");
        newsType.add("游戏");
        newsType.add("财经");
        newsType.add("股票");

        for(String type:newsType){
            fragmentList.add(new NewsListFragment(type));
        }

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return newsType.get(position);
        }
    }
}