package com.practice.newscollector;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    private static App instance;
    protected AppComponent appComponent;

    public static App getInstance() {
        return instance;
    }

    public Context getAppContext(){
        return this.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.create();
    }

    public void setComponents(AppComponent appComponent){
        this.appComponent = appComponent;
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
