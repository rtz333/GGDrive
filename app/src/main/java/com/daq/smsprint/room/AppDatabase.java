package com.daq.smsprint.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.models.SmsModel;


import androidx.room.TypeConverters;

import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.models.SmsModel;
import com.daq.smsprint.util.Converter;

@Database(entities = {SmsModel.class, CallLogModel.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database_sms_print";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public abstract SmsDao smsDao();
    public abstract CallDao callDao();
}
