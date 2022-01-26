package com.daq.smsprint.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
     //database name

    static final String DB_NAME = "SPYAPP.DB";
    // database version
    static final int DB_VERSION = 1;
    static  final String TABLE_NAME = "Contacts";
    static  final String TABLE_NAME_SMS = "Sms";

    //call

    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String NUMBER_COL= "number";
    private static final String SCALL_COL = "scall";
    private static final String ECALL_COL = "ecall";
    private static final String DURATION_COL = "duration";
    private static final String CALLTYPE_COL = "callType";
    private static final String CALLDATE_COL = "callDate";

    //sms

    private static final String ID_COL_SMS = "Id";
    private static final String THREAD_ID_COL_SMS = "Thread_id";
    private static final String NAME_COL_SMS = "Name";
    private static final String NUMBER_COL_SMS= "Number";
    private static final String TYPE_SMS_COL="Type";
    private static final String CONTENT_SMS_COL = "Content";
    private static final String TIME_SMS_COl = "smsTime";
    private static final String DATE_SMS_COL = "smsDate";

    public dbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + NUMBER_COL + " TEXT,"
                + SCALL_COL + " TEXT,"
                + ECALL_COL + " TEXT,"
                + DURATION_COL + " TEXT,"
                + CALLDATE_COL + " TEXT,"
                + CALLTYPE_COL + " TEXT)";

        db.execSQL(query);

        String querysms = "CREATE TABLE " + TABLE_NAME_SMS + " ("
                + ID_COL_SMS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL_SMS + " TEXT,"
                + NUMBER_COL_SMS + " TEXT,"
                + TYPE_SMS_COL + " TEXT,"
                + CONTENT_SMS_COL + " TEXT,"
                + TIME_SMS_COl + " TEXT,"
                + DATE_SMS_COL+ " TEXT,"
                + THREAD_ID_COL_SMS + " TEXT)";
        db.execSQL(querysms);

    }

    public boolean  onInsert(String name, String number, String scall, String ecall, String  callDuration, String callType , String callDate) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(NUMBER_COL, number);
        values.put(SCALL_COL, scall);
        values.put(ECALL_COL, ecall);
        values.put(DURATION_COL, callDuration);
        values.put(CALLDATE_COL, callDate);
        values.put(CALLTYPE_COL, callType);
        db.insert(TABLE_NAME, null, values);

        return true;
    }

    public boolean insertCallLog(String name, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(NUMBER_COL, number);
        db.insert(TABLE_NAME, null, values);

        return true;
    }


    public Boolean onInsertSms(String smsThread,String smsName, String smsAddress, String smsType, String smsContent, String  smsTime , String smsDatee) {


        //  Log.i("all_data", "sms_onInsert: "+smsThread);

  //  Log.i("all_data", "sms_onInsert: "+smsThread);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(THREAD_ID_COL_SMS, smsThread);
        values.put(NAME_COL_SMS, smsName);
        values.put(NUMBER_COL_SMS, smsAddress);
        values.put(TYPE_SMS_COL, smsType);
        values.put(CONTENT_SMS_COL, smsContent);
        values.put(TIME_SMS_COl, smsTime);
        values.put(DATE_SMS_COL, smsDatee);
        db.insert(TABLE_NAME_SMS, null, values);

        //   Log.i("data ","sms_working.....!");

 //   Log.i("data ","sms_working.....!");

        return true;
    }

    public int onIncoming(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+CALLTYPE_COL+"='Incoming' ORDER BY ID,callDate DESC  ",null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public int onMissed(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+CALLTYPE_COL+"='Missed' ORDER BY id,date(callDate) DESC ",null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int onOutgoing(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+CALLTYPE_COL+"='Outgoing' ORDER BY id,date(callDate) DESC ",null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }
    public int onRejected(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+CALLTYPE_COL+"='Rejected' ORDER BY id,date(callDate) DESC  ",null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Cursor  readallData(String data){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+CALLTYPE_COL+"='"+data+"' GROUP BY date(callDate),number ORDER BY date(callDate) DESC,id DESC, number DESC",null);
        return cursor;

    }
    public Cursor  readtablelData(String table){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '"+table+"'  GROUP BY number ORDER BY date(callDate) DESC,id DESC ",null);
        System.out.println(cursor);
        return cursor;

    }
    //SELECT DISTINCT number FROM contacts WHERE  callType='Missed'  And number IN ( SELECT number FROM 'Contacts'   )

    public Cursor  readsmsallData(String daata){
        SQLiteDatabase db = this.getReadableDatabase();

        //SELECT distinct Number , Name from Sms
        //  SELECT Name,smsDate,smsTime,Type FROM 'Sms' where Type='sent'  group by Number order by id , date(smsDate) Desc  limit 0, 10
        // SELECT  *  FROM 'Sms'  where Type='sent' group by Number order by  date(smsDate) Desc  limit 0, 10
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_SMS+" WHERE "+TYPE_SMS_COL+"='"+daata+"' Group by  date(smsDate),Number ORDER BY date(smsDate) DESC  ",null);


        // Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_SMS+" WHERE "+TYPE_SMS_COL+"='"+daata+"' GROUP BY Number ORDER BY time(smsTime) Desc  limit 0,1",null);

        return cursor;
    }
    public Cursor  readsmstablelData(){
        SQLiteDatabase db = this.getReadableDatabase();

        //  SELECT  *  FROM 'Sms'  group by Number order by Id , date(smsDate) Desc  limit 0, 10
        // SELECT  *  FROM 'Sms'   group by Number order by  date(smsDate) Desc  limit 0, 3

      //  SELECT  *  FROM 'Sms'  group by Number order by Id , date(smsDate) Desc  limit 0, 10
       // SELECT  *  FROM 'Sms'   group by Number order by  date(smsDate) Desc  limit 0, 3

        Cursor cursor = db.rawQuery("SELECT * FROM '"+TABLE_NAME_SMS +"' GROUP BY Number  ORDER BY  time(smsTime) DESC,date(smsDate) DESC,id DESC ",null);
        return cursor;
    }

    public Boolean onCallDelete(String num){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,NUMBER_COL+"='"+num+"'",null);
        return true;
    }
    public Boolean onSmsDelete(String num){
        SQLiteDatabase db = this.getReadableDatabase();


        //  db.delete(Table_Name, "Raw_Field_Name" + "='" + Raw_field_value+"'", null);
        db.delete(TABLE_NAME_SMS,NUMBER_COL_SMS+"='"+num+"'",null);

      //  db.delete(Table_Name, "Raw_Field_Name" + "='" + Raw_field_value+"'", null);
         db.delete(TABLE_NAME_SMS,NUMBER_COL_SMS+"='"+num+"'",null);

        return true;
    }
    public Cursor  redinfosms(String data){
        //SELECT Content,smsDate,Type FROM 'Sms'where Number='+919454582403' order by id Desc, date(smsDate) Asc,Time(smsTime) Asc
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_SMS+" WHERE Number ='"+data+"' ORDER BY date(smsDate) ASC  ",null);
        return cursor;
    }
    public Cursor  redinfocall(String data){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE number ='"+data+"' ORDER BY date(callDate) DESC",null);
        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SMS);
        onCreate(db);
    }
}
