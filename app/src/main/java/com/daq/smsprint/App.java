package com.daq.smsprint;

import android.app.Application;
import android.content.Context;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.repository.Repository;
import com.daq.smsprint.repository.SharePref;
import com.daq.smsprint.room.AppDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App extends Application {

    public static Context applicationContext;
    public static SharePref sharePref;
    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        sharePref = new SharePref(applicationContext);
        appDatabase = AppDatabase.getInstance(applicationContext);


        Executor executor = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            Repository.smsList = appDatabase.smsDao().getSmsList();
            Repository.callLogList = appDatabase.callDao().getCallList();
        });
    }
}
