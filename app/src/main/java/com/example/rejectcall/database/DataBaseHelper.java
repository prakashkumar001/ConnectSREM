package com.example.rejectcall.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rejectcall.model.CallSms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakash on 4/15/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "call_sms_db";

    // Contacts table name
    private static final String TABLE_DATA = "call_sms_table";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME = "time";
    private static final String KEY_SENDER_PHONE_NO = "sender_phone";
    private static final String KEY_OWNER_PHONE_NO = "owner_phone";
    private static final String KEY_SMS = "sms";
    private static final String KEY_IS_SYNCED = "sync";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_RINGCOUNT = "ringcount";

    public DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_TIME + " TEXT," + KEY_SENDER_PHONE_NO + " TEXT," + KEY_OWNER_PHONE_NO + " TEXT,"
                + KEY_IS_SYNCED +" TEXT," + KEY_TOKEN +" TEXT," + KEY_SMS + " TEXT," + KEY_RINGCOUNT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

        // Create tables again
        onCreate(db);
    }

    public void add_details(CallSms data)
    {
        SQLiteDatabase db = this.getWritableDatabase();



        ContentValues values = new ContentValues();

        values.put(KEY_SMS, data.getMessage()); // Contact Name
        values.put(KEY_SENDER_PHONE_NO, data.getSenderPhone());
        values.put(KEY_OWNER_PHONE_NO, data.getOwnerPhone());
        values.put(KEY_TIME, data.getTime());
        values.put(KEY_TYPE, data.getType());
        values.put(KEY_TOKEN,data.getApi_token());
        values.put(KEY_RINGCOUNT,data.getApi_ringcount());
        values.put(KEY_IS_SYNCED, data.getIsSync()); // Contact Phone Number



        // Inserting Row

        db.insert(TABLE_DATA, null, values);

        Log.i("Inserted","Inserted"+data.getIsSync());

        db.close(); // Closing database connection

    }

    public List<CallSms> getunSyncdata()
    {
         List<CallSms> listdata=new ArrayList<>();
        // Select All Query

        String selectQuery = "SELECT  * FROM call_sms_table WHERE sync='"+"false"+"'";



        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {

            do {

                CallSms contact = new CallSms();


                contact.setId(cursor.getString(0).toString());

                contact.setType(cursor.getString(1));

                contact.setTime(cursor.getString(2));

                contact.setSenderPhone(cursor.getString(3));

                contact.setOwnerPhone(cursor.getString(4));

                contact.setIsSync(cursor.getString(5));

                contact.setApi_token(cursor.getString(6));
                contact.setMessage(cursor.getString(7));
                contact.setApi_ringcount(cursor.getString(8));
                // Adding contact to list

                listdata.add(contact);

            } while (cursor.moveToNext());

        }

        // return contact list

        return listdata;
    }


    public int updateContact(String id) {

        SQLiteDatabase db = this.getWritableDatabase();



        ContentValues values = new ContentValues();
        //data.setIsSync("true");
        values.put(KEY_IS_SYNCED,"true");





        // updating row

        return db.update(TABLE_DATA, values, KEY_ID + " = ?",

                new String[] { String.valueOf(id) });

    }

    public CallSms lastrecord() {


        CallSms contact = null;

        String selectQuery = "SELECT * FROM call_sms_table ORDER BY id DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {

            do {

                 contact = new CallSms();


                contact.setId(cursor.getString(0).toString());

                contact.setType(cursor.getString(1));

                contact.setTime(cursor.getString(2));

                contact.setSenderPhone(cursor.getString(3));

                contact.setOwnerPhone(cursor.getString(4));

                contact.setIsSync(cursor.getString(5));

                contact.setApi_token(cursor.getString(6));
                contact.setMessage(cursor.getString(7));
                contact.setApi_ringcount(cursor.getString(8));
                // Adding contact to list


            } while (cursor.moveToNext());

        }

        return contact;

    }

    public List<CallSms> getallData()
    {
        List<CallSms> listdata=new ArrayList<>();
        // Select All Query

        String selectQuery = "SELECT  * FROM call_sms_table";



        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {

            do {

                CallSms contact = new CallSms();


                contact.setId(cursor.getString(0).toString());

                contact.setType(cursor.getString(1));

                contact.setTime(cursor.getString(2));

                contact.setSenderPhone(cursor.getString(3));

                contact.setOwnerPhone(cursor.getString(4));

                contact.setIsSync(cursor.getString(5));

                contact.setApi_token(cursor.getString(6));

                contact.setMessage(cursor.getString(7));
                contact.setApi_ringcount(cursor.getString(8));

                // Adding contact to list

                listdata.add(contact);

            } while (cursor.moveToNext());

        }

        // return contact list

        return listdata;
    }

}
