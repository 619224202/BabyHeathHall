package com.swkj.bear.iptvbearandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;

import com.swkj.babyhall.MainActivity;
import com.swkj.babyheathhall.R;
import com.swkj.bear.iptvbearandroid.utils.BackgroundMusic;
import com.swkj.bear.iptvbearandroid.views.PlayProgressView;


import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmuku.ijk.IjkVideoView;

public class StartActivity extends Activity {

    private IjkVideoView mVideoView;
    private PlayProgressView mPlayProgressView;

    private int run = 0;

    //总时间，毫秒
    private long duration;
    //
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            mPlayProgressView.setCurrentTime(mVideoView.getCurrentPosition());
            mPlayProgressView.setProgress(100 * mVideoView.getCurrentPosition() / mVideoView.getDuration());
            run++;
            if (mPlayProgressView.isV() && run == 10) {
                mPlayProgressView.downView();
            }
            if (run == 10) {
                run = 0;
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mPlayProgressView = findViewById(R.id.play_progress);

        System.out.println("StartActivity onCreate");

        BackgroundMusic.getInstance(this).pauseBackgroundMusic();

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView = findViewById(R.id.video_view);

        String url = getIntent().getStringExtra("url");
        System.out.println("onActivityResult" + url);
        Uri mVideoUri = Uri.parse(url);
//        mVideoUri = Uri.parse("http://112.33.16.34:18080/ts/bbdy.ts");
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.start();
        mVideoView.setVisibility(View.INVISIBLE);
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mVideoView.stopPlayback();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mVideoView.setVisibility(View.VISIBLE);
                mPlayProgressView.setCountTime(mVideoView.getDuration());
                mPlayProgressView.upView();
            }
        });

        handler.postDelayed(runnable, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //改写物理返回键的逻辑
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            mPlayProgressView.downView();
            mVideoView.setVisibility(View.INVISIBLE);
            mVideoView.stopPlayback();
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            mPlayProgressView.upView();
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                mPlayProgressView.setPlay(false);
            }else{
                mVideoView.start();
                mPlayProgressView.setPlay(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (mVideoView.isPlaying()) {
                int addTime = mVideoView.getCurrentPosition() + 10000;
                if (addTime < mVideoView.getDuration()) {
                    mVideoView.seekTo(addTime);
                }
            }
            mPlayProgressView.upView();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (mVideoView.isPlaying()) {
                int subTime = mVideoView.getCurrentPosition() - 10000;
                if (subTime > 0) {
                    mVideoView.seekTo(subTime);
                }
            }
            mPlayProgressView.upView();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void speedUp(long time){

    }
}