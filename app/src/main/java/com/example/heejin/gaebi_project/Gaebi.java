package com.example.heejin.gaebi_project;

import android.app.Application;

import com.example.heejin.gaebi_project.util.VoiceManager;

/**
 * Created by heejin on 2016. 11. 8..
 */
public class Gaebi extends Application {

    public static Gaebi _instance;
    private VoiceManager mVoiceManager= new VoiceManager();
    public Gaebi(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = new Gaebi();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public VoiceManager getmVoiceManager() {
        return mVoiceManager;
    }

}
