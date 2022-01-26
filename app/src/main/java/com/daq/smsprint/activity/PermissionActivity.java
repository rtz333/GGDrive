package com.daq.smsprint.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.daq.smsprint.databinding.ActivityPermissionBinding;
import com.daq.smsprint.repository.Constant;

public class PermissionActivity extends AppCompatActivity {


    private static final String TAG = "AAA";

    private ActivityPermissionBinding binding;
    private ActivityResultLauncher<Intent> onPermissionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            onPermissionResult(result);
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        clickHandler();

    }

    private void clickHandler() {
        binding.btnAgree.setOnClickListener(v -> {
            requestPermissions(Constant.permissions, Constant.REQUEST_CODE_PERMISSION);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                boolean result = true;
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "onRequestPermissionsResult: ");
                        result = false;
                        break;
                    }
                }
                if (result) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    onPermissionLauncher.launch(intent);
                }
            }
        }
    }

    private void onPermissionResult(ActivityResult result) {
        if (checkPermission()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "This app can't work without permission. Please accept permissions!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        for (String permission : Constant.permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
