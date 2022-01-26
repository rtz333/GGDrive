package com.daq.smsprint.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andexert.library.RippleView;
import com.daq.smsprint.App;
import com.daq.smsprint.R;
import com.daq.smsprint.adapter.CallAdapter;
import com.daq.smsprint.adapter.SmsLogAdapter;
import com.daq.smsprint.databinding.ActivityMainBinding;
import com.daq.smsprint.helper.dbHelper;
import com.daq.smsprint.interfaces.CallLogClick;
import com.daq.smsprint.interfaces.SmsClick;
import com.daq.smsprint.models.CallInfoModel;
import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.models.MessageChatModel;
import com.daq.smsprint.models.SmsModel;
import com.daq.smsprint.repository.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements CallLogClick, SmsClick {
    public static final String TAG = "nuceince";
    private dbHelper dbHandler;
    String callName, callNumber, callStartTime, callEndTime, call_access_Date, callDuration, callType, callDate;
    private ActivityMainBinding binding;
    private SmsLogAdapter smsAdapter;
    private CallAdapter callAdapter;

    private String threadId, smsDate, smsTime, smsName, smsAddress, smsType, smsContent;
    private String lastSmsId = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new dbHelper(MainActivity.this);
        clickHandler();


        initData();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() {
        ProgressDialog dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {

            if (App.sharePref.isFirstRunApp()) {
                getFetchCallLogs();
                getAllSms(App.applicationContext);
                getCallLog();
                getSms();
                App.sharePref.setFirstRunApp(false);
            }


            handler.post(() -> {

                callAdapter = new CallAdapter(Repository.callLogList, MainActivity.this, this);
                binding.rvCall.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.rvCall.setAdapter(callAdapter);
            });

            handler.post(() -> {
                smsAdapter = new SmsLogAdapter(Repository.smsList, MainActivity.this, this);
                binding.rvSms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.rvSms.setAdapter(smsAdapter);
                dialog.dismiss();
            });

        });
    }

    @Override
    public void onSmsClick(SmsModel smsModel, int position) {

    }

    private void clickHandler() {
        binding.btnCall.setOnRippleCompleteListener(rippleView -> {
            binding.rvCall.setVisibility(View.GONE);
            binding.rvCall.setVisibility(View.VISIBLE);
            binding.btnSms.setBackgroundResource(R.drawable.ic_bg_call_logs);
            binding.tvSms.setTextColor(getColor(R.color.call));
            binding.btnCall.setBackgroundResource(R.drawable.ic_bg_sms);
            binding.tvCalls.setTextColor(getColor(R.color.white));

            binding.rvSms.setVisibility(View.GONE);
            binding.rvCall.setVisibility(View.VISIBLE);
            callAdapter.setList(Repository.callLogList);
        });


        binding.btnCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SmsActivity.class));
            }
        });

        binding.btnSms.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                binding.btnSms.setBackgroundResource(R.drawable.ic_bg_sms);
                binding.tvSms.setTextColor(Color.WHITE);
                binding.btnCall.setBackgroundResource(R.drawable.ic_bg_call_logs);
                binding.tvCalls.setTextColor(getColor(R.color.call));
                binding.rvCall.setVisibility(View.GONE);
                binding.rvSms.setVisibility(View.VISIBLE);
                smsAdapter.setList(Repository.smsList);

            }
        });

        binding.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

    }

    private void getCallLog() {
        String fetech = "Contacts";

        Cursor cursor = dbHandler.readtablelData(fetech);
        Repository.callLogList.clear();
        while (cursor.moveToNext()) {
            ArrayList<CallInfoModel> callInfoModels = new ArrayList<>();
            Cursor callInfoCursor = dbHandler.redinfocall(cursor.getString(2));
            while (callInfoCursor.moveToNext()) {
                CallInfoModel call = new CallInfoModel(callInfoCursor.getString(3), callInfoCursor.getString(4), callInfoCursor.getString(5), callInfoCursor.getString(6), callInfoCursor.getString(7), callInfoCursor.getString(2), callInfoCursor.getString(1));
                if (call.getDate() != null) {
                    callInfoModels.add(call);
                }
            }
            callInfoCursor.close();
            CallLogModel call = new CallLogModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    , cursor.getString(4), cursor.getString(5), cursor.getString(7), cursor.getString(6), callInfoModels);
            Repository.callLogList.add(call);
            Repository.callInfoList.addAll(callInfoModels);
            Log.d(TAG, "callInfoList: " + Repository.callInfoList.size());
        }
        App.appDatabase.callDao().insert(Repository.callLogList);
        Log.d(TAG, "callLogList: " + Repository.callLogList.size());
        cursor.close();

    }


    private void getSms() {
        SmsObserver smsObserver = new SmsObserver(null);
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms/"),
                true, smsObserver);

        Cursor cursor = dbHandler.readsmstablelData();
        while (cursor.moveToNext()) {

            List<MessageChatModel> messageChatModelList = new ArrayList<>();
            Cursor messageChatCursor = dbHandler.redinfosms(cursor.getString(2));
            while (messageChatCursor.moveToNext()) {

                if (messageChatCursor.getString(3).equals("inbox")) {

                    String dateTime = messageChatCursor.getString(6) + "(" + messageChatCursor.getString(5) + ")";

                    MessageChatModel data = new MessageChatModel(
                            messageChatCursor.getString(4),
                            messageChatCursor.getString(6),
                            1,
                            cursor.getString(2)
                    );
                    messageChatModelList.add(data);

                } else if (messageChatCursor.getString(3).equals("sent")) {

                    String dateTime = messageChatCursor.getString(6) + "(" + messageChatCursor.getString(5) + ")";


                    MessageChatModel data = new MessageChatModel(
                            messageChatCursor.getString(4),
                            messageChatCursor.getString(6),
                            0,
                            cursor.getString(2)
                    );
                    messageChatModelList.add(data);

                } else {

                    String dateTime = messageChatCursor.getString(6) + "(" + messageChatCursor.getString(5) + ")";


                    MessageChatModel data = new MessageChatModel(
                            messageChatCursor.getString(4),
                            messageChatCursor.getString(6),
                            2,
                            cursor.getString(2)
                    );
                    messageChatModelList.add(data);

                }

            }
            messageChatCursor.close();

            SmsModel sms = new SmsModel(cursor.getString(7), cursor.getString(0), cursor.getString(6), cursor.getString(5), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4), messageChatModelList);
            Repository.smsList.add(sms);
            Repository.messageChatModelList.addAll(messageChatModelList);
            Log.d(TAG, "messageChatModelList: " + Repository.messageChatModelList.size());
        }
        App.appDatabase.smsDao().insert(Repository.smsList);
        Log.d(TAG, "smsList: " + Repository.smsList.size());
        cursor.close();
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getFetchCallLogs() {
        dbHandler = new dbHelper(MainActivity.this);
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "date DESC");
        while (cursor.moveToNext()) {

            callNumber = cursor.getString(cursor.getColumnIndex("number"));
            String string = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            callName = string;


            callName = (string == null || string.equals("")) ? nameCall(this, callNumber) : MainActivity.this.callName;
            callType = cursor.getString(cursor.getColumnIndex("type"));
            call_access_Date = cursor.getString(cursor.getColumnIndex("date"));
            Duration ofDays = Duration.ofDays((long) cursor.getColumnIndex("duration"));

            callDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(this.call_access_Date)));

            callStartTime = new SimpleDateFormat("h:mm:ss a").format(new Date(Long.parseLong(this.call_access_Date)));

            switch (Integer.parseInt(callType)) {
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
                case CallLog.Calls.VOICEMAIL_TYPE:
                    callType = "Voicemail";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    callType = "Rejected";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    callType = "Blocked";
                    break;
                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                    callType = "Externally Answered";
                    break;
                default:
                    callType = "NA";
                    break;
            }
            @SuppressLint("Range") int s = (int) cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));

            int sec = s % 60;
            int min = (s / 60) % 60;
            int hours = (s / 60) / 60;

            callDuration = hours + ":" + min + ":" + sec;

            callEndTime = " ";

            dbHandler.onInsert(callName, callNumber, callStartTime, callEndTime, callDuration, callType, call_access_Date);

        }
        cursor.close();
    }

    @SuppressLint("Range")
    public void getAllSms(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Date dateFormat = new Date(Long.valueOf(smsDate));
                    String type = null;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_DRAFT:
                            type = "draft";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_FAILED:
                            type = "failed";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_QUEUED:
                            type = "wating";
                            break;
                        default:
                            break;
                    }
                    threadId = c.getString(c.getColumnIndex("thread_id"));

                    SimpleDateFormat dateForma = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat time = new SimpleDateFormat("h:mm:ss a");
//                    smsDate = dateForma.format(dateFormat);
                    smsTime = time.format(dateFormat);

                    smsName = getContactName(context, number);
                    smsAddress = number;
                    smsType = type;
                    smsContent = body;

                    dbHandler.onInsertSms(threadId, smsName, smsAddress, smsType, smsContent, smsTime, smsDate);
                    c.moveToNext();
                }
            }
            c.close();
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

    @SuppressLint("Range")
    protected void getOutgoingSMS() {
//        Log.i(TAG, "called getOutgoingSMS");

        Context context = getApplicationContext();

        Uri uriSMSURI = Uri.parse("content://sms/");
        String selection = "type = '2'"; //Selection parameter to only select messages in sent folder

        Cursor cur = this.getContentResolver().query(uriSMSURI, null, selection,
                null, null);

        // point to the first record (the last SMS message sent)
        cur.moveToNext();

        //Return if message has already been logged, else print to logcat
        String id = cur.getString(cur.getColumnIndex("_id"));
        if (id.equals(lastSmsId)) {
//            Log.i(TAG, "message already logged, returning");
            return;
        } else {
            String address = cur.getString(cur.getColumnIndex("address"));
            String name = cur.getString(cur.getColumnIndex("person"));
            String date = cur.getString(cur.getColumnIndex("date"));
            String msg = cur.getString(cur.getColumnIndex("body"));
            String status = cur.getString(cur.getColumnIndex("type"));
            threadId = cur.getString(cur.getColumnIndex("thread_id"));

            Date dateFormat = new Date(Long.valueOf(date));
            DateFormat dateForma = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat time = new SimpleDateFormat("h:mm:ss a");

            smsName = getContactName(context.getApplicationContext(), address);
            smsAddress = address;
            smsDate = dateForma.format(dateFormat);
            smsTime = time.format(dateFormat);
            smsContent = msg;
            smsType = "sent";

            dbHandler.onInsertSms(threadId, smsName, smsAddress, smsType, smsContent, smsTime, smsDate);

            // update the last SMS id
            lastSmsId = id;
        }
    }

    @Override
    public void onClickCallLog(CallLogModel callLogModel, int position) {
        Repository.callLogModel = callLogModel;
        startActivity(new Intent(this, CallInfoData.class));

    }


    class SmsObserver extends ContentObserver {
        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getOutgoingSMS();
        }
    }

    @SuppressLint("Range")
    public static String nameCall(Context context, String number) {
        String res = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            Cursor c = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    res = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                }
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res == null ? "Unknown" : res;
    }

}
