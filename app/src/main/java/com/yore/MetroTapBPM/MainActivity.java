package com.yore.MetroTapBPM;

import java.util.*;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;


public class MainActivity extends AppCompatActivity implements
    ViewPager.OnPageChangeListener{
    int offset = 0;
    TranslateAnimation transAnimation;
    ImageView iv_tap_bottom;
    View iv_tap;
    View iv_metro;
    ConstraintLayout.LayoutParams params_tap_bottom;

    Double ms;
    Boolean is_BPM;


        private ViewPager viewPager;

    private TapFragment tapFragment = new TapFragment();
    private MetroFragment metroFragment = new MetroFragment();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            iv_tap_bottom = findViewById(R.id.tap_bottom);
            iv_metro = findViewById(R.id.selection_metro);
            iv_tap = findViewById(R.id.selection_tap);
            Log.i("ff","ff");
            //tabLayout = (TabLayout) findViewById(R.id.tabLayout);

            //注册监听
            viewPager.addOnPageChangeListener(this);
            //tabLayout.addOnTabSelectedListener(this);
            InitImageView();
            params_tap_bottom = (ConstraintLayout.LayoutParams)iv_tap_bottom.getLayoutParams();
            params_tap_bottom.width =offset;
            iv_tap_bottom.setX(offset/2);
            Log.i("ffff",String.valueOf(offset));

            processController();
            //添加适配器，在viewPager里引入Fragment
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            Log.i("ff","mnb");
                            return tapFragment;
                        case 1:
                            Log.i("ff","mnv");
                            return metroFragment;

                        //case 2:
                            //return fragment3;
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 2;
                }
            });

        }

        void processController(){
            View.OnClickListener listener_metro = new View.OnClickListener(){
                @Override
                public  void onClick(View v){
                    //iv_metro_bottom.setVisibility(View.VISIBLE);
                    //iv_tap_bottom.setVisibility(View.INVISIBLE);
                    Log.i("ff",String.valueOf(viewPager.getChildCount()));

                    transAnimation = new TranslateAnimation(0, offset, 0, 0);
                    transAnimation.setFillAfter(true);// True:圖片停在動畫結束位置
                    transAnimation.setDuration(150);
                    iv_tap_bottom.startAnimation(transAnimation);
                    ms = tapFragment.getMs();
                    is_BPM = tapFragment.getIs_BPM();
                    onPageChangedToMetro();
                    viewPager.setCurrentItem(1);
                }
            };
            View.OnClickListener listener_tap = new View.OnClickListener(){
                @Override
                public  void onClick(View v){
                    Log.i("ff","dd");

                    //iv_metro_bottom.setVisibility(View.INVISIBLE);
                    //iv_tap_bottom.setVisibility(View.VISIBLE);
                    transAnimation = new TranslateAnimation(offset, 0, 0, 0);
                    transAnimation.setFillAfter(true);// True:圖片停在動畫結束位置
                    transAnimation.setDuration(150);
                    iv_tap_bottom.startAnimation(transAnimation);
                    ms = metroFragment.getMs();
                    is_BPM = metroFragment.getIs_BPM();
                    onPageChangedToTap();
                    viewPager.setCurrentItem(0);
                }
            };
            iv_tap.setOnClickListener(listener_tap);
            iv_metro.setOnClickListener(listener_metro);
        }


/*
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //TabLayout里的TabItem被选中的时候触发
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
*/
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //viewPager滑动之后显示触发
            //tabLayout.getTabAt(position).select();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }



    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 獲取解析度寬度
        offset = screenW / 2;// 計算偏移量
    }

    public void onPageChangedToMetro() {
        metroFragment.onPageSelected(ms,is_BPM);
    }
    public void onPageChangedToTap() {
        metroFragment.stopMetro();
        tapFragment.onPageSelected(ms,is_BPM);
    }
}


