package com.swkj.bear.iptvbearandroid.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmuku.ijk.FileMediaDataSource;

/**
 * Created by wanglu on 2019/9/27.
 */
public class MediaSoundPlayer {
    private String mediaUrl;
    //声音的类型。背景跟一般声音
    public int mediaType;
    //类型
    public final static int MEDIA_PLAYGROUND    = 1;   //背景
    public final static int MEDIA_EFFECT        = 2;    //Effect
    //是否循环播放
    public boolean isLoop;
    //media音乐
    private IjkMediaPlayer mediaPlayer;

    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;
    //是否播放结束

    public Context context;

    private Map<String,String> mHeaders;

    private String bufferUrl="";


    public MediaSoundPlayer(Context context){
        this.context = context;
        this.mediaType = MEDIA_EFFECT;
        this.isLoop = false;
    }

    public void initMedia(){
        mediaPlayer = new IjkMediaPlayer();
        //初始化声音
        mediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

//        mediaPlayer.setOption(1, "probesize", 200L);
//                    ijkMediaPlayer.setOption(4, "packet-buffering", 1L);
        //是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
//        mediaPlayer.setOption(4, "packet-buffering", 0);
        mediaPlayer.setOption(4, "flush_packets", 1L);
//                    ijkMediaPlayer.setOption(4, "framedrop", 1L);

//        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"soundtouch",0);
        //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC,"skip_loop_filter",48);
        //设置播放前的最大探测时间
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzemaxduration",100L);
        //设置播放前的探测时间 1,达到首屏秒开效果
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1);
        //播放前的探测Size，默认是1M, 改小一点会出画面更快
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"probesize",1024);  //1024
        //每处理一个packet之后刷新io上下文
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"flush_packets",1L);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"reconnect",5);
//                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-buffer-size",1);
        //跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
//        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"framedrop",5);
        //最大fps
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-fps",60);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);//SeekTo设置优化
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"fflags","fastseek");//设置seekTo能够快速seek到指定位置并播放
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
//                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

//        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
//////        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "an", 1);
//        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"find_stream_info", 0);
//        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
//        //mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", 1);
//                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener completionListener){
        mediaPlayer.setOnCompletionListener(completionListener);
    }

    private boolean isInPlaybackState() {
        return (mediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public void stopMedia(){
        if(mediaPlayer!=null &&mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mCurrentState = STATE_IDLE;
//        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        am.abandonAudioFocus(null);
    }

    public void end(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void start() {
        System.out.println("开始播放声音>>>>>>>>>11111");
        if (isInPlaybackState()) {
            System.out.println("开始播放声音>>>>>>>>>2222");
            mediaPlayer.start();
        }
        mTargetState = STATE_PLAYING;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void playMedia(String url,boolean isLoop) throws IOException {

        if(isInPlaybackState()){
            stopMedia();
        }
        long beginTime = System.currentTimeMillis();
        initMedia();
        System.out.println("初始化player耗时:"+(System.currentTimeMillis()-beginTime));
        this.bufferUrl = url;
        Uri mUri = Uri.parse(url);
        String scheme = mUri.getScheme();
        beginTime = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (TextUtils.isEmpty(scheme) || scheme.equalsIgnoreCase("file"))) {
            IMediaDataSource dataSource = new FileMediaDataSource(new File(mUri.toString()));
            mediaPlayer.setDataSource(dataSource);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mediaPlayer.setDataSource(context, mUri, mHeaders);
        } else {
            mediaPlayer.setDataSource(mUri.toString());
        }
        System.out.println("加载资源耗时:"+(System.currentTimeMillis()-beginTime));
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(false);
        beginTime = System.currentTimeMillis();
        mediaPlayer.prepareAsync();
        System.out.println("准备耗时:"+(System.currentTimeMillis()-beginTime));
        mediaPlayer.setLooping(isLoop);
        mCurrentState = STATE_PREPARING;
        mediaPlayer.setOnPreparedListener(mPreparedListener);
        start();
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {

            MediaSoundPlayer.this.mCurrentState = STATE_PREPARED;

            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            if (mTargetState == STATE_PLAYING) {
                start();
            }

        }
    };

    public void setState(int state){
        this.mCurrentState = state;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && mediaPlayer.isPlaying();
    }

}
