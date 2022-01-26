package com.daq.smsprint.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daq.smsprint.R;
import com.daq.smsprint.activity.SmsLogDetails;
import com.daq.smsprint.databinding.ItemSmsBinding;
import com.daq.smsprint.interfaces.SmsClick;
import com.daq.smsprint.models.SmsModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SmsModel> smsList;
    private Context context;
    private SmsClick smsClick;
    SimpleDateFormat dateForma;
    DateFormat time;
    public SmsLogAdapter(List<SmsModel> smsHolder, Context context, SmsClick smsClick) {
        this.smsList = smsHolder;
        this.context = context;
        this.smsClick = smsClick;
        this.dateForma = new SimpleDateFormat("yyyy-MM-dd");
        this.time = new SimpleDateFormat("h:mm:ss a");
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemSmsBinding binding = ItemSmsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ((ItemViewHolder) holder).binData(smsList.get(position));
    }

    private static final String TAG = "AAA";
    @Override
    public int getItemCount() {
        return smsList.size();
    }

    private class  ItemViewHolder extends  RecyclerView.ViewHolder{

        private  final  ItemSmsBinding binding ;

        public ItemViewHolder (ItemSmsBinding itemSmsBinding){
            super(itemSmsBinding.getRoot());
            this.binding = itemSmsBinding;
        }


        public void binData(SmsModel smsList){

            binding.senderName.setText(smsList.getSmsName());
            binding.senderNumber.setText(smsList.getSmsAddress());
            Log.d(TAG, "binData: "+smsList.getSmsDate());
            Date date=new Date(Long.parseLong(smsList.getSmsDate()));
            binding.senderTime.setText(dateForma.format(date)+" - "+time.format(date));

            binding.senderName.setText(smsList.getSmsContent());


            binding.senderNumber.setText(smsList.getSmsAddress());

            binding.rvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    smsClick.onSmsClick(smsList, getAdapterPosition());

                }
            });
        }
    }

    public void setList(List<SmsModel> smsList) {
        this.smsList = smsList;
        notifyDataSetChanged();
    }


}
