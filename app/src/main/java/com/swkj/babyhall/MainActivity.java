package com.swkj.babyhall;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.swkj.babyheathhall.R;
import com.swkj.bear.iptvbearandroid.activity.PlayActivity;
import com.swkj.bear.iptvbearandroid.bridge.HostJsScope;
import com.swkj.bear.iptvbearandroid.pay.IptvPay;
import com.swkj.bear.iptvbearandroid.pay.Pay;
import com.swkj.bear.iptvbearandroid.utils.BackgroundMusic;
import com.swkj.bear.iptvbearandroid.utils.MainHandler;
import com.swkj.bear.iptvbearandroid.utils.MediaSoundPlayer;
import com.swkj.bear.iptvbearandroid.views.LoadingView;

import java.io.IOException;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends Activity {

    //http://202.99.114.74:53980
//    private String url = "http://192.168.2.221:8088/IptvClassLTA/jsp/login_lt.jsp?UserID=ww222222222223332s&UserToken=wws&StbVendor=&ReturnUrl=&resolution=hd";
//    http://112.33.16.34:18080/file/bbdy.ts
    private String url = "http://112.33.16.34:8081/IptvEDULT/jsp/login_hwtjlt.jsp?UserID=ww232222321s&UserToken=wws1&StbVendor=&ReturnUrl=&resolution=hd";

    public static boolean isFirstLogin = true;
    public static String bufferUrl;

    private WebView mWebView;

    private LoadingView mLoadingView;

    private ImageView exitImage;

    private ImageView rightImage;
    private ImageView leftImage;

    public static boolean isFirst = true;

    public static boolean isIntroduce = true;

    private boolean isGameStart;

    private boolean isLoadOver = false;

    private boolean loadLibaryOver = false;


    private MediaSoundPlayer bgPlayer;
    private MediaSoundPlayer effectPlayer;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    MainActivity.this.mLoadingView.setVisibility(View.INVISIBLE);
                    Log.v("AAA","loadOver>>>>>>");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BackgroundMusic.getInstance(this).playBackgroundMusic("bgm.mp3",true);

        if (isFirst) {
            isFirst = false;
            IptvPay.InitPay(this);
            IptvPay.setMainActivity(this);
            Pay.sendAuthRequest(false,"");
        }

        mLoadingView = findViewById(R.id.mLoadingView);
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.setProgress(0);
        exitImage = findViewById(R.id.exit);
        rightImage = findViewById(R.id.right);
        leftImage = findViewById(R.id.left);
        initMediaPlayer();
//        mediaPlayer = findViewById(R.id.ijkview);
        showEndGame(false);
//        playBgPlayer("http://192.168.2.157:8080/test/Hwelcome.ts");
//        setPermission();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int p = mLoadingView.getProgress();
                while (true){
                    if(p<100){
                        p++;
                        if(p>=100){
                            p=100;
                        }
                        final int per = p;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingView.setProgress(per);
                            }
                        });
                    }
                    if(p>=100 && isLoadOver){
                        break;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                while (p != 100){
//                    try {
//                        Thread.sleep(30);
//                        p=p+1;
//                        mLoadingView.setProgress(p);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                if(p == 100){

                    isGameStart = true;
                    Message msg = new Message();
                    msg.what = 1;
                    uiHandler.sendMessage(msg);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mLoadingView.setVisibility(View.INVISIBLE);
//                        }
//                    });
                }
            }
        });
        thread.start();


//        animation = AnimationUtils.loadAnimation(this,R.anim.translate);
//        animation.setAnimationListener(new Animation.AnimationListener(){
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                cloud1Image.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        animation1 = AnimationUtils.loadAnimation(this,R.anim.translate1);
//        animation1.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                cloud2Image.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
        init();
    }

    public void init() {
        mWebView = findViewById(R.id.webView1);
        if(isFirstLogin){
            bufferUrl = url;
            isFirstLogin = false;
        }
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+";Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        webSettings.setAppCacheEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                MainActivity.this.mLoadingView.setVisibility(View.INVISIBLE);
                if(!isLoadOver) {
                    isLoadOver = true;
                    callJavaScript();
                }
                //设定加载结束的操作
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        mWebView.addJavascriptInterface(new HostJsScope(this), "AndroidPay");
//        String dUrl = "http://202.99.114.74:54004/IptvBearAndroid/jsp/login_lt.jsp?UserID="+IptvPay.useID+"&businessID="+IptvPay.mac+"&UserToken=wws&StbVendor=&ReturnUrl=&resolution=hd";
//        String dUrl = "http://202.99.114.28:2380/IptvBearAndroid/jsp/login_lt.jsp?UserID="+IptvPay.useID+"&businessID="+IptvPay.mac+"&UserToken=wws&StbVendor=&ReturnUrl=&resolution=hd";
       // String dUrl = "http://192.168.2.222:8088/IptvBearA/jsp/login_lt.jsp?UserID="+IptvPay.useID+"&businessID="+IptvPay.mac+"&UserToken=wws&StbVendor=&ReturnUrl=&resolution=hd";
        //        mWebView.loadUrl(dUrl);
        System.out.println("bufferUrl="+bufferUrl);
//        mWebView.loadUrl(bufferUrl);
        doWebUrl(bufferUrl);
    }

    public void callJavaScript(){
        doWebUrl("javascript:goLogin()");
//        mWebView.loadUrl("javascript:goLogin()");
    }

    public void toMonitorCompanyList(final String userid,String isPass,final String propId,final String info){
//        isPass = "1";
        isIntroduce = Integer.valueOf(isPass) == 0?true:false;
//        mWebView.loadUrl("javascript:toMonitorCompanyList('"+userid+"','"+isPass+"','"+info+"','"+propId+"','"+0+"')");
        doWebUrl("javascript:toMonitorCompanyList('"+userid+"','"+isPass+"','"+info+"','"+propId+"','"+0+"')");
    }

    public void callBackPayCycle(String tradeNo,String uid,String tradeStatus){
        doWebUrl("javascript:orderBack("+tradeStatus+")");
//        doWebUrl("javascript:getOrderBackList('"+uid+"','"+tradeStatus+"','"+tradeNo+"')");
//        mWebView.loadUrl("javascript:getOrderBackList('"+uid+"','"+tradeStatus+"','"+tradeNo+"')");
    }

//    public void callBackPayCycle(String tradeNo,String uid,String tradeStatus){
//        doWebUrl("javascript:getOrderBackList('"+uid+"','"+tradeStatus+"','"+tradeNo+"')");
////        mWebView.loadUrl("javascript:getOrderBackList('"+uid+"','"+tradeStatus+"','"+tradeNo+"')");
//    }

    private void showEndGame(boolean isShow) {
        if (isShow) {
            exitImage.setVisibility(View.VISIBLE);
            leftImage.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.INVISIBLE);
        }else {
            exitImage.setVisibility(View.INVISIBLE);
            leftImage.setVisibility(View.INVISIBLE);
            rightImage.setVisibility(View.INVISIBLE);
        }
    }

    public void playVideo(String url) {
        System.out.println("playVideo" + url);
//        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        stopAll();
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra("url", url);

        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(IptvPay.b_payLock){
            return true;
        }
        if (!isGameStart) {
            return true;
        }

        //改写物理返回键的逻辑
        if(keyCode== KeyEvent.KEYCODE_BACK) {
//            mWebView.loadUrl("javascript:back()");
            doWebUrl("javascript:back()");
            return true;
        }
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

    @Override
    protected void onDestroy() {
        BackgroundMusic.getInstance(this).end();
        if(bgPlayer!=null){
            bgPlayer.end();
        }
        bgPlayer = null;
        if(effectPlayer!=null){
            effectPlayer.end();
        }
        effectPlayer = null;
        super.onDestroy();
    }

    public void doWebUrl(final String webUrl){
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
        if(mWebView!=null) {
            mWebView.loadUrl(webUrl);
        }
            }
        });
    }

    public void initMediaPlayer(){
        if(!loadLibaryOver){
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            loadLibaryOver = true;
        }
        bgPlayer = new MediaSoundPlayer(this);
        effectPlayer = new MediaSoundPlayer(this);
//        bgPlayer.initMedia();
//        effectPlayer.initMedia();

//        mVideoUri = Uri.parse("http://112.33.16.34:18080/ts/bbdy.ts");
    }

    public void playBgPlayer(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (bgPlayer) {
                    if (bgPlayer.isPlaying()) {
                        bgPlayer.stopMedia();
                    }
                    try {
                        bgPlayer.playMedia(url, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void playEffectPlayer(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (effectPlayer) {
                    if (effectPlayer.isPlaying()) {
                        effectPlayer.stopMedia();
                    }
                    try {
                        effectPlayer.playMedia(url, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //effectPlayer.setLooping(true);
    }

    public void stopEffectPlayer(){
        if(effectPlayer!=null){
            effectPlayer.stopMedia();
        }
        //effectPlayer.setLooping(true);
    }

    public void stopBgPlayer(){

        if(bgPlayer!=null){
            bgPlayer.stopMedia();
        }
    }

    public void stopAll(){
        stopBgPlayer();
        stopEffectPlayer();
    }


}