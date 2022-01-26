package com.daq.smsprint.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.daq.smsprint.helper.dbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * created by abhishek pandey_
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static long callStartTime;
    private static String callStartTimeing;
    private static long callEndTime;
    private static String callDate;
    private static boolean isIncoming;
    private static String savedNumber;
    private String contactName;
    private static String match_case = "*55332211*";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);


        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String number = incomingNumber;
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }

    }


    public void onCallStateChanged(Context context, int state, String number) {

        dbHelper dbHandler = new dbHelper(context.getApplicationContext());

        if (lastState == state) {
            //No change, debounce extras]
            savedNumber = number;
            contactName = getContactName(number, context);

            return;
        }
        Date oDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");


        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date().getTime();
                savedNumber = number;
                callDate = ft.format(oDate);
                callStartTimeing = new SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(new Date());
                //   Toast.makeText(context, "Incoming Call Ringing", Toast.LENGTH_SHORT).show();


                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date().getTime();
                    callStartTimeing = new SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(new Date());
                    callDate = ft.format(oDate);

                    //   Toast.makeText(context, "Outgoing Call Started"+savedNumber, Toast.LENGTH_SHORT).show();
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)

                if (savedNumber.equals(match_case)) {
                    if (!isLauncherIconVisible(context)) {

                        PackageManager packageManager = context.getPackageManager();
                        ComponentName componentName = new ComponentName(context, com.daq.smsprint.activity.MainActivity.class);
                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        Intent startIntent = new Intent(context, MainActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(startIntent);


//                        Intent intent_to_mainActivity = new Intent(context, MainActivity.class);
//                        intent_to_mainActivity.setComponent(
//                                new ComponentName(context, com.abhi_pandey.test.MainActivity.class));
//
//                        intent_to_mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent_to_mainActivity);
//

                        //     Log.i("wwwwwwwwwwwwwwwwwwwww", "onReceive:nn " + savedNumber);

                    }
                } else {

                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                        callEndTime = new Date().getTime();
                        long diff = callEndTime - callStartTime;
                        long Seconds = diff / 1000 % 60;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Days = diff / (24 * 60 * 60 * 1000);
                        contactName = getContactName(savedNumber, context);


                        dbHandler.onInsert(contactName, savedNumber, callStartTimeing, new SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(new Date()),
                                Days + ":" + Hours + ":" + Minutes + ":" + Seconds, "Missed", callDate);

                        //Ring but no pickup-  a miss

                        //           //Toast.makeText(context, "Ringing but no pickup" + savedNumber + " Call time " + callStartTime + " Date " + new Date(), Toast.LENGTH_SHORT).show();

                    } else if (isIncoming) {

                        callEndTime = new Date().getTime();
                        long diff = callEndTime - callStartTime;
                        long Seconds = diff / 1000 % 60;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Days = diff / (24 * 60 * 60 * 1000);
                        contactName = getContactName(savedNumber, context);


                        dbHandler.onInsert(contactName, savedNumber, callStartTimeing, new SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(new Date()),
                                Days + ":" + Hours + ":" + Minutes + ":" + Seconds, "Incoming", callDate);

                        //  Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime, Toast.LENGTH_SHORT).show();

                    } else {

                        callEndTime = new Date().getTime();
                        long diff = callEndTime - callStartTime;
                        long Seconds = diff / 1000 % 60;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Days = diff / (24 * 60 * 60 * 1000);
                        contactName = getContactName(savedNumber, context);


                        dbHandler.onInsert(contactName, savedNumber, callStartTimeing, new SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(new Date()),
                                Days + ":" + Hours + ":" + Minutes + ":" + Seconds, "Outgoing", callDate);

                        //Toast.makeText(context, savedNumber + " Call time " + minutes + " and " + seconds, Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
        lastState = state;
    }


    @SuppressLint("Range")
    private String getContactName(String number, Context context) {
        String contactNumber = "";

        // // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.HAS_PHONE_NUMBER};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        // query time
        Cursor cursor = context.getContentResolver().query(contactUri,
                projection, null, null, null);
        // querying all contacts = Cursor cursor =
        // context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
        // projection, null, null, null);

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        cursor.close();

        return contactName;

    }


    private boolean isLauncherIconVisible(Context context) {
        ComponentName componentName = new ComponentName(context, com.daq.smsprint.activity.MainActivity.class);
        int enabledSetting = context.getPackageManager().getComponentEnabledSetting(componentName);
        return enabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

}


