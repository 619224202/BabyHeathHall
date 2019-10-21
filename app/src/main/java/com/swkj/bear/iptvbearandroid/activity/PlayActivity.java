package com.swkj.bear.iptvbearandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.swkj.babyhall.MainActivity;
import com.swkj.babyheathhall.R;
import com.swkj.media.android.PlayView;

import tv.danmuku.ijk.IjkVideoView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;


public class PlayActivity extends Activity {
    private IjkVideoView mVideoView;

//    private String[] urls = {"http://221.228.226.23/11/t/j/v/b/tjvbwspwhqdmgouolposcsfafpedmb/sh.yinyuetai.com/691201536EE4912BF7E4F1E2C67B8119.mp4",
//            "http://221.228.226.5/15/t/s/h/v/tshvhsxwkbjlipfohhamjkraxuknsc/sh.yinyuetai.com/88DC015DB03C829C2126EEBBB5A887CB.mp4",
//            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
//    };
    private PlayView mPlayView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(this).inflate(R.layout.vedio_main, null);
        setContentView(rootView);
        String url = getIntent().getStringExtra("url");
        System.out.println("onActivityResult" + url);
        mVideoView = findViewById(R.id.ijkview);
//        url = "ftp://ftptest:123456@192.168.2.157:21/download/1234.ts";
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
        View playView = rootView.findViewById(R.id.playview);
        mPlayView = new PlayView(this, playView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayView.clearHandler();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ORIENTATION_LANDSCAPE) {
            mPlayView.onLandSpace();
        } else {
            mPlayView.onPortrait();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayView.onResume();
    }

    @Override
    public void onBackPressed() {
//        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            mPlayView.toggleFullScreen();
//        } else {
//            super.onBackPressed();
//        }
        toHall();
    }

    private boolean isSeek;
    private boolean isSeekUp;
    private int addTime;
    private int addCount;
    private long beginTime;//计时
    private long waitTime = 3000;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //改写物理返回键的逻辑
        //确定键播放暂停
        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if(mPlayView.isPlaying()) {
                mPlayView.onPause();
            }else{
                mPlayView.continuePlay();
            }
            mPlayView.showControl();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mPlayView.isPlaying() && !isSeek) {
//                mPlayView.addTime(10000,true);
                isSeek = true;
                isSeekUp = true;
                addTime = 0;
                addCount = 0;
                doAddTime();
                mPlayView.clearHandler();
                mPlayView.showControl();
            }


        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mPlayView.isPlaying() && !isSeek) {
//                mPlayView.addTime(-10000,true);
                isSeek = true;
                isSeekUp = false;
                addTime = 0;
                addCount = 0;
                doAddTime();
                mPlayView.clearHandler();
                mPlayView.showControl();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mPlayView.isPlaying() && isSeek) {
               isSeek = false;
                mPlayView.addTime(addTime,true);
            }

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mPlayView.isPlaying()&&isSeek) {
                isSeek = false;
                mPlayView.addTime(addTime,true);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public void doAddTime(){
        beginTime = System.currentTimeMillis();
        waitTime = 1500;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isSeek){
                    if(System.currentTimeMillis()-beginTime>=waitTime) {
                        int currentAddTime = 0;
                        int iFlag = 1;
                        if (!isSeekUp) {
                            iFlag = -1;
                        }
                        currentAddTime = 10000;
                        final int addTimeBuf = currentAddTime * iFlag;
                        addTime += addTimeBuf;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPlayView.addTime(addTimeBuf, false);
                            }
                        });
                        addCount++;
                        if(addCount==1){
                            waitTime = 1000;
                        }else{
                            waitTime = 1000;
                        }
                        beginTime = System.currentTimeMillis();
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private synchronized void addTime(){

    }


    public void toHall(){
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
