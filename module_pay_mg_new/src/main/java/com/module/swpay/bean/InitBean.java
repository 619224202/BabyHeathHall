package com.module.swpay.bean;

/**
 * Created by wanglu on 2019/1/16.
 */
public class InitBean {
    //游戏名称
    private String gameName;
     //运营商分配的app_id
    private String app_id;
    //运营商分配的app_key
    private String app_key;
    //初始化的时间
    private String initTime;
    //sdk返回的账号
    private String userId;

    private String sdkVersion;
    //初始化的状态
    private int initStatus;
    public final static int INIT_STATUS_READY = 1;
    public final static int INIT_STATUS_SUCCESS = 2;
    public final static int INIT_STATUS_FAILED = 3;

    public InitBean(){
        initStatus = INIT_STATUS_READY;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public int getInitStatus() {
        return initStatus;
    }

    public void setInitStatus(int initStatus) {
        this.initStatus = initStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
}
