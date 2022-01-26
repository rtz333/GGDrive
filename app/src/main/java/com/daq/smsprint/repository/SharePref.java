package com.daq.smsprint.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    private static final String APP = "sms_print_app";
    private static final String IS_FIRST_RUN_APP = "is_first_run_app";

    private static final String KEY_FOLDER_NAME_GOOGLE_DRIVE = "KEY_FOLDER_NAME_GOOGLE_DRIVE";
    private static final String FOLDER_NAME = "SMS Print";
    private static final String IS_GOOGLE_DRIVE_AUTH = "IS_GOOGLE_DRIVE_AUTHascx";
    private static final String GOOGLE_DRIVE_ACCOUNT_NAME = "GOOGLE_DRIVE_ACCOUNT_NAME";

    private final SharedPreferences.Editor editor;
    private final SharedPreferences preferences;
    private String key_folder_id_google_drive = "key_folder_id_google_drive";



    public SharePref(Context context) {
        this.preferences = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    public boolean isFirstRunApp() {
        return preferences.getBoolean(IS_FIRST_RUN_APP, true);
    }

    public void setFirstRunApp(boolean isFirstRunApp) {
        editor.putBoolean(IS_FIRST_RUN_APP, isFirstRunApp);
        editor.apply();
    }


    public String folderNameGoogleDrive() {
        return preferences.getString(KEY_FOLDER_NAME_GOOGLE_DRIVE, FOLDER_NAME);
    }

    public void setFolderNameGoogleDrive(String folderName) {
        editor.putString(KEY_FOLDER_NAME_GOOGLE_DRIVE, folderName);
        editor.apply();
    }

    public String folderIdGoogleDrive() {
        return preferences.getString(key_folder_id_google_drive, null);
    }

    public void setFolderIDGoogleDrive(String folderID) {
        editor.putString(key_folder_id_google_drive, folderID);
        editor.apply();
    }

    public boolean isIsGoogleDriveAuth(){
        return preferences.getBoolean(IS_GOOGLE_DRIVE_AUTH,false);
    }

    public void setGoogleDriveAuth(boolean isAuth){
        editor.putBoolean(IS_GOOGLE_DRIVE_AUTH,isAuth);
        editor.apply();
    }

    public String googleAccountName(){
        return preferences.getString(GOOGLE_DRIVE_ACCOUNT_NAME,null);
    }

    public void setGoogleAccountName(String name){
        editor.putString(GOOGLE_DRIVE_ACCOUNT_NAME,name);
        editor.apply();
    }
}
