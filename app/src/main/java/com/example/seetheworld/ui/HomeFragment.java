package com.example.seetheworld.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.seetheworld.R;
import com.example.seetheworld.data.Data;
import com.example.seetheworld.ui.floatwindow.FloatWindow;
import com.example.seetheworld.ui.news.NewsListFragment;
import com.example.seetheworld.ui.news.ShowNewsActivity;
import com.example.seetheworld.ui.search.SearchActivity;
import com.example.seetheworld.util.SRUtils;
import com.example.seetheworld.util.TTSUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment {

    private ImageButton search_btn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<NewsListFragment> fragmentList = new ArrayList<>();
    private ArrayList<String> newsType = new ArrayList<>();
    private ImageButton listen_all_btn;
    private MyAdapter adapter;
    private ImageButton voice_helper_btn;
    private static final String TAG = "SRUtils";
    private PopupWindow popupWindow;
    private Handler handler = new Handler(Looper.myLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        voice_helper_btn = view.findViewById(R.id.voice_helper_btn);
        search_btn = view.findViewById(R.id.news_search_btn);
        listen_all_btn = view.findViewById(R.id.listen_news_btn);
        tabLayout = view.findViewById(R.id.news_tab_layout);
        viewPager = view.findViewById(R.id.news_view_pager);

        adapter = new MyAdapter(getChildFragmentManager(), 0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        addListener();
    }

    /**
     * @description: ??????????????????
     * @params: []
     * @return: void
     * @author: renjian
     * @dateTime: 2022/6/5 17:19
     */

    private void addListener() {
        // ????????????
        voice_helper_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.pop_window,null);
                Glide.with(popupView)
                        .load("https://cdn.smartroomcn.com/wp-content/uploads/2019/07/smartroom10-1562944851.gif")
                        .into((ImageView) popupView.findViewById(R.id.Voice_gif));

                popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAtLocation(getView(), Gravity.CENTER,0,0);

                // popupwindow??????????????????????????????
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        SRUtils.getInstance(getContext()).cancelRecognize();
                        TTSUtils.getInstance().stop();
                    }
                });

                // ??????????????????
                startSpeechRecognize(1000,getContext());
                
            }
        });

        // ??????????????????
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSearchPage("??????",false);
            }
        });

        // ????????????????????????
        listen_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenAllNews();
            }
        });

        // viewpager ??????????????????
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

    /**
     * ??????????????????
     * @param context
     */
    private void startSpeechRecognize(int millis, Context context){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Data.dataType= 2;
                TTSUtils.getInstance().speak("????????????",getContext());
                SRUtils sr = SRUtils.getInstance(context);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sr.startRecognize();
                    }
                }).start();

                Thread r = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while(!sr.isResultReady());
                    }
                });
                r.start();

                while(r.getState()!= Thread.State.TERMINATED);
                Log.d(TAG, "reuslt: " + sr.getResult());
                doSomething(sr.getResult());
                Log.d(TAG, "startSpeechRecognize: ????????????");
            }
        }).start();
    }


    /**
     * ?????????????????????????????????
     * @param order
     */
    private void doSomething(String order){
        // ??????
        if(order.indexOf("??????")!= -1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TTSUtils.getInstance()
                            .speak("???????????????1.????????????2.??????????????????????????????????????????????????????;3.????????????????????????????????????",
                            getContext());
                }
            }).start();
            Log.d(TAG, "doSomething: ????????????");
            startSpeechRecognize(18000,getContext());
            return;
        }

        // ????????????????????????
        if(order.indexOf("?????????") != -1){
            dismiss();
            listenAllNews();
        } else if (order.indexOf("??????") != -1){
            dismiss();
            // ????????????????????????
            for(String title:newsType){
                int position = order.indexOf(title);
                if(position != -1){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(position-1);
                            listenAllNews();
                        }
                    });
                }
            }
        } else if(order.indexOf("??????") != -1){
            dismiss();
            // ?????????????????????
            Log.d(TAG, "doSomething: "+order.substring(2));
            redirectToSearchPage(order.substring(2),true);
        } else {
            // ?????????????????????
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TTSUtils.getInstance()
                            .speak("?????????????????????????????????????????????????????????????????????",
                                    getContext());
                }
            }).start();
            startSpeechRecognize(8200,getContext());
        }
    }

    /**
     * ?????? popupWindow
     */
    private void dismiss(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        });
    }
    /**
     * ?????????????????????
     * @param hint
     * @param read
     */
    private void redirectToSearchPage(String hint, boolean read){
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hint",hint);
        bundle.putBoolean("read",read);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * ?????????
     */
    private void listenAllNews(){
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

    /**
     * ???????????????
     */
    private void initData(){
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");
        newsType.add("??????");

        for(String type:newsType){
            fragmentList.add(new NewsListFragment(type));
        }

    }

    /**
     * viewpager???adapter
     */
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

        public int getPostionByTitle(String title){
            return newsType.indexOf(title);
        }
    }
}