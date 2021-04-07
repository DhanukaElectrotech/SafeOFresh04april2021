package com.dhanuka.morningparcel;

import android.app.Application;


/**
 * 作者: Sunshine
 * 时间: 2017/4/14.
 * 邮箱: 44493547@qq.com
 * 描述:
 */

public class App extends Application {

    private static App app;
     @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getInstance(){
        return app;
    }



}
