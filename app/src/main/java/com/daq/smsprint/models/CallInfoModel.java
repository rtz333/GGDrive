package com.daq.smsprint.models;


public class CallInfoModel {
    private String startTime, endTime, Duration, Date, Type, number, name;
    private boolean check = false;
    private Long longDate;

    public CallInfoModel(String startTime, String endTime, String duration, String date, String type, String number, String name) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.Duration = duration;
        this.Date = date;
        this.Type = type;
        this.number = number;
        this.name = name;
    }

    public Long getLongDate() {
        return longDate;
    }

    public void setLongDate(Long longDate) {
        this.longDate = longDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
