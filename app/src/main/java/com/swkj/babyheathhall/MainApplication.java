package com.swkj.babyheathhall;

import android.app.Application;

/**
 * Created by wanglu on 2019/9/12.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("load<<<<<<<<<<<");
        System.loadLibrary("mg20pbase");
    }
}
