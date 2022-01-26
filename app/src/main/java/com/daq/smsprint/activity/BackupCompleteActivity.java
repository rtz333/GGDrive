package com.daq.smsprint.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.daq.smsprint.App;
import com.daq.smsprint.databinding.ActivityBackupCompletedBinding;
import com.daq.smsprint.repository.Repository;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class BackupCompleteActivity extends AppCompatActivity {
    ActivityBackupCompletedBinding binding;
    Drive googleDriveService;
    GoogleAccountCredential credential;
    DriveServiceHelper mDriveServiceHelper;
    String accountName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBackupCompletedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        accountName= App.sharePref.googleAccountName();
        credential = GoogleAccountCredential.usingOAuth2(
                BackupCompleteActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
        googleDriveService =
                new Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName(accountName)
                        .build();
        mDriveServiceHelper = new DriveServiceHelper(googleDriveService);

        binding.tvCallCount.setText(Repository.callInfoList.size()+"");
        binding.tvSmsCount.setText(Repository.messageChatModelList.size()+"");
        binding.tvDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(Repository.callInfoList.get(0).getDate()))));
        binding.tvTime.setText(Repository.callInfoList.get(0).getStartTime());
    }
}
