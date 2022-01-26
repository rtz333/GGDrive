package com.daq.smsprint.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.daq.smsprint.adapter.SmsCardAdapter;
import com.daq.smsprint.databinding.ActivityDetailsCallLogBinding;
import com.daq.smsprint.helper.dbHelper;
import com.daq.smsprint.models.MessageChatModel;
import com.daq.smsprint.models.SmsModel;
import com.daq.smsprint.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class SmsLogDetails extends AppCompatActivity {

    SmsCardAdapter adapter;
    private ActivityDetailsCallLogBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsCallLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        LinearLayoutManager manager = new LinearLayoutManager(SmsLogDetails.this, RecyclerView.VERTICAL, false);
        binding.rvChat.setLayoutManager(manager);

        binding.rvChat.smoothScrollToPosition(Repository.smsModel.getMessageChatModelList().size());
        adapter = new SmsCardAdapter(Repository.smsModel.getMessageChatModelList(), SmsLogDetails.this);
        binding.rvChat.setAdapter(adapter);

        binding.btnBack.setOnRippleCompleteListener(rippleView -> {
            onBackPressed();
        });

        binding.btnSelectAll.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

            }
        });
        hideSoftKeyboard();
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
