package com.swkj.bear.iptvbearandroid.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import com.swkj.babyheathhall.R;


/**
 * Created by zuixian on 2018/5/14.
 */

public class LoadingView extends ConstraintLayout {

    private ProgressBar mProgress;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.loading,this);

        mProgress = findViewById(R.id.customProgressBar);
    }

    public void setProgress(int progress){
        mProgress.setProgress(progress);
    }

    public int getProgress(){
        return mProgress.getProgress();
    }
}
