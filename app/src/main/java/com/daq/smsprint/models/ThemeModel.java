package com.daq.smsprint.models;

public class ThemeModel {
    private String backgroundName;
    private int backgroundDrawble;

    public ThemeModel(String backgroundName, int backgroundDrawble) {
        this.backgroundName = backgroundName;
        this.backgroundDrawble = backgroundDrawble;
    }

    public String getBackgroundName() {
        return backgroundName;
    }

    public int getBackgroundDrawble() {
        return backgroundDrawble;
    }

    public void setBackgroundDrawble(int backgroundDrawble) {
        this.backgroundDrawble = backgroundDrawble;
    }

    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }
}
