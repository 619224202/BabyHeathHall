package com.swkj.bear.iptvbearandroid.bridge;

import android.webkit.JavascriptInterface;

import com.swkj.babyhall.MainActivity;
import com.swkj.bear.iptvbearandroid.pay.Pay;
import com.swkj.bear.iptvbearandroid.utils.MainHandler;

/**
 * Created by zuixian on 2018/5/17.
 */



public class HostJsScope {

    public MainActivity act;
    private int count;
    public HostJsScope(MainActivity activity){
        this.act = activity;
    }

    @JavascriptInterface
    public void sendAuthRequest(String productId,String contentId,boolean isDebug,String info){
        Pay.sendAuthRequest(isDebug,info);
    }

    @JavascriptInterface
    public void payCycle(final String tradeNo,final String productId,final boolean isDebug,String price){
        System.out.println("pay>>>>>");
        Pay.payCycle(tradeNo,productId,isDebug,act);
    }

    @JavascriptInterface
    public void playMedia(final String url,final String backUrl){
        System.out.println("playVideo:url="+url);
        System.out.println("playVideo:backUrl="+backUrl);
        MainActivity.bufferUrl = backUrl;
        MainHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                act.playVideo(url);
            }
        });

//        if(count%2==0) {
//            playMusic("http://192.168.2.157:8080/test/0.ts", false, false);
//        }else {
//            playMusic("http://192.168.2.157:8080/test/1.ts", false, false);
//        }
//        count++;
//        Pay.payCycle("","",false,act);
    }

    @JavascriptInterface
    public void playMusic(String url,boolean loop,boolean isBg){
        //播放声音,创建一个声音的声音池,声音播放完则销毁
        if(isBg) {
            act.playBgPlayer(url);
        }else{
            act.playEffectPlayer(url);
        }
    }

    @JavascriptInterface
    public void stopMusic(boolean isBg){
        if(isBg){
            act.stopBgPlayer();
        }else{
            act.stopEffectPlayer();
        }
    }

    @JavascriptInterface
    public void ExitGame(){
        System.out.println("ExitGame");

        act.finish();
        System.exit(0);//退出程序
    }
}
