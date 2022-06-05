package com.example.seetheworld.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.seetheworld.ui.news.NewsListFragment;
import com.example.seetheworld.ui.search.SearchActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ImageButton search_btn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<NewsListFragment> fragmentList = new ArrayList<>();
    private ArrayList<String> newsType = new ArrayList<>();


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
        tabLayout = view.findViewById(R.id.news_tab_layout);
        viewPager = view.findViewById(R.id.news_view_pager);
        initData();
        viewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(), 0));
        tabLayout.setupWithViewPager(viewPager);

        addListener();
    }

    /**
     * @description: 设置监听
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