package com.daq.smsprint.models;

import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.daq.smsprint.util.Converter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "table_smsmodel")
public class SmsModel {

     @PrimaryKey(autoGenerate = true)
     private int rowID;

     public int getRowID() {
          return rowID;
     }

     public void setRowID(int rowID) {
          this.rowID = rowID;
     }

     private String id,threadId, smsDate, smsTime, smsName,smsAddress, smsType,smsContent, appName, strBody;


     @TypeConverters(Converter.class)
     private List<MessageChatModel> messageChatModelList;

     public String getStrBody() {
          return strBody;
     }

     public void setStrBody(String strBody) {
          this.strBody = strBody;
     }

     public String getAppName() {
          return appName;
     }

     public void setAppName(String appName) {
          this.appName = appName;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getThreadId() {
          return threadId;
     }

     public void setThreadId(String threadId) {
          this.threadId = threadId;
     }

     public String getSmsDate() {
          return smsDate;
     }

     public void setSmsDate(String smsDate) {
          this.smsDate = smsDate;
     }

     public String getSmsTime() {
          return smsTime;
     }

     public void setSmsTime(String smsTime) {
          this.smsTime = smsTime;
     }

     public String getSmsName() {
          return this.smsName;
     }

     public void setSmsName(String smsName) {
          this.smsName = smsName;
     }

     public String getSmsAddress() {
          return this.smsAddress;
     }

     public void setSmsAddress(String smsAddress) {
          this.smsAddress = smsAddress;
     }

     public String getSmsType() {
          return smsType;
     }

     public void setSmsType(String smsType) {
          this.smsType = smsType;
     }

     public String getSmsContent() {
          return smsContent;
     }

     public void setSmsContent(String smsContent) {
          this.smsContent = smsContent;
     }




     public List<MessageChatModel> getMessageChatModelList() {
          return messageChatModelList;
     }

     public void setMessageChatModelList(List<MessageChatModel> messageChatModelList) {
          this.messageChatModelList = messageChatModelList;
     }

     public SmsModel(String id, String threadId, String smsDate, String smsTime, String smsName, String smsAddress, String smsType, String smsContent, List<MessageChatModel> messageChatModelList) {

          this.id = id;
          this.threadId = threadId;
          this.smsDate = smsDate;
          this.smsTime = smsTime;
          this.smsName = smsName;
          this.smsAddress = smsAddress;
          this.smsType = smsType;
          this.smsContent = smsContent;

          this.messageChatModelList = messageChatModelList;

     }
}
