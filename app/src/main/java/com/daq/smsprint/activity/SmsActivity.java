package com.daq.smsprint.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andexert.library.RippleView;
import com.daq.smsprint.App;
import com.daq.smsprint.R;
import com.daq.smsprint.adapter.CallAdapter;
import com.daq.smsprint.adapter.SmsLogAdapter;
import com.daq.smsprint.databinding.ActivityPrintSmsBinding;
import com.daq.smsprint.dialog.FilterDialog;
import com.daq.smsprint.helper.dbHelper;
import com.daq.smsprint.interfaces.CallLogClick;
import com.daq.smsprint.interfaces.FilterClick;
import com.daq.smsprint.interfaces.SmsClick;
import com.daq.smsprint.models.CallInfoModel;
import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.models.SmsModel;
import com.daq.smsprint.repository.Repository;
import com.daq.smsprint.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * created by abhishek pandey_
 */

public class SmsActivity extends AppCompatActivity implements CallLogClick, SmsClick {

    private dbHelper dbHandler;
    private ActivityPrintSmsBinding binding;
    FilterClick filterClick;

    private String threadId, smsDate, smsTime, smsName, smsAddress, smsType, smsContent;
    private ArrayList<SmsModel> smsMolder = new ArrayList<>();
    private String lastSmsId = "";
    CallAdapter callAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrintSmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new dbHelper(SmsActivity.this);
        SmsLogAdapter adapter = new SmsLogAdapter(Repository.smsList, getApplicationContext(), this);
        binding.rvChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvChat.setAdapter(adapter);
        callAdapter = new CallAdapter(Repository.callLogList, SmsActivity.this, this);
        binding.rvCall.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvCall.setAdapter(callAdapter);

        buttonClick(this);
    }

    private void buttonClick(SmsClick smsClick) {
        binding.btnSms.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                binding.rvChat.setVisibility(View.VISIBLE);
                binding.rvCall.setVisibility(View.GONE);
                binding.btnSms.setBackgroundResource(R.drawable.ic_bg_sms);
                binding.tvSms.setTextColor(Color.WHITE);
                binding.btnCall.setBackgroundResource(R.drawable.ic_bg_call_logs);
                binding.tvCall.setTextColor(getColor(R.color.call));

//                smsMolder.clear();
//                Cursor cursor = dbHandler.readsmstablelData();
//                while (cursor.moveToNext()) {
//                    SmsModel sms = new SmsModel(cursor.getString(7), cursor.getString(0), cursor.getString(6), cursor.getString(5), cursor.getString(1)
//                            , cursor.getString(2), cursor.getString(3), cursor.getString(4));
//                    smsMolder.add(sms);
//                }
//                cursor.close();
                SmsLogAdapter adapter = new SmsLogAdapter(Repository.smsList, getApplicationContext(), smsClick);
                binding.rvChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.rvChat.setAdapter(adapter);
            }
        });

        binding.btnCall.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                binding.rvChat.setVisibility(View.GONE);
                binding.rvCall.setVisibility(View.VISIBLE);
                binding.btnSms.setBackgroundResource(R.drawable.ic_bg_call_logs);
                binding.tvSms.setTextColor(getColor(R.color.call));
                binding.btnCall.setBackgroundResource(R.drawable.ic_bg_sms);
                binding.tvCall.setTextColor(getColor(R.color.white));
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        binding.btnRefresh.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                ProgressDialog dialog = ProgressDialog.show(SmsActivity.this, "", "Loading. Please wait...", true);
                Executor executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute((() -> {
                    App.appDatabase.callDao().delete(Repository.callLogList);
                    getCallLog();
                    handler.post((() -> {
                        callAdapter = new CallAdapter(Repository.callLogList, SmsActivity.this, new CallLogClick() {
                            @Override
                            public void onClickCallLog(CallLogModel callLogModel, int position) {
                                Repository.callLogModel = callLogModel;
                                startActivity(new Intent(SmsActivity.this, CallInfoData.class));
                            }
                        });
                        binding.rvCall.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.rvCall.setAdapter(callAdapter);
                        dialog.dismiss();
                    }));
                }));

            }
        });
        binding.btnFilter.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                binding.btnUnFilter.setVisibility(View.VISIBLE);
                FilterDialog filterDialog = new FilterDialog(SmsActivity.this, new FilterClick() {
                    @Override
                    public void onFilterApply(String date) {
                        ArrayList<CallLogModel> filteredList = new ArrayList<>();

                        for (CallLogModel item : Repository.callLogList) {

                            for (CallInfoModel callInfoModel : item.getCallInfoModels()) {

                                if (callInfoModel.getDate().contains(date)) {
                                    filteredList.add(item);
                                }
                            }

                        }
                        callAdapter.setData(filteredList);

                    }
                });
                filterDialog.show();
                filterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

            }
        });
        binding.btnUnFilter.setOnRippleCompleteListener(rippleView -> {
            binding.btnFilter.setVisibility(View.VISIBLE);
            binding.btnUnFilter.setVisibility(View.GONE);
            callAdapter.setData(Repository.callLogList);
        });
    }

    @Override
    public void onSmsClick(SmsModel smsModel, int position) {
        Repository.smsModel = smsModel;
        startActivity(new Intent(this, SmsLogDetails.class));
    }

    private void filter(String text) {
        ArrayList<CallLogModel> filteredList = new ArrayList<>();
        ArrayList<CallLogModel> originList = new ArrayList<>(Repository.callLogList);
        for (CallLogModel item : originList) {
            if (item.getCallName() == null) {
                continue;
            }
            if (item.getCallName().toLowerCase().contains(text.toLowerCase()) || item.getCallNumber().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        }
        callAdapter.setData(filteredList);
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
        }
        App.appDatabase.callDao().insert(Repository.callLogList);
        cursor.close();

    }


    @Override
    public void onClickCallLog(CallLogModel callLogModel, int position) {
        Repository.callLogModel = callLogModel;
        startActivity(new Intent(this, CallInfoData.class));
    }
}







