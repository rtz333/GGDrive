package com.daq.smsprint.util;

import androidx.room.TypeConverter;

import com.daq.smsprint.models.CallInfoModel;
import com.daq.smsprint.models.MessageChatModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    @TypeConverter
    public static String fromCallInfoModel(ArrayList<CallInfoModel> callInfoModels) {
        Gson gson = new Gson();
        return gson.toJson(callInfoModels);
    }

    @TypeConverter
    public static ArrayList<CallInfoModel> toCallInfoModel(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CallInfoModel>>() {}.getType();
        ArrayList<CallInfoModel> callInfoModels = gson.fromJson(json, type);
        if (callInfoModels == null) {
            callInfoModels = new ArrayList<>();
        }
        return callInfoModels;
    }

    @TypeConverter
    public static String fromMessageChatModel(List<MessageChatModel> messageChatModelList) {
        Gson gson = new Gson();
        return gson.toJson(messageChatModelList);
    }

    @TypeConverter
    public static List<MessageChatModel> toMessageChatModel(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MessageChatModel>>() {}.getType();
        List<MessageChatModel> messageChatModelList = gson.fromJson(json, type);
        if (messageChatModelList == null) {
            messageChatModelList = new ArrayList<>();
        }
        return  messageChatModelList;
    }
}
