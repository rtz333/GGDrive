package com.daq.smsprint.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daq.smsprint.models.SmsModel;

import java.util.List;

@Dao
public interface SmsDao {

    @Insert
    void insert(SmsModel smsModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SmsModel> smsModels);

    @Query("SELECT * FROM table_smsmodel")
    List<SmsModel> getSmsList();
}
