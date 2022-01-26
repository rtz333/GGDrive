package com.daq.smsprint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daq.smsprint.App;
import com.daq.smsprint.R;
import com.daq.smsprint.adapter.ThemeAdapter;
import com.daq.smsprint.databinding.ActivityThemeBinding;
import com.daq.smsprint.models.ThemeModel;
import com.daq.smsprint.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThemeActivity extends AppCompatActivity implements ThemeAdapter.OnClickTheme {
    ActivityThemeBinding binding;
    List<ThemeModel> themeModels = new ArrayList<>();
    ThemeAdapter themeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        themeModels = getThemes();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        binding.rvTheme.setLayoutManager(gridLayoutManager);
        themeAdapter = new ThemeAdapter(this, this, themeModels);
        binding.rvTheme.setAdapter(themeAdapter);
        themeAdapter.setData(themeModels);
        onImportImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onImportImageResult(result);
            }
        });
    }

    private List<ThemeModel> getThemes() {
        List<ThemeModel> themeModels = new ArrayList<>();
        themeModels.add(new ThemeModel("importThem", R.drawable.importtheme));
        themeModels.add(new ThemeModel("animals1", R.drawable.animals1));
        themeModels.add(new ThemeModel("animals2", R.drawable.animals2));
        themeModels.add(new ThemeModel("animals3", R.drawable.animals3));
        themeModels.add(new ThemeModel("animals4", R.drawable.animals4));
        themeModels.add(new ThemeModel("animals5", R.drawable.animals5));
        themeModels.add(new ThemeModel("animals6", R.drawable.animals6));
        themeModels.add(new ThemeModel("animals7", R.drawable.animals7));
        themeModels.add(new ThemeModel("animals8", R.drawable.animals8));
        themeModels.add(new ThemeModel("animals9", R.drawable.animals9));
        themeModels.add(new ThemeModel("animals10", R.drawable.animals10));
        themeModels.add(new ThemeModel("business1", R.drawable.business1));
        themeModels.add(new ThemeModel("business2", R.drawable.business2));
        themeModels.add(new ThemeModel("business3", R.drawable.business3));
        themeModels.add(new ThemeModel("business4", R.drawable.business4));
        themeModels.add(new ThemeModel("business5", R.drawable.business5));
        themeModels.add(new ThemeModel("business6", R.drawable.business6));
        themeModels.add(new ThemeModel("business7", R.drawable.business7));
        themeModels.add(new ThemeModel("business8", R.drawable.business8));
        themeModels.add(new ThemeModel("business9", R.drawable.business9));
        themeModels.add(new ThemeModel("business10", R.drawable.business10));
        themeModels.add(new ThemeModel("love1", R.drawable.love1));
        themeModels.add(new ThemeModel("love2", R.drawable.love2));
        themeModels.add(new ThemeModel("love3", R.drawable.love3));
        themeModels.add(new ThemeModel("love4", R.drawable.love4));
        themeModels.add(new ThemeModel("love5", R.drawable.love5));
        themeModels.add(new ThemeModel("love6", R.drawable.love6));
        themeModels.add(new ThemeModel("love7", R.drawable.love7));
        themeModels.add(new ThemeModel("love8", R.drawable.love8));
        themeModels.add(new ThemeModel("love9", R.drawable.love9));
        themeModels.add(new ThemeModel("love10", R.drawable.love10));
        themeModels.add(new ThemeModel("nature1", R.drawable.nature1));
        themeModels.add(new ThemeModel("nature2", R.drawable.nature2));
        themeModels.add(new ThemeModel("nature3", R.drawable.nature3));
        themeModels.add(new ThemeModel("nature4", R.drawable.nature4));
        themeModels.add(new ThemeModel("nature5", R.drawable.nature5));
        themeModels.add(new ThemeModel("nature6", R.drawable.nature6));
        themeModels.add(new ThemeModel("nature7", R.drawable.nature7));
        themeModels.add(new ThemeModel("nature8", R.drawable.nature8));
        themeModels.add(new ThemeModel("nature9", R.drawable.nature9));
        themeModels.add(new ThemeModel("nature10", R.drawable.nature10));
        themeModels.add(new ThemeModel("mood11", R.drawable.mood11));
        themeModels.add(new ThemeModel("mood12", R.drawable.mood12));
        themeModels.add(new ThemeModel("mood13", R.drawable.mood13));
        themeModels.add(new ThemeModel("mood14", R.drawable.mood14));
        themeModels.add(new ThemeModel("mood15", R.drawable.mood15));
        themeModels.add(new ThemeModel("mood16", R.drawable.mood16));
        themeModels.add(new ThemeModel("mood17", R.drawable.mood17));
        themeModels.add(new ThemeModel("mood18", R.drawable.mood18));
        themeModels.add(new ThemeModel("mood19", R.drawable.mood19));
        themeModels.add(new ThemeModel("mood20", R.drawable.mood20));


        return themeModels;
    }

    public void onImportImageResult(ActivityResult result) {

    }

    private ActivityResultLauncher<Intent> onImportImageLauncher;

    @Override
    public void onClick(ThemeModel themeModel, int position) {
        if (position == 0) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            onImportImageLauncher.launch(intent);
        } else {
            Repository.callLogModel.setDrawbleBackGround(themeModel.getBackgroundDrawble());
            Repository.callLogModel.setNameBackGround(themeModel.getBackgroundName());
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                App.appDatabase.callDao().update(Repository.callLogModel);
                handler.post(() -> {
                    onBackPressed();
                });
            });

        }
    }

}


