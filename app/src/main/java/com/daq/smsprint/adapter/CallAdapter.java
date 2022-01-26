package com.daq.smsprint.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daq.smsprint.R;
import com.daq.smsprint.activity.CallInfoData;
import com.daq.smsprint.helper.dbHelper;

import com.daq.smsprint.interfaces.CallLogClick;
import com.daq.smsprint.models.CallLogModel;

import java.util.ArrayList;
import java.util.List;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyViewHolder> {
    List<CallLogModel> callList;
    private dbHelper dbHandler;
    private Context mcontext;






    private CallLogClick callLogClick;


    public CallAdapter(List<CallLogModel> callList, Context mcontext, CallLogClick callLogClick) {
        this.callList = callList;
        this.mcontext = mcontext;
        this.callLogClick = callLogClick;
    }


    public void setData(List<CallLogModel> dataholder) {
        this.callList = dataholder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_logs, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.callName.setText(callList.get(position).getCallName());
        holder.phoneNumber.setText(callList.get(position).getCallNumber());
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mcontext, CallInfoData.class);
                intent.putExtra("call_name",callList.get(position).getCallName());
                intent.putExtra("call_number", callList.get(position).getCallNumber());
                mcontext.startActivity(intent);

                callLogClick.onClickCallLog(callList.get(position), position);



            }
        });

    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView callDate, callDuration, callName, callType, phoneNumber, callStartTime, callEndTime;
        public LinearLayout cvItem;
        public ImageView callDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.callName = (TextView) itemView.findViewById(R.id.tvName);
            this.phoneNumber = (TextView) itemView.findViewById(R.id.tvTelephone);
            this.cvItem = (LinearLayout) itemView.findViewById(R.id.itemCallLog);


        }
    }

    public void setList(List<CallLogModel> callList) {
        this.callList = callList;
        notifyDataSetChanged();
    }
}
