package com.daq.smsprint.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.models.SmsModel;

import java.util.List;

@Dao
public interface CallDao {


    @Insert
    void insert(CallLogModel callLogModel);

    @Insert
    void insert(List<CallLogModel> callLogModels);

    @Query("SELECT * FROM table_calllogmodel")
    List<CallLogModel> getCallList();


    @Delete
    void delete(List<CallLogModel> callLogModels);

    @Update
    void update(CallLogModel callLogModel);
}
