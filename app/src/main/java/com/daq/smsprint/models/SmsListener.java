package com.daq.smsprint.models;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;

import androidx.core.app.ActivityCompat;

import com.daq.smsprint.helper.dbHelper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * created by abhishek pandey_
 */

public class SmsListener extends BroadcastReceiver {
    private dbHelper dbHandler;
        public static final String SMS_BUNDLE = "pdus";
        private String smsThread;
        private String smsContent;
        private String smsDate;
        private String smsName;
        private String smsNumber;
        private String smsTime;

          @Override
        public void onReceive(Context context, Intent intent) {

              if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                  return;
              }

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {


                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        //String smsBody = messages[i].getDisplayMessageBody().toString();
                        String address = messages[i].getOriginatingAddress();
                        long date = messages[i].getTimestampMillis();
                        String dateText = new SimpleDateFormat("yyyy-MM-dd").format(new Date(date));

                        smsName = getContactName(context.getApplicationContext(), address);
                        smsNumber = address;
                        smsDate = dateText;
                        smsTime = (String) DateFormat.format("h:mm:ss aa", new Time(date));
                        smsThread = messages[i].getDisplayOriginatingAddress();
                    }

                        if (messages.length > 0) {
                            StringBuffer content = new StringBuffer();
                            for (SmsMessage sms : messages)
                                content.append(sms.getDisplayMessageBody());
                            smsContent = content.toString();
                        }

                        String smsType="inbox" ;

                        dbHelper dbHandler = new dbHelper(context.getApplicationContext());

                        dbHandler.onInsertSms(smsThread,smsName, smsNumber, smsType, smsContent, smsTime, smsDate);

                }
            }

        }

        @SuppressLint("Range")
        public String getContactName(Context context, String phoneNumber) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)), new String[]{"display_name"}, (String) null, (String[]) null, (String) null);
            if (cursor == null) {
                return null;
            }
            String contactName = "Unknown";
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex("display_name"));
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return contactName;
        }

    }



