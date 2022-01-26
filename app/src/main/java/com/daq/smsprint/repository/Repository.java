package com.daq.smsprint.repository;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.daq.smsprint.models.CallInfoModel;
import com.daq.smsprint.models.MessageChatModel;
import com.daq.smsprint.models.SmsModel;
import com.daq.smsprint.models.CallLogModel;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public static List<SmsModel> smsList = new ArrayList<>();
    public static List<CallLogModel> callLogList = new ArrayList<>();


    public static List<CallLogModel> callSelectList = new ArrayList<>();

    public static List<CallInfoModel> callInfoList = new ArrayList<>();
    public static List<MessageChatModel> messageChatModelList = new ArrayList<>();
    public static ObservableField<String> date = new ObservableField<>("");
    public static ObservableBoolean isFiltering = new ObservableBoolean(false);
    public static CallLogModel callLogModel;
    public static SmsModel smsModel;



}
