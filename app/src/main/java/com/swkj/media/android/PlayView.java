package com.swkj.media.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.swkj.babyhall.MainActivity;
import com.swkj.babyheathhall.R;
import com.swkj.bear.iptvbearandroid.activity.PlayActivity;
import com.swkj.bear.iptvbearandroid.activity.StartActivity;
import com.swkj.bear.iptvbearandroid.utils.LogUtils;
import com.swkj.bear.iptvbearandroid.utils.MyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmuku.ijk.IjkVideoView;

/**
 * desc：
 * author：Created by xusong on 2018/9/19 15:44.
 */


public class PlayView implements View.OnClickListener {

    private Activity mContext;
    private View mRootView;
    //播放器
    private IjkVideoView mIjkVideoView;
    //当前播放时间
    private TextView mTvCurrentTime;
    //进度控制
    private SeekBar mSeekBar;
    //总共播放时间
    private TextView mTvTotalTime;
    //全屏按钮
    private ImageView mIvFullScreen;
    //播放状态
    private ImageView mIvPlayStatus;
    //正在缓冲
    private LinearLayout mLlLoading;
    //控制界面
    private LinearLayout mLlControl;
    //进度变化的Handler
    private PlayHandler mPlayHandler = new PlayHandler();
    //手势识别器
    private GestureDetector mGestureDetector;
    //记录当前的播放进度
    private int mCurrentPosition;
    //快进快退
    private final TextView mTvProgress;
    //
    private int mCurrentX;

    private static final int Normal_Play = 1;
    private static final int Pause_Play = 2;
    private static final int Hide_Control = 3;
    private static final int Normal_Delay = 1000;
    private static final int Hide_Title_Interval = 5000;
    private final int mTouchSlop;
    private boolean mIsSetting;
    //
    private static AudioManager mAudioManager;


    public PlayView(Activity context, View rootView) {
        mContext = context;
        mRootView = rootView;

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        mIjkVideoView = rootView.findViewById(R.id.ijkview);
        mTvCurrentTime = rootView.findViewById(R.id.tv_current_time);
        mSeekBar = rootView.findViewById(R.id.seekbar);
        mTvTotalTime = rootView.findViewById(R.id.tv_total_time);
        mIvFullScreen = rootView.findViewById(R.id.iv_play_screen);
        mIvPlayStatus = rootView.findViewById(R.id.iv_play_status);
        mLlLoading = rootView.findViewById(R.id.ll_loading);
        mLlControl = rootView.findViewById(R.id.ll_control);
        mTvProgress = rootView.findViewById(R.id.tv_progress);
        /*---------------------------------------点击事件----------------------------------------*/
        mIvFullScreen.setOnClickListener(this);
        mIvPlayStatus.setOnClickListener(this);
        //手势识别器
        mGestureDetector = new GestureDetector(mContext, new PlayGesture());

        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mIsSetting) {
                        mIsSetting = false;
                        mTvProgress.setVisibility(View.GONE);
                    }
                    if (mCurrentX != 0) {
                        int progress = mCurrentX + mIjkVideoView.getCurrentPosition();
                        mIjkVideoView.seekTo(progress);
                        sycnProgress(false, progress);
                        mTvProgress.setVisibility(View.GONE);
                        mCurrentX = 0;
                    }

                }
                return true;
            }
        });

        //seekBar监听
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//进度变化
                if (fromUser) {//是用户手动拖动的
                    if (mIjkVideoView.isPlaying()) {
                        progress = progress * mIjkVideoView.getDuration() / 100;
                        sycnProgress(true, progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始拖动
                mIvPlayStatus.setVisibility(View.GONE);
                if (mIjkVideoView.isPlaying())
                    clearHandler();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//停止拖动
                int progress = seekBar.getProgress() * mIjkVideoView.getDuration() / 100;
                mIjkVideoView.seekTo(progress);
                sycnProgress(true, progress);

            }
        });

        //播放开始的监听
        mIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                formatTime(mTvTotalTime, iMediaPlayer.getDuration());
                mLlLoading.setVisibility(View.GONE);
                mContext.findViewById(R.id.loadbg).setVisibility(View.GONE);
                //检测播放进度和缓冲进度
                mPlayHandler.sendEmptyMessage(Normal_Play);

            }
        });

        //播放完成监听
        mIjkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mIvPlayStatus.setImageResource(R.mipmap.jz_restart_normal);
                mIvPlayStatus.setVisibility(View.VISIBLE);
                mSeekBar.setProgress(0);
                mSeekBar.setSecondaryProgress(0);
                sycnProgress(true, 0);
                mIjkVideoView.pause();
                mIjkVideoView.release(true);
                ((PlayActivity)mContext).toHall();
            }
        });

        //播放错误监听
        mIjkVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                LogUtils.LogE("onError i=" + i);
                ((PlayActivity)mContext).toHall();
                return false;
            }
        });

        //缓冲监听
        mIjkVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                if (i == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {//开始缓冲
                    mLlLoading.setVisibility(View.VISIBLE);
                    mIvPlayStatus.setVisibility(View.GONE);
                   clearHandler();
                } else if (i == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {//缓冲完成
                    mLlLoading.setVisibility(View.GONE);
                    mPlayHandler.sendEmptyMessage(Pause_Play);
                }
                return false;
            }
        });


    }

    /**
     * 格式化时间
     *
     * @param textView
     */
    private void formatTime(TextView textView, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String format = sdf.format(new Date(time));
        if (mIjkVideoView.getDuration() > 60 * 60 * 1000) {//大于一小时
            textView.setText(mIjkVideoView.getDuration() / 60 / 1000 + ":" + format.substring(format.length() - 5, format.length()));
        } else {
            textView.setText(format.substring(format.length() - 5, format.length()));
        }
    }

    //横屏
    public void onLandSpace() {
        ViewGroup.LayoutParams params = mRootView.getLayoutParams();
        params.height = MyUtils.getScreenWidth(mContext);
    }

    //竖屏
    public void onPortrait() {
        ViewGroup.LayoutParams params = mRootView.getLayoutParams();
        params.height = MyUtils.dip2px(mContext, 260);
    }


    private class PlayHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Normal_Play:
                    if (mIjkVideoView.isPlaying()) {
                        if (mLlControl.getVisibility() == View.VISIBLE) {
                            mPlayHandler.sendEmptyMessageDelayed(Hide_Control, Hide_Title_Interval);
                        }
                        sycnProgress(false, mIjkVideoView.getCurrentPosition());
                        sendEmptyMessageDelayed(Normal_Play, Normal_Delay);
                    }
                    break;
                case Pause_Play:
                    sendEmptyMessage(Normal_Play);
                    break;
                case Hide_Control:
                    mPlayHandler.removeMessages(Hide_Control);
                    mLlControl.setVisibility(View.GONE);
                    mIvPlayStatus.setVisibility(View.GONE);
                    break;

            }
        }
    }

    /**
     * 同步进度
     */
    private void sycnProgress(boolean fromUser, int currentPosition) {
        formatTime(mTvCurrentTime, currentPosition);
        if (!fromUser) {
            //进度
            mSeekBar.setProgress((int) ((currentPosition * 1.0f / mIjkVideoView.getDuration()) * 100));
            //缓冲
            mSeekBar.setSecondaryProgress(mIjkVideoView.getBufferPercentage());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_screen://全屏
                toggleFullScreen();
                break;
            case R.id.iv_play_status:
                if (!mIjkVideoView.isPlaying()) {//没有在播放
                    continuePlay();
                } else {//在播放
                    onPause();
                }
                break;
        }

    }

    /**
     * 防止内存泄漏
     */
    public void clearHandler() {
        if (mPlayHandler != null)
            mPlayHandler.removeCallbacksAndMessages(null);
    }


    public void onPause() {
        if (mIjkVideoView.isPlaying()) {
            mCurrentPosition = mIjkVideoView.getCurrentPosition();
            clearHandler();
            mIjkVideoView.pause();
            mIvPlayStatus.setImageResource(R.mipmap.jz_play_normal);
            mIvPlayStatus.setVisibility(View.VISIBLE);
        }
    }

    public void continuePlay(){
        mIjkVideoView.start();
        mIvPlayStatus.setImageResource(R.mipmap.jz_pause_normal);
        mPlayHandler.sendEmptyMessage(Pause_Play);
        if (mCurrentPosition != 0)
            sycnProgress(false, mCurrentPosition);
    }

    /**
     * 全屏切换
     */
    public void toggleFullScreen() {
        if (mContext.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//横屏
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mIvFullScreen.setImageResource(R.mipmap.jz_enlarge);
        } else {//竖屏
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mIvFullScreen.setImageResource(R.mipmap.jz_shrink);
        }
    }

    public void onResume() {
    }

    private class PlayGesture extends GestureDetector.SimpleOnGestureListener {

        private int mCurrentVolume;

        @Override
        public boolean onDown(MotionEvent e) {
            mCurrentVolume = getCurrentVolume();
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtils.LogE("onSingleTapUp");
            if (mLlControl.getVisibility() == View.GONE || mIvPlayStatus.getVisibility() == View.GONE) {
                mLlControl.setVisibility(View.VISIBLE);

                mIvPlayStatus.setImageResource(mIjkVideoView.isPlaying() ? R.mipmap.jz_pause_normal : R.mipmap.jz_play_normal);

                mIvPlayStatus.setVisibility(View.VISIBLE);
            } else {
                mLlControl.setVisibility(View.GONE);
                mIvPlayStatus.setVisibility(View.GONE);
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int diffX = (int) (e2.getX() - e1.getX());
            int diffY = (int) (e2.getY() - e1.getY());

            if (Math.abs(diffX) > mTouchSlop && mIjkVideoView.isPlaying() && !mIsSetting) {//有效滑动
                mIvPlayStatus.setVisibility(View.GONE);
                mTvProgress.setText((diffX * 50 / 1000) + "S");
                mCurrentX = diffX * 50;
                if (diffX > 0) {
                    switchIcon(R.mipmap.jz_forward_icon);
                } else {
                    switchIcon(R.mipmap.jz_backward_icon);
                }

            } else if (Math.abs(diffY) > mTouchSlop) {//有效滑动
                //是否正在改变声音或亮度
                mIsSetting = true;
                if (diffY > 0) { //向下拖动
                    if (e1.getX() < mIjkVideoView.getMeasuredWidth() / 2) {//减小亮度
                        MyUtils.setScreenBrightness(mContext, MyUtils.getScreenBrightness(mContext) - (diffY / 100));
                        switchIcon(R.mipmap.play_bright);
                        mTvProgress.setText((MyUtils.getScreenBrightness(mContext) * 100 / 255) + "%");

                    } else {//减小声音
                        float percent = (float) (diffY * 1.0 / mRootView.getMeasuredHeight());
                        LogUtils.LogE("percent = " + percent);
                        setVolume(mCurrentVolume, -percent / 2);
                    }

                } else {//向上拖动
                    if (e1.getX() < mIjkVideoView.getMeasuredWidth() / 2) {//增加亮度
                        MyUtils.setScreenBrightness(mContext, MyUtils.getScreenBrightness(mContext) - (diffY / 100));
                        switchIcon(R.mipmap.play_bright);
                        mTvProgress.setText((MyUtils.getScreenBrightness(mContext) * 100 / 255) + "%");

                    } else {//增加声音
                        float percent = (float) (diffY * 1.0 / mRootView.getMeasuredHeight());
                        LogUtils.LogE("percent = " + percent);
                        setVolume(mCurrentVolume, -percent / 2);

                    }
                }
            }


            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return false;
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogUtils.LogE("onDoubleTap");
            toggleFullScreen();
            return super.onDoubleTap(e);
        }
    }

    private void switchIcon(int iconId) {
        mTvProgress.setVisibility(View.VISIBLE);
        mTvProgress.setCompoundDrawablesRelativeWithIntrinsicBounds(null, mContext.getResources().getDrawable(iconId), null, null);
    }

    /**
     * 获取声音
     */
    public int getCurrentVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 设置声音
     */

    public void setVolume(int currentVolume, float percent) {
        LogUtils.LogE(Math.round(getMaxVolume() * percent) + "");
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + Math.round(getMaxVolume() * percent), AudioManager.FLAG_PLAY_SOUND);
        if (getCurrentVolume() == 0) {
            switchIcon(R.mipmap.play_voice_off);
            mTvProgress.setText("off");

        } else {
            switchIcon(R.mipmap.play_voice_normal);
            mTvProgress.setText(getCurrentVolume() * 100 / getMaxVolume() + "%");

        }


    }

    /**
     * 获取声音
     */
    public int getMaxVolume() {

        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    public boolean isPlaying(){
        if(mIjkVideoView!=null && mIjkVideoView.isPlaying()){
            return true;
        }
        return false;
    }

    /**
     * 时间，是否seek
     * @param time
     * @param isSeek
     */
    public void addTime(int time,boolean isSeek){
        //总时间
        int currentTime = mIjkVideoView.getCurrentPosition() + time;
        if(!isSeek){
            currentTime = (mSeekBar.getProgress()*mIjkVideoView.getDuration())/100+time;
        }
        if(time>0) {
            if (currentTime >= mIjkVideoView.getDuration()) {
                currentTime = mIjkVideoView.getDuration()-100;
            }
        }else{
           if(currentTime<0){
               currentTime = 100;
            }
        }
        System.out.println("增加时间为="+time+",isSeek="+isSeek);
        if(time!=0){
            sycnProgress(false, currentTime);
            mTvProgress.setVisibility(View.GONE);
            if(isSeek){
                mIjkVideoView.seekTo(currentTime);
            }
        }
        showControl();
    }

    public void showControl(){
        mIvPlayStatus.setImageResource(R.mipmap.jz_play_normal);
        mIvPlayStatus.setVisibility(View.VISIBLE);
        mLlControl.setVisibility(View.VISIBLE);
    }


}
