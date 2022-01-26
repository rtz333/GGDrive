package com.daq.smsprint.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daq.smsprint.R;
import com.daq.smsprint.models.MessageChatModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public  class SmsCardAdapter extends RecyclerView.Adapter {

    List<MessageChatModel> messageChatModelList;
    Context context;
    private OnClickItemListener onClickItemlistener;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_NA = 2;

    SimpleDateFormat dateForma;
    DateFormat timeFormat;
    public interface OnClickItemListener{
       // void onClickItem;
    }

    public SmsCardAdapter(List<MessageChatModel> messageChatModelList, Context context) {
        this.messageChatModelList = messageChatModelList;
        this.context = context;
        this.dateForma = new SimpleDateFormat("yyyy-MM-dd");
        this.timeFormat = new SimpleDateFormat("h:mm:ss a");
     //   this.onClickItemListener = onClickItemListener;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageChatModel message = (MessageChatModel) messageChatModelList.get(position);
        if (message.getViewType() == 0) {
            // If the current user is the sender of the message
            Log.e("getItemViewType", "0");
            return VIEW_TYPE_MESSAGE_SENT;
        }else if (message.getViewType() == 1) {
            // If the current user is the sender of the message
            Log.e("getItemViewType", "1");
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            // If some other user sent the message
            Log.e("getItemViewType", "2");
            return VIEW_TYPE_MESSAGE_NA;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_outgoing, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_incoming, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageChatModel message = messageChatModelList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageChatModelList.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView time;
        ImageView ivView;


        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.tvBody);
            time = (TextView) itemView.findViewById(R.id.tvtime);
            ivView = itemView.findViewById(R.id.ivProfileOther);

        }

        void bind(MessageChatModel messageModel) {
            Date date=new Date(Long.parseLong(messageModel.getTime()));
            message.setText(messageModel.getText());
            time.setText(dateForma.format(date)+" - "+timeFormat.format(date));

        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;
        ImageView ivView;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.tvBody);
            time = (TextView) itemView.findViewById(R.id.tvName);
            ivView = itemView.findViewById(R.id.ivProfileMe);

        }

        void bind(MessageChatModel messageModel) {
            Date date=new Date(Long.parseLong(messageModel.getTime()));
            message.setText(messageModel.getText());
            time.setText(dateForma.format(date)+" - "+timeFormat.format(date));
        }
    }
}

