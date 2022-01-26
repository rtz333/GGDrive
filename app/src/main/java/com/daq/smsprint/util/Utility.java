package com.daq.smsprint.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utility {


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static double getRandomNumber(double min, double max) {
        return min + new Random().nextDouble() * (max - min);
    }

    public static String convertDateToString(Date date, String strDateFormat) {
        if (date == null) {
            return "";
        }
        if (strDateFormat == null || strDateFormat.equals("")) {
            strDateFormat = "dd/MM/yyyy";
        }
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

        return dateFormat.format(date);
    }

    public static String getTimeDuration(long j) {
        int i = (int) (j / 1000);
        int i2 = i / 3600;
        int i3 = i % 3600;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60)});
    }

    public static void translate(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, 0.0f, 2, 1.0f, 2, 0.0f, 2, 0.0f);
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setRepeatMode(1);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(translateAnimation);
    }

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public static boolean isBetweenDates(String min, String max, String between) throws ParseException {
        Date minDate, maxDate, betweenDate;
        if (between.equals("")) {
            return true;
        }
        minDate = simpleDateFormat.parse(min);
        betweenDate = simpleDateFormat.parse(between);
        if (max.equals("")) {
            return betweenDate.compareTo(minDate) >= 0;
        }
        maxDate = simpleDateFormat.parse(max);
        return betweenDate.compareTo(minDate) >= 0 && betweenDate.compareTo(maxDate) <= 0;
    }

    public static boolean compareDate(String day1, String day2) {
        Date date1, date2;
        try {
            date1 = simpleDateFormat.parse(day1);
            date2 = simpleDateFormat.parse(day2);
            return date1.compareTo(date2) < 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }

    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyMMdd_HHmmss_SSS");
        return df.format(Calendar.getInstance().getTime());
    }

    public static long getID() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
        return Long.parseLong(df.format(Calendar.getInstance().getTime()));
    }

    public static String addDays(String date, int days) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(simpleDateFormat.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, days);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static long getTimeStamp(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    public static Calendar getCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(simpleDateFormat.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static long getNumberOfDaysBetweenDates(String startDate, String endDate) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(startDate);
            date2 = simpleDateFormat.parse(endDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date2 != null;
        assert date1 != null;
        long diff = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static byte[] convertToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

//    public static List<Currency> parseJsonToObject(String json) {
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<Currency>>() {
//        }.getType();
//        List<Currency> currencies = gson.fromJson(json, type);
//        if (currencies == null) {
//            currencies = new ArrayList<>();
//        }
//        return currencies;
//    }

    public static String currencyFormat(String string) {
        NumberFormat numberFormat = new DecimalFormat("#,###");
        numberFormat.setMaximumFractionDigits(2);
        double number = Double.parseDouble(string);
        return numberFormat.format(number);
    }

    public static String roundingDecimal(float number) {
        float roundedDecimal = (float) Math.round(number * 100) / 100;
        return String.valueOf(roundedDecimal);
    }

}
