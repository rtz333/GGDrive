package com.daq.smsprint.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.andexert.library.RippleView;
import com.daq.smsprint.App;
import com.daq.smsprint.databinding.ActivitySetupBinding;
import com.daq.smsprint.models.Test;

import com.daq.smsprint.repository.Repository;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SetupActivity extends AppCompatActivity {
    public static final String TAG = "nrenflas";
    ActivitySetupBinding binding;
    GoogleAccountCredential credential;
    private static Drive googleDriveService;
    DriveServiceHelper mDriveServiceHelper;
    String accountName;
    Account account;
    File temp;

    public static final int SMS_CASE=28189;
    public static final int CALL_CASE=252;
    public static final int ALL=9874;

    public static int type;


    private ActivityResultLauncher<Intent> mCredentialLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_CANCELED){
                Log.d(TAG, "No choose account: ");
            }else if (result.getResultCode()==RESULT_OK){
                accountName = result.getData().getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                credential.setSelectedAccountName(accountName);
                account = new GoogleAccountManager(SetupActivity.this).getAccountByName(accountName);
                credential.setSelectedAccount(account);
                googleDriveService =
                        new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName(accountName)
                                .build();
                //if you want to use your common space of G drive
                mDriveServiceHelper = new DriveServiceHelper(googleDriveService);

                Log.d(TAG, "Choose account: "+accountName);
                new GEtToken(credential).execute();
            }


        }
    });
    private ActivityResultLauncher<Intent> mCredentialLauncherLogin = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_CANCELED){
                Log.d(TAG, "RESULT_CANCELED: ");
            }else if (result.getResultCode()==RESULT_OK){
                account = new GoogleAccountManager(SetupActivity.this).getAccountByName(accountName);
                credential.setSelectedAccount(account);
                googleDriveService =
                        new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName(accountName)
                                .build();
                Toast.makeText(SetupActivity.this, accountName, Toast.LENGTH_SHORT).show();
                //if you want to use your common space of G drive
                mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                isHasExitsAccount();
                Log.d(TAG, "Choose account22: "+account.name);
                Log.d(TAG, "mCredentialLauncherLogin: " + result.getData().getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
            }

        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (App.sharePref.googleAccountName()!=null){
            startActivity(new Intent(SetupActivity.this,BackupCompleteActivity.class));
            finish();
        }
        binding.btnLoginDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credential = GoogleAccountCredential.usingOAuth2(
                        SetupActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
                mCredentialLauncher.launch(credential.newChooseAccountIntent());
            }
        });

        binding.btnCallLogBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.btnFolderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFolder();
            }
        });


        binding.btnBackUpNow.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                createBackupFileHandle();
            }
        });

    }

    private void checkHasGoogleDriveFolder() {
        mDriveServiceHelper.queryFiles(null)
                .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
                    @Override
                    public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
//                        Gson gson = new Gson();
//                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders));
                        Log.d(TAG, "checkHasGoogleDrive: "+googleDriveFileHolders.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetupActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createBackupFileHandle() {
        if (!binding.switchCallLog.isChecked() && !binding.switchSms.isChecked()){

            Toast.makeText(SetupActivity.this, "Choose at least 1 format pls", Toast.LENGTH_SHORT).show();
            return;
        }else if (binding.switchCallLog.isChecked() && !binding.switchSms.isChecked()){
            type=CALL_CASE;
            createCallLogBackup();
        }else if (!binding.switchCallLog.isChecked() && binding.switchSms.isChecked()){
            type=SMS_CASE;
            createSmsLogBackup();
        }else if (binding.switchCallLog.isChecked() && binding.switchSms.isChecked()){
            type=ALL;
        }
    }

    private void createSmsLogBackup() {
        new CreateSmsLogBackupFile().execute();
    }

    private void createCallLogBackup() {
        new CreateCallLogBackupFile().execute();

    }

    private void upLoadFile(File file) {
        mDriveServiceHelper.uploadFile(file, "application/json", App.sharePref.folderIdGoogleDrive())
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();

                        Log.d(TAG, "upload: " + gson.toJson(googleDriveFileHolder));
                        file.deleteOnExit();
                        Intent intent=new Intent(SetupActivity.this,BackupCompleteActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    private void creatJsonBackupFile() {
        try {
            temp=File.createTempFile("ttessst020",".json",null);
//            CallLogModel[] callLogModels=Repository.callLogList.toArray(new CallLogModel[Repository.callLogList.size()]);
            Gson gson=new Gson();
//            try (Writer writer = new FileWriter("Output.json")) {
//                gson = new GsonBuilder().create();
//                gson.toJson(users, writer);
//            }
            List<Test> tests=new ArrayList<>();
            tests.add(new Test("nsajkcnas","315616"));
            tests.add(new Test("nuvuyvu","21560"));
            FileWriter fileWriter=new FileWriter(temp);
            gson.toJson(tests,fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createFolder() {
        mDriveServiceHelper.createFolderIfNotExist(App.sharePref.folderNameGoogleDrive(), null)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        App.sharePref.setFolderIDGoogleDrive(googleDriveFileHolder.getId());
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());

                    }
                });
    }

    private boolean isHasExitsAccount() {

        if (account != null) {
            binding.tvDriveLogin.setText("Logged on: " + account.name);
            App.sharePref.setGoogleAccountName(account.name);
            return true;
        } else {
            binding.tvDriveLogin.setText("Drive Login");
            App.sharePref.setGoogleAccountName(null);
            return false;
        }
    }

    private class GEtToken extends AsyncTask<Void, Void, String> {
        GoogleAccountCredential mCredential;
        String s;

        public GEtToken(GoogleAccountCredential mCredential) {
            this.mCredential = mCredential;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if (mCredential.getSelectedAccount() == null) {
                    return "account null";
                }
                s = mCredential.getToken();
                GoogleAuthUtil.clearToken(SetupActivity.this, s);
                s = mCredential.getToken();
                return s == null ? "null" : "token:  " + s;
            } catch (IOException e) {
                return "IOException " + e.getMessage();
            } catch (UserRecoverableAuthException e) {
                mCredentialLauncherLogin.launch(e.getIntent());
                return "UserRecoverableAuthException " + e.getMessage();
            } catch (GoogleAuthException e) {
                return "GoogleAuthException " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.startsWith("token:")){
                isHasExitsAccount();
                Log.d(TAG, s);
            }else {
                Log.d(TAG, "onPostExecute: "+s);
            }
        }
    }

    private class CreateCallLogBackupFile extends AsyncTask<Void,Void,Void>{
        File tempFile;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                tempFile=File.createTempFile("call",".json",null);
                Gson gson=new Gson();
                FileWriter fileWriter=new FileWriter(tempFile);
                gson.toJson(Repository.callInfoList,fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d(TAG, "onPostExecute: "+Repository.callInfoList.size());
            upLoadFile(tempFile);
            if(type==ALL){
                new CreateSmsLogBackupFile().execute();
            }
        }
    }

    private class CreateSmsLogBackupFile extends AsyncTask<Void,Void,Void>{
        File tempFile;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                tempFile=File.createTempFile("sms",".json",null);
                Gson gson=new Gson();
                FileWriter fileWriter=new FileWriter(tempFile);
                gson.toJson(Repository.messageChatModelList,fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d(TAG, "onPostExecute: "+Repository.messageChatModelList.size());
            upLoadFile(tempFile);

        }
    }




}
