package com.daq.smsprint.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.daq.smsprint.databinding.ActivitySplashBinding;
import com.daq.smsprint.repository.Constant;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "AAA";
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (checkPermission()) {
            startActivity(new Intent(this, MainActivity.class));
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(() -> {
//                startActivity(new Intent(this, MainActivity.class));
//            }, 2000);


        } else {
            startActivity(new Intent(this, PermissionActivity.class));
        }
    }

    private boolean checkPermission() {
        for (String permission : Constant.permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "checkPermission: " + permission);
                return false;
            }
        }
        return true;
    }


}
