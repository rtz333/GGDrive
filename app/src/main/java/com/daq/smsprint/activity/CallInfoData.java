package com.daq.smsprint.activity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daq.smsprint.App;
import com.daq.smsprint.adapter.CallInfoAdapter;
import com.daq.smsprint.databinding.ActivityDetailsCallLogBinding;
import com.daq.smsprint.helper.dbHelper;
import com.daq.smsprint.models.CallInfoModel;
import com.daq.smsprint.models.CallLogModel;
import com.daq.smsprint.repository.Repository;

import java.util.ArrayList;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daq.smsprint.R;
import com.daq.smsprint.adapter.CallInfoAdapter;
import com.daq.smsprint.databinding.ActivityDetailsCallLogBinding;
import com.daq.smsprint.helper.dbHelper;
import com.daq.smsprint.interfaces.OnClickCallInfo;
import com.daq.smsprint.models.CallInfoModel;
import com.daq.smsprint.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class CallInfoData extends AppCompatActivity implements OnClickCallInfo {
    dbHelper dbHandler;
    String value, name;
    CallInfoAdapter adapter;
    ActivityDetailsCallLogBinding binding;
    ArrayList<CallInfoModel> callInfoModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsCallLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new dbHelper(this);

        if (getIntent().getExtras() != null) {
            value = getIntent().getStringExtra("call_number");
            name = getIntent().getStringExtra("call_name");
            binding.tvNameDetails.setText(name);
            LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            binding.rvChat.setLayoutManager(manager);
            Cursor cursor = dbHandler.redinfocall(value);
            callInfoModels.clear();
            while (cursor.moveToNext()) {
                CallInfoModel call = new CallInfoModel(cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(2), cursor.getString(1));
                if (call.getDate() != null) {
                    callInfoModels.add(call);
                }

            }
            cursor.close();

        }


        btnCheckAll();
        binding.tvNameDetails.setText(name);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvChat.setLayoutManager(manager);
        adapter = new CallInfoAdapter(Repository.callLogModel.getCallInfoModels(), CallInfoData.this, this, true);
        binding.rvChat.setAdapter(adapter);
        btnClick();

    }

    @Override
    protected void onResume() {
        super.onResume();


//
        if (Repository.callLogModel.getNameBackGround().equals("white")) {
            binding.rvChat.setBackground(null);
        } else {
            binding.rvChat.setBackground(ContextCompat.getDrawable(this, Repository.callLogModel.getDrawbleBackGround()));
        }
    }

    private void btnClick() {
        binding.btnBackground.setOnRippleCompleteListener(rippleView -> {
            startActivity(new Intent(CallInfoData.this, ThemeActivity.class));
        });
    }

    private void btnCheckAll() {
        binding.btnSelectAll.setOnRippleCompleteListener(rippleView -> {
            List<CallInfoModel> callInfoModel = adapter.getCallInfoModel();
            List<CallInfoModel> callInfoModelList = adapter.getSelectedItem();
            if (callInfoModelList.size() == callInfoModel.size()) {
                adapter.setAllCallInfoUnselected();
                binding.imgSelectAll.setImageResource(R.drawable.ic_tick_select);

            } else {
                adapter.setAllCallInfoSelected();
                binding.imgSelectAll.setImageResource(R.drawable.ic_chooseticket);

            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onClick(CallInfoModel callInfoModel, int position) {
        ArrayList<CallInfoModel> callInfoModels = adapter.getCallInfoModel();
        ArrayList<CallInfoModel> callInfoModelArrayList = adapter.getSelectedItem();
        if (callInfoModelArrayList.size() == callInfoModels.size()) {
            adapter.setAllCallInfoUnselected();
            binding.btnSelectAll.setBackgroundResource(R.drawable.ic_tick_select);
        } else {
            adapter.setAllCallInfoSelected();
            binding.btnSelectAll.setBackgroundResource(R.drawable.ic_chooseticket);
        }
    }

}
