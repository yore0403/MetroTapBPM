package com.yore.MetroTapBPM;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MetroFragment extends Fragment {

    ImageView iv_center_circle_ripple;
    ImageView iv_center_circle_shadow;
    ImageView iv_center_circle;
    TextView tv_int_text;
    TextView tv_float_text;
    //View iv_tap;
    //View iv_metro;
    //ImageView iv_tap_bottom;
    ImageView iv_metro_bottom;
    ImageView iv_bpm;
    ImageView iv_reset;
    TextView tv_bpm;
    TextView tv_circle;
    TextView tv_reset;
    ImageView iv_bpm_shadow;
    ImageView iv_reset_shadow;
    ConstraintLayout.LayoutParams params_center_circle;
    ConstraintLayout.LayoutParams params_tap_bottom;
    Animation scaleAnimation;
    //List<Long> time_list = new ArrayList<>();
    Long timeMillis,previous_timeMillis;
    Integer time_list_Maxnum = 6;
    Double standardDeviaction = 1000.0;
    Boolean is_BPM = true;
    Boolean is_started = false;
    Double BPM_ms;
    Double ms = 1.0;
    int i = 0;
    //int offset = 0;
    //TranslateAnimation transAnimation;
    Animation scaleAnimation1;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("onCreateView","dd");
        View view = inflater.inflate(R.layout.fragment_metro, container, false);

        iv_center_circle_ripple = view.findViewById(R.id.center_circle_ripple);
        iv_center_circle_shadow = view.findViewById(R.id.center_circle_shadow);
        iv_center_circle = view.findViewById(R.id.center_circle);
        //iv_metro = view.findViewById(R.id.selection_metro);
        //iv_tap = view.findViewById(R.id.selection_tap);
        iv_metro_bottom = view.findViewById(R.id.metro_bottom);
        //iv_tap_bottom = view.findViewById(R.id.tap_bottom);
        tv_int_text = view.findViewById(R.id.integer_text);
        tv_float_text = view.findViewById(R.id.float_text);
        iv_bpm = view.findViewById(R.id.bpm_circle);
        iv_reset = view.findViewById(R.id.ms_circle);
        tv_bpm = view.findViewById(R.id.bpm_text);
        tv_reset = view.findViewById(R.id.start_text);
        tv_circle = view.findViewById(R.id.circle_text);
        iv_bpm_shadow = view.findViewById(R.id.bpm_circle_shadow);
        iv_reset_shadow = view.findViewById(R.id.ms_circle_shadow);
        params_center_circle = (ConstraintLayout.LayoutParams)iv_center_circle.getLayoutParams();
        //params_tap_botom = (ConstraintLayout.LayoutParams)iv_tap_bottom.getLayoutParams();
        scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.center_circle_ripple_animation);


        iv_center_circle_shadow.setScaleX(3.05f);
        iv_center_circle_shadow.setScaleY(3.05f);

        processController();




        setBPMMsNum();

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //processView();


        //InitImageView();
        //params_tap_botom.width = offset;
        //iv_tap_bottom.setX(offset/2);

    }
    void processController(){

        ImageView.OnTouchListener listener_bpm = new View.OnTouchListener(){
            @Override
            public  boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_bpm_shadow.setVisibility(View.VISIBLE);
                        is_BPM = !is_BPM;
                        if(is_BPM) {
                            tv_circle.setText("BPM");
                            tv_bpm.setText("ms");
                        }else {
                            tv_circle.setText("ms");
                            tv_bpm.setText("BPM");
                        }
                        setBPMMsNum();

                        break;
                    case MotionEvent.ACTION_UP:
                        iv_bpm_shadow.setVisibility(View.INVISIBLE);
                        break;
                }
                return true;
            }

        };
        iv_bpm.setOnTouchListener(listener_bpm);

        ImageView.OnTouchListener listener_start = new View.OnTouchListener(){
            @Override
            public  boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(is_started){
                            i = 0;
                            iv_center_circle_shadow.setScaleX(3.05f);
                            iv_center_circle_shadow.setScaleY(3.05f);
                            handler.removeCallbacks(runnable);
                        }else{
                            if(ms!=0) {

                                handler.post(runnable);
                            }
                        }
                        is_started = !is_started;
                        iv_center_circle.setImageResource(R.drawable.center_circle_pressed);
                        //iv_reset.setImageResource(R.drawable.bpm_circle_hollowed_touched);
                        //iv_reset_shadow.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_center_circle.setImageResource(R.drawable.center_circle);
                        //iv_reset.setImageResource(R.drawable.bpm_circle_hollowed);
                        //iv_reset_shadow.setVisibility(View.INVISIBLE);

                        break;
                }
                return true;
            }

        };
        iv_center_circle.setOnTouchListener(listener_start);

        ImageView.OnTouchListener listener_reset = new View.OnTouchListener(){
            @Override
            public  boolean onTouch(View v, MotionEvent event){

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ms = 0.0;
                        tv_int_text.setText("0");
                        tv_float_text.setText(".00");
                        previous_timeMillis = null;
                        timeMillis = null;
                        iv_reset.setImageResource(R.drawable.bpm_circle_hollowed_touched);
                        iv_reset_shadow.setVisibility(View.VISIBLE);
                        i = 0;
                        iv_center_circle_shadow.setScaleX(3.05f);
                        iv_center_circle_shadow.setScaleY(3.05f);
                        handler.removeCallbacks(runnable);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_reset.setImageResource(R.drawable.bpm_circle_hollowed);
                        iv_reset_shadow.setVisibility(View.INVISIBLE);

                        break;
                }
                return true;
            }

        };
        iv_reset.setOnTouchListener(listener_reset);



    }
    void setBPMMsNum(){
        if(is_BPM) {
            BPM_ms = getBPM();
        }else{
            BPM_ms = getMs();
        }

        int BPM_ms_int = BPM_ms.intValue();
        int BPM_ms_float = (int) ((BPM_ms.floatValue() - BPM_ms_int) * 100);

        tv_int_text.setText(String.valueOf(BPM_ms_int));
        tv_float_text.setText("." + String.format("%02d", BPM_ms_float));
    }

    Double getBPM(){

        return ms == 0 ? 0 : Double.valueOf(1000*60/this.ms);
    }

    Double getMs(){
        return this.ms;
    }

    public Boolean getIs_BPM() {
        return is_BPM;
    }

    public void onPageSelected(Double ms, Boolean is_BPM) {
        this.ms = ms;
        this.is_BPM = is_BPM;
        if(is_BPM) {
            tv_circle.setText("BPM");
            tv_bpm.setText("ms");
        }else {
            tv_circle.setText("ms");
            tv_bpm.setText("BPM");
        }
        if(is_BPM) {
            BPM_ms = getBPM();
        }else{
            BPM_ms = getMs();
        }
        Log.i("BPM", BPM_ms.toString());
        int BPM_ms_int = BPM_ms.intValue();
        int BPM_ms_float = (int) ((BPM_ms.floatValue() - BPM_ms_int) * 100);

        tv_int_text.setText(String.valueOf(BPM_ms_int));
        tv_float_text.setText("." + String.format("%02d", BPM_ms_float));

        is_started = false;

        //handler.post(runnable);
        //startMetro();
        //timer.schedule(timerTask, 0,getMs().intValue());
        //iv_center_circle_ripple.startAnimation(scaleAnimation);

    }

    final Handler handler = new Handler();

    final Runnable runnable = new Runnable() {
        public void run() {
            i++;
            if(i >= 5){
                iv_center_circle_shadow.setScaleX(3.8f);
                iv_center_circle_shadow.setScaleY(3.8f);
            }else if(i >= 3){
                iv_center_circle_shadow.setScaleX(3.4f);
                iv_center_circle_shadow.setScaleY(3.4f);
            }else if(i == 1){
                iv_center_circle_shadow.setScaleX(3.15f);
                iv_center_circle_shadow.setScaleY(3.15f);
            }else{
                iv_center_circle_shadow.setScaleX(3.05f);
                iv_center_circle_shadow.setScaleY(3.05f);
            }
            iv_center_circle_ripple.startAnimation(scaleAnimation);
                handler.postDelayed(this, ms.intValue());
        }
    };

    public void stopMetro(){
        i = 0;
        iv_center_circle_shadow.setScaleX(3.05f);
        iv_center_circle_shadow.setScaleY(3.05f);
        handler.removeCallbacks(runnable);
    }
    public interface onTabTouchListener{

        void onPageChangedToTap(Double ms, Boolean is_BPM);
    }
}


