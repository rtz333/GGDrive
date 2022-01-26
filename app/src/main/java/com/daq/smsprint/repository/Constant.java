package com.daq.smsprint.repository;

public class Constant {

    public static final int REQUEST_CODE_PERMISSION = 1001;
    public static final String[] permissions = {
            "android.permission.INTERNET",
            "android.permission.READ_CALL_LOG",
            "android.permission.READ_CONTACTS",
            "android.permission.WRITE_CALL_LOG",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_SMS",
            "android.permission.READ_CONTACTS",
            "android.permission.RECEIVE_SMS",
            "android.permission.RECEIVE_BOOT_COMPLETED"
    };
}
