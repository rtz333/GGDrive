package com.daq.smsprint.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.daq.smsprint.R;
import com.daq.smsprint.databinding.DialogFilterBinding;
import com.daq.smsprint.interfaces.FilterClick;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.DatePicker;

import com.daq.smsprint.R;
import com.daq.smsprint.activity.SmsActivity;
import com.daq.smsprint.databinding.DialogFilterBinding;
import com.daq.smsprint.interfaces.FilterClick;
import com.daq.smsprint.repository.Repository;
import com.daq.smsprint.util.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

public class FilterDialog extends BottomSheetDialog {
    private final Activity mActivity;
    private DialogFilterBinding binding;
    private int yearCreationDate;
    private int monthCreationDate;
    private int dayOfMonthCreationDate;
    private int yearDueDate;
    private int monthDueDate;
    private int dayOfMonthDueDate;
    private long creationDateTimeStamp;
    FilterClick filterClick;

    public FilterDialog(Activity activity, FilterClick filterClick) {
        super(activity);
        this.mActivity = activity;
        this.filterClick = filterClick;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(true);
        initializeData();
        binding.edtDate.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int i, int i1, int i2) {
                    Repository.date.set(i + "/" + (i1 + 1) + "/" + i2);
                    yearDueDate = i;
                    monthDueDate = i1;
                    dayOfMonthDueDate = i2;
                    int month= 0;
                    String s = null;
                    month=i1+1;
                    if (month<10){
                        s= String.valueOf("0"+month);
                    } else if (month>10) {
                        s= String.valueOf(month);
                    }
                    binding.edtDate.setText(i + "-" + s + "-" + i2);
                }
            }, yearDueDate, monthDueDate, dayOfMonthDueDate);
            datePickerDialog.show();
        });
        binding.btnOk.setOnRippleCompleteListener(rippleView -> {
            String date = binding.edtDate.getText().toString();
            filterClick.onFilterApply(date);
            dismiss();
        });
    }

    private void initializeData() {
        yearDueDate = yearCreationDate = Utility.getCalendar(Utility.getCurrentDate()).get(Calendar.YEAR);
        monthDueDate = monthCreationDate = Utility.getCalendar(Utility.getCurrentDate()).get((Calendar.MONTH));
        dayOfMonthDueDate = dayOfMonthCreationDate = Utility.getCalendar(Utility.getCurrentDate()).get((Calendar.DAY_OF_MONTH));
        creationDateTimeStamp = Utility.getTimeStamp(yearCreationDate, monthCreationDate, dayOfMonthCreationDate);
    }

}
