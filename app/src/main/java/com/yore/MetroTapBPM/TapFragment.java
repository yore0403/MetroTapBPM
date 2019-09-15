package com.yore.MetroTapBPM;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TapFragment extends Fragment {

    ImageView iv_center_circle_ripple;
    ImageView iv_center_circle_shadow;
    ImageView iv_center_circle;
    TextView tv_int_text;
    TextView tv_float_text;
    View iv_tap;
    View iv_metro;
    //ImageView iv_tap_bottom;
    ImageView iv_metro_bottom;
    ImageView iv_bpm;
    ImageView iv_reset;
    TextView tv_bpm;
    TextView tv_reset;
    TextView tv_circle;
    ImageView iv_bpm_shadow;
    ImageView iv_reset_shadow;
    ConstraintLayout.LayoutParams params_center_circle;
    ConstraintLayout.LayoutParams params_tap_botom;
    Animation scaleAnimation;
    List<Long> time_list = new ArrayList<>();
    Long timeMillis,previous_timeMillis;
    Integer time_list_Maxnum = 6;
    Double standardDeviaction = 1000.0;
    Boolean is_BPM = true;
    Double BPM_ms;
    //int offset = 0;
    //TranslateAnimation transAnimation;
    Animation scaleAnimation1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tap, container, false);
        iv_center_circle_ripple = view.findViewById(R.id.center_circle_ripple);
        iv_center_circle_shadow = view.findViewById(R.id.center_circle_shadow);
        iv_center_circle = view.findViewById(R.id.center_circle);
        iv_metro = view.findViewById(R.id.selection_metro);
        iv_tap = view.findViewById(R.id.selection_tap);
        iv_metro_bottom = view.findViewById(R.id.metro_bottom);
        //iv_tap_bottom = view.findViewById(R.id.tap_bottom);
        tv_int_text = view.findViewById(R.id.integer_text);
        tv_float_text = view.findViewById(R.id.float_text);
        iv_bpm = view.findViewById(R.id.bpm_circle);
        iv_reset = view.findViewById(R.id.reset_circle);
        tv_bpm = view.findViewById(R.id.bpm_text);
        tv_reset = view.findViewById(R.id.reset_text);
        tv_circle = view.findViewById(R.id.circle_text);
        iv_bpm_shadow = view.findViewById(R.id.bpm_circle_shadow);
        iv_reset_shadow = view.findViewById(R.id.reset_circle_shadow);
        params_center_circle = (ConstraintLayout.LayoutParams)iv_center_circle.getLayoutParams();
        //params_tap_botom = (ConstraintLayout.LayoutParams)iv_tap_bottom.getLayoutParams();
        scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.center_circle_ripple_animation);

        iv_center_circle_shadow.setScaleX(3.05f);
        iv_center_circle_shadow.setScaleY(3.05f);
        processController();
        tapController();
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

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

        ImageView.OnTouchListener listener_reset = new View.OnTouchListener(){
            @Override
            public  boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_list.clear();
                        tv_int_text.setText("0");
                        tv_float_text.setText(".00");
                        previous_timeMillis = null;
                        timeMillis = null;

                        iv_reset.setImageResource(R.drawable.bpm_circle_hollowed_touched);
                        iv_reset_shadow.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_reset.setImageResource(R.drawable.bpm_circle_hollowed);
                        iv_reset_shadow.setVisibility(View.INVISIBLE);

                        break;
                }
                return true;
            }

        };
        //tv_reset.setOnClickListener(listener_reset);
        iv_reset.setOnTouchListener(listener_reset);

    }


    void tapController(){

        ImageView.OnTouchListener listener1 = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        previous_timeMillis = timeMillis;
                        timeMillis = System.currentTimeMillis();
                        if(previous_timeMillis != null)time_list.add(timeMillis - previous_timeMillis);

                        if(time_list.size()>=time_list_Maxnum)time_list.remove(0);
                        iv_center_circle.setImageResource(R.drawable.center_circle_pressed);
                        iv_center_circle_ripple.startAnimation(scaleAnimation);

                        setBPMMsNum();


                        break;
                    case MotionEvent.ACTION_UP:
                        iv_center_circle.setImageResource(R.drawable.center_circle);
                        int a = 0;

                    if(standardDeviaction<30){
                        iv_center_circle_shadow.setScaleX(3.8f);
                        iv_center_circle_shadow.setScaleY(3.8f);
                    }else if(standardDeviaction<60){
                        iv_center_circle_shadow.setScaleX(3.4f);
                        iv_center_circle_shadow.setScaleY(3.4f);
                    }else if(standardDeviaction<100){
                        iv_center_circle_shadow.setScaleX(3.15f);
                        iv_center_circle_shadow.setScaleY(3.15f);
                    }else{
                        iv_center_circle_shadow.setScaleX(3.05f);
                        iv_center_circle_shadow.setScaleY(3.05f);
                    }


                        break;
                }

                return true;
            }




        };
        //iv_center_circle.setOnClickListener(listener);
        iv_center_circle.setOnTouchListener(listener1);

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
        String float_text = "." + String.format("%02d", BPM_ms_float);
        tv_float_text.setText(float_text);
    }



    Double getMs(){
        double sum = 0;
        double avg_ms = 0;
        double total_ms = 0;
        double total_divider = 0;
        if(time_list.size() == 0)return 0.0;
        for(int i = 0;i<time_list.size();i++){
            total_ms += (i+1)*(i+1)*(i+1)*(i+1)*time_list.get(i);
            total_divider += (i+1)*(i+1)*(i+1)*(i+1);
        }


        avg_ms = total_ms/total_divider;
        for(int i = 0;i < time_list.size();i++ ){
            sum += Math.pow(time_list.get(i)-avg_ms, 2);
        }
        standardDeviaction =  time_list.size()<=1?1000:Math.sqrt(sum/time_list.size());
        Log.i("size",String.valueOf(standardDeviaction));
        Log.i("getBPM",String.valueOf(avg_ms));
        return avg_ms;
    }
    public Double getBPM(){
        if(time_list.size() == 0)return 0.0;
        return 1000*60/getMs();
    }

    public Boolean getIs_BPM() {
        return is_BPM;
    }

    public interface onTabTouchListener{
    void onPageChangedToMetro(Double ms, Boolean is_BPM);
}
    public void onPageSelected(Double ms, Boolean is_BPM) {
        Log.i("dddsds",String.valueOf(ms));
        this.is_BPM = is_BPM;
        if(is_BPM) {
            tv_circle.setText("BPM");
            tv_bpm.setText("ms");
        }else {
            tv_circle.setText("ms");
            tv_bpm.setText("BPM");
        }
        setBPMMsNum();

        if(ms == 0){
            time_list.clear();
            tv_int_text.setText("0");
            tv_float_text.setText(".00");
        }

        iv_center_circle_shadow.setScaleX(3.05f);
        iv_center_circle_shadow.setScaleY(3.05f);
    }



}


