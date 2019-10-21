package com.swkj.bear.iptvbearandroid.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swkj.babyheathhall.R;


public class PlayProgressView extends ConstraintLayout {

    private ProgressBar mProgress;
    private ImageView imageView1;
    private ImageView imageView2;
    private TextView textView1;

    private String countTime = "/00:00:00";
    private boolean isV = true;

    public PlayProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.play_progress,this);

        mProgress = findViewById(R.id.progressBar);
        imageView1 = findViewById(R.id.play_imageView_1);
        imageView2 = findViewById(R.id.play_imageView_2);
        textView1 = findViewById(R.id.textView1);
    }

    public void setProgress(int progress){
        mProgress.setProgress(progress);
    }

    public int getProgress(){
        return mProgress.getProgress();
    }

    public void setPlay(boolean flag) {
        if (flag) {
            imageView2.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);
        } else {
            imageView2.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
        }
    }

    public void setCountTime(int t) {
        countTime = "/" + formatTime(t);
    }

    public void setCurrentTime(int t) {
        textView1.setText(String.format("%s", formatTime(t) + countTime));
    }

    public String formatTime(int t) {
        t /= 1000;
        String h = String.valueOf(t / 3600);
        int m = (t % 3600) / 60;
        String tm = (m<10)?"0"+m:String.valueOf(m);
        int s = (t % 3600) % 60;
        String ts = (s<10)?"0"+s:String.valueOf(s);

        return h + ":" + tm + ":" + ts;
    }

    public void upView() {
        isV = true;
        mProgress.setVisibility(VISIBLE);
        imageView1.setVisibility(VISIBLE);
        imageView2.setVisibility(VISIBLE);
        textView1.setVisibility(VISIBLE);
    }

    public  void downView() {
        isV = false;
        mProgress.setVisibility(INVISIBLE);
        imageView1.setVisibility(INVISIBLE);
        imageView2.setVisibility(INVISIBLE);
        textView1.setVisibility(INVISIBLE);
    }

    public boolean isV (){
        return isV;
    }
}