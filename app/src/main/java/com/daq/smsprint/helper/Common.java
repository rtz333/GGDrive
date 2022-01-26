package com.daq.smsprint.helper;

import android.net.Uri;

import java.util.regex.Pattern;

public class Common {

    public static final String ANDROID_MESSAGES_APP_PACKAGE_NAME = "com.google.android.apps.messaging";
    public static final String APPLICATION_NAME = "SMSBackupRestore";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long DEFAULT_TRANSITION_RESOLUTION = 604800000;
    public static final String DELETE_AFTER_PROCESSING_FINISHED = "delete_after_processing_finished";
    private static final Uri FAQ_URL = Uri.parse("http://synctech.com.au/sms-faqs/");
    public static final String NOTIFICATION_INTENT_FILE_OPERATION = "NotificationIntentFileOperation";
    public static final String NOTIFICATION_INTENT_OPERATION_RESULT = "NotificationIntentOperationResult";
    public static final String NOTIFICATION_MESSAGE_EXTRA_NAME = "NotificationMessage";
    public static final String NOTIFICATION_TITLE_EXTRA_NAME = "NotificationTitle";
    private static final Pattern NUMBER_CLEANER_PATTERN = Pattern.compile("[\\- ()]");
    private static final Pattern NUMBER_CLEANER_PATTERN_FOR_DUPLICATES = Pattern.compile("[\\-+ ()]");
    private static final Boolean UPLOAD_ON_WIFI_DEFAULT = Boolean.TRUE;
    public static final String UTF8_ENCODING = "UTF-8";

    public static String cleanPhoneNumberForDuplicateSearch(String str) {
        return str == null ? "" : NUMBER_CLEANER_PATTERN_FOR_DUPLICATES.matcher(str).replaceAll("");
    }


}
