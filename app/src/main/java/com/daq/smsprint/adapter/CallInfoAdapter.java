package com.daq.smsprint.adapter;

import android.content.Context;
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
import com.daq.smsprint.models.CallInfoModel;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.andexert.library.RippleView;
import com.daq.smsprint.R;
import com.daq.smsprint.interfaces.OnClickCallInfo;
import com.daq.smsprint.models.CallInfoModel;


import java.util.ArrayList;


public class CallInfoAdapter extends RecyclerView.Adapter<CallInfoAdapter.Viewholder> {

    ArrayList<CallInfoModel> callInfoModel = new ArrayList<>();
    Context context;

    ClickTEst clickTEst;


    public interface ClickTEst{
        void onclick(String startTime);
    }

    private OnClickCallInfo onClickCallInfo;
    private final boolean icCheck;

    public CallInfoAdapter(ArrayList<CallInfoModel> callInfoModel, Context context, OnClickCallInfo onClickCallInfo, boolean icCheck) {
        this.callInfoModel = callInfoModel;
        this.context = context;
        this.onClickCallInfo = onClickCallInfo;
        this.icCheck = icCheck;
    }

    public ArrayList<CallInfoModel> getCallInfoModel() {
        return callInfoModel;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());

        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_call, parent, false));
    }



    public void onBindViewHolder(@NonNull CallInfoAdapter.Viewholder holder, int position) {
        holder.bindata(callInfoModel.get(position));
            holder.callerDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(callInfoModel.get(position).getDate()))) + " " +
                    callInfoModel.get(position).getStartTime());

        holder.endCallerTime.setText(callInfoModel.get(position).getEndTime());
        holder.callerId.setText(callInfoModel.get(position).getNumber());
        holder.callName.setText(callInfoModel.get(position).getName());
        holder.duration.setText(callInfoModel.get(position).getDuration());

        switch (callInfoModel.get(position).getType()) {
            case "Incoming":
                holder.style.setImageResource(R.drawable.ic_call);
                break;
            case "Outgoing":
                holder.style.setImageResource(R.drawable.ic_outgoing);
                break;
            case "MissCall":
                holder.style.setImageResource(R.drawable.ic_misscall);
                break;
            case "Rejected":
                holder.style.setImageResource(R.drawable.ic_misscall);
                break;

        }



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTEst.onclick(callInfoModel.get(position).getStartTime());
            }
        });



    }

    @Override
    public int getItemCount() {
        return callInfoModel.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView callName, endCallerTime, callerDate, callerId, duration;
        ImageView style, ivCheck;

        RelativeLayout relativeLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            callerDate = itemView.findViewById(R.id.tvDateCall);
            endCallerTime = itemView.findViewById(R.id.tvEndTime);
            callerId = itemView.findViewById(R.id.tvNumber);
            callName = itemView.findViewById(R.id.tvName);
            style = itemView.findViewById(R.id.imgStyle);
            duration = itemView.findViewById(R.id.tvDuration);

            relativeLayout=itemView.findViewById(R.id.rlCallStyle);

            ivCheck = itemView.findViewById(R.id.imgSelect);
            relativeLayout = itemView.findViewById(R.id.rlCallStyle);
        }

        public void bindata(CallInfoModel infoModel) {
            if (infoModel.isCheck()) {
                ivCheck.setImageResource(R.drawable.ic_chooseticket);
            } else {
                ivCheck.setImageResource(R.drawable.ic_tick_select);
            }
            ivCheck.setOnClickListener(v -> {
                if (infoModel.isCheck()) {
                    ivCheck.setImageResource(R.drawable.ic_tick_select);
                    infoModel.setCheck(false);

                } else {
                    ivCheck.setImageResource(R.drawable.ic_chooseticket);
                    infoModel.setCheck(true);

                }

            });

        }

    }

    public ArrayList<CallInfoModel> getSelectedItem() {
        ArrayList<CallInfoModel> arrayList = new ArrayList<>();
        if (this.callInfoModel != null) {
            for (int i = 0; i < this.callInfoModel.size(); i++) {
                if (this.callInfoModel.get(i).isCheck()) {
                    arrayList.add(this.callInfoModel.get(i));
                }
            }
        }
        return arrayList;
    }

    public void setAllCallInfoUnselected() {
        if (this.callInfoModel != null) {
            for (int i = 0; i < this.callInfoModel.size(); i++) {
                if (this.callInfoModel.get(i).isCheck()) {
                    this.callInfoModel.get(i).setCheck(false);
                }
            }
        }
    }

    public void setAllCallInfoSelected() {
        if (this.callInfoModel != null) {
            for (int i = 0; i < this.callInfoModel.size(); i++) {
                if (!this.callInfoModel.get(i).isCheck()) {
                    this.callInfoModel.get(i).setCheck(true);
                }
            }
        }
    }

}
