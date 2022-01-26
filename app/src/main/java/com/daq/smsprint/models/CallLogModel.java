package com.daq.smsprint.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;



import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.daq.smsprint.util.Converter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "table_calllogmodel")
public class CallLogModel {

    @PrimaryKey(autoGenerate = true)
    private int rowId;

    private String  id,callName, callNumber,callEndTime, callDuration, callType,callDate,callAddress;

    @ColumnInfo(name = "start_time")
    private String callStartTime;




    private String nameBackGround;
    private int drawbleBackGround;

    public byte[] getImageImport() {
        return imageImport;
    }

    public void setImageImport(byte[] imageImport) {
        this.imageImport = imageImport;
    }

    private byte[] imageImport;

    public String getNameBackGround() {
        return nameBackGround;
    }

    public void setNameBackGround(String nameBackGround) {
        this.nameBackGround = nameBackGround;
    }

    public int getDrawbleBackGround() {
        return drawbleBackGround;
    }

    public void setDrawbleBackGround(int drawbleBackGround) {
        this.drawbleBackGround = drawbleBackGround;
    }

    @TypeConverters(Converter.class)
    private ArrayList<CallInfoModel> callInfoModels;

    public CallLogModel(String id, String callName, String callNumber, String callStartTime, String callEndTime, String callDuration, String callType, String callDate, ArrayList<CallInfoModel> callInfoModels) {
        this.id = id;
        this.callName = callName;
        this.callNumber = callNumber;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.callDuration = callDuration;
        this.callType = callType;
        this.callDate = callDate;

        this.callInfoModels = callInfoModels;
        this.nameBackGround = "white";
        this.imageImport = new byte[]{};
    }

    public ArrayList<CallInfoModel> getCallInfoModels() {
        return callInfoModels;
    }

    public void setCallInfoModels(ArrayList<CallInfoModel> callInfoModels) {
        this.callInfoModels = callInfoModels;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }


    public String getCallAddress() {
        return callAddress;
    }

    public void setCallAddress(String callAddress) {
        this.callAddress = callAddress;
    }


    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(String callStartTime) {
        this.callStartTime = callStartTime;
    }

    public String getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }
}
