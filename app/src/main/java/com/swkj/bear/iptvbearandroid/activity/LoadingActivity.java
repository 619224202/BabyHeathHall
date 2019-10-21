package com.swkj.bear.iptvbearandroid.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.swkj.babyhall.MainActivity;
import com.swkj.babyheathhall.R;
import com.swkj.bear.iptvbearandroid.bridge.HostJsScope;
import com.swkj.bear.iptvbearandroid.utils.BackgroundMusic;
import com.swkj.bear.iptvbearandroid.views.LoadingView;

import java.security.Key;
import java.util.ArrayList;


public class LoadingActivity extends Activity {

    private int selIndex;

    private ImageView role1;
    private ImageView role2;
    private ImageView role3;
    private ImageView role4;

    private ImageView gifImage;
    private ImageView hand1Image;
    private ImageView hand2Image;
    private ImageView hand3Image;
    private ImageView hand4Image;

    private TextView textView;
    private TextView textView1;

    private ArrayList<ImageView> roles;
    private ArrayList<ImageView> hands;

    private LoadingView mLoadingView;

    private boolean isGameStart;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    LoadingActivity.this.mLoadingView.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        setPermission();
//        BackgroundMusic.getInstance(this).playBackgroundMusic("bgm.mp3",true);

//        isGameStart = false;

//        mLoadingView = findViewById(R.id.mLoadingView);
//        mLoadingView.setProgress(0);
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int p = mLoadingView.getProgress();
//                while (p != 100){
//                    try {
//                        Thread.sleep(20);
//                        p++;
//                        mLoadingView.setProgress(p);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(p == 100){
//                    isGameStart = true;
//                    Message msg = new Message();
//                    msg.what = 1;
//                    uiHandler.sendMessage(msg);
//                }
//            }
//        });
//        thread.start();

//        gifImage = findViewById(R.id.gif1);
//        AnimationDrawable ad = (AnimationDrawable)gifImage.getDrawable();
//        ad.start();

//        hand1Image = findViewById(R.id.hand1);
//        hand2Image = findViewById(R.id.hand2);
//        hand3Image = findViewById(R.id.hand3);
//        hand4Image = findViewById(R.id.hand4);
//
//        hands = new ArrayList<>();
//        hands.add(hand1Image);
//        hands.add(hand2Image);
//        hands.add(hand3Image);
//        hands.add(hand4Image);
//
//        textView = findViewById(R.id.textView);
//        textView1 = findViewById(R.id.textView2);
//        textView1.setText(R.string.confirm_enter);
//
//        selIndex = 0;
//
//        role1 = findViewById(R.id.role1);
//        role2 = findViewById(R.id.role2);
//        role3 = findViewById(R.id.role3);
//        role4 = findViewById(R.id.role4);

//        roles = new ArrayList<>();
//        roles.add(role1);
//        roles.add(role2);
//        roles.add(role3);
//        roles.add(role4);
//
//        playAnim();
    }


    public void playAnim(){

//        switch (selIndex+1){
//            case 1:
//                textView.setText(R.string.role_text_1);
//                break;
//            case 2:
//                textView.setText(R.string.role_text_2);
//                break;
//            case 3:
//                textView.setText(R.string.role_text_3);
//                break;
//            case 4:
//                textView.setText(R.string.role_text_4);
//                break;
//        }

        for(int i = 0; i < roles.size(); i++){
            ImageView role = roles.get(i);
            AnimationDrawable ad = (AnimationDrawable)role.getDrawable();
            if(i == selIndex){
                if(!ad.isRunning()){
                    ad.start();
                }
                hands.get(i).setVisibility(View.VISIBLE);
            }else{
                if(ad.isRunning()){
                    ad.stop();
                }
                hands.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(!isGameStart){
            return true;
        }

        if(keyCode== KeyEvent.KEYCODE_DPAD_LEFT){
            if(selIndex > 0){
                selIndex--;
                playAnim();
            }
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            if(selIndex < 3){
                selIndex++;
                playAnim();
            }
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_ENTER){
            System.out.println(selIndex+1);
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("headNum",selIndex+1);
            intent.putExtras(bundle);
            startActivity(intent,bundle);
            finish();
        }
        System.out.println("keyCode:"+keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundMusic.getInstance(this).pauseBackgroundMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusic.getInstance(this).resumeBackgroundMusic();
    }

    public void setPermission(){
        int version = Build.VERSION.SDK_INT;
        System.out.println("sdk版本是:"+version);
        String[] permissionStr = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE};

        String[] pression = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(LoadingActivity.this, permissionStr, 102);

    }
}
