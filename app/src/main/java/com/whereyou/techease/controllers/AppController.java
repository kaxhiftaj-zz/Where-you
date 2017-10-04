package com.whereyou.techease.controllers;

import android.app.Application;


/**
 * Created by Zahid on 9/22/17.
 */

public class AppController extends Application {
    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }

    public static AppController getInstance() {
        return mInstance;
    }

    public static AppController context() {
        return mInstance;
    }


}
