package com.daq.smsprint.models;

import android.widget.ImageView;

public class MessageChatModel {
    private String text;
    private String time;
    private boolean isCheck =false;
    private int viewType;
    private String phone;






    public MessageChatModel(String text, String time, int viewType,String phone1) {
        this.text = text;
        this.time = time;
        this.viewType = viewType;
        this.phone=phone1;

    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}