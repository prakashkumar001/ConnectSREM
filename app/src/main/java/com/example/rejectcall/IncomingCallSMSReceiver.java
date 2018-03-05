package com.example.rejectcall;

/**
 * Created by Creative IT Works on 13-Apr-17.
 */

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rejectcall.Utils.InternetPermissions;
import com.example.rejectcall.Utils.UploadDataToServer;
import com.example.rejectcall.database.DataBaseHelper;
import com.example.rejectcall.model.CallSms;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class IncomingCallSMSReceiver extends BroadcastReceiver{
    static boolean isRinging = false;
    String number = "";
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    InternetPermissions internetPermissions;
    DataBaseHelper dataBaseHelper;
    public List<CallSms> dataList;
    Context ctx;
    static Timer timer;
    TimerTask timerTask;
    public static int count=0;
    int ringcount=0;
    public static String phonenumber="";
    final Handler handler = new Handler();
    public void onReceive( Context context, Intent intent) {

        ctx=context;
        internetPermissions=new InternetPermissions(context);
        dataBaseHelper=new DataBaseHelper(context);




        // Get the current Phone State
        String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        if(phoneState!=null)
        {
            // If phone is "Ringing"
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                isRinging = true;
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);



                startTimer(number);
                    }


            // if phone is idle after ringing
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {


                stoptimertask(0);
            }

            if(phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {

                stoptimertask(1000);
            }

        }else if (bundle!=null)
        {
            try {

                String phoneNumber="";
                String senderNum="";
                String message="";
                SmsMessage[] smsm = null;


                if (bundle != null) {
                    // Get the SMS message
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsm = new SmsMessage[pdus.length];
                    for (int i = 0; i < smsm.length; i++) {
                        smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        phoneNumber =  smsm[i].getOriginatingAddress();
                        message += smsm[i].getMessageBody().toString();
                    }
                }
                //YYYY-MM-DD HH:MM:SS
                String token_value="";
                SharedPreferences prefs = ctx.getSharedPreferences("TOKEN", MODE_PRIVATE);

                try {
                    token_value = prefs.getString("token", "");//"No name defined" is the default value.

                }catch (Exception e)
                {

                }



                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String today= dateFormatter.format(new Date());
                CallSms data=new CallSms(phoneNumber,"",message,today,"SMS","false",token_value,"");


                dataBaseHelper.add_details(data);



                if(internetPermissions.isInternetOn())
                {

                    Gson gson=new Gson();
                    dataList=new ArrayList<>();
                    dataList=dataBaseHelper.getunSyncdata();
                    String json= gson.toJson(dataList);

                    Log.i("GSON","GSON"+json);


                    pushValuesToServer(json);

                }else
                {

                }



            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }




    }

    public void endCall(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            Object telephonyService = m.invoke(tm);

            c = Class.forName(telephonyService.getClass().getName());
            m = c.getDeclaredMethod("endCall");
            m.setAccessible(true);
            m.invoke(telephonyService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pushValuesToServer(String json)
    {
        JSONObject object=null;
        JSONArray jsonArray=null;
// Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        try {
           // object=new JSONObject(json);
             jsonArray = new JSONArray(json);
             //object = new JSONObject();
           // object.put("jsondata", jsonArray);


        }catch (Exception e)
        {

        }
        String responsefromServer="";


        String url = "http://support.shortreminders.com/incomingsms/mobapi1.php";


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, jsonArray,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        // pDialog.hide();
                        //responsefromServer=response.toString();
                    Log.i("Prakash","Prakash"+response);

                        List<CallSms> data=dataBaseHelper.getunSyncdata();



                        for(int i=0;i<response.length();i++)
                        {
                            try {
                                JSONObject object=response.getJSONObject(i);
                                String id=object.getString("id");
                                dataBaseHelper.updateContact(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }




                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        });

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(jsonObjReq);

    }

    public void initializeTimerTask() {



        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp

                handler.post(new Runnable() {

                    public void run() {

                        //get the current timeStamp

                        count=count+1;
                    }

                });

            }

        };

    }

    public void startTimer(String number) {

        //set a new Timer

        timer = new Timer();

        phonenumber=number;


        //initialize the TimerTask's job

        initializeTimerTask();



        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms

        timer.scheduleAtFixedRate(timerTask, 0, 1000); //

    }

    public void stoptimertask( int status) {

        //stop the timer, if it's not already null

        if (timer != null) {

            timer.cancel();

            timer = null;

            String token_value="";
            SharedPreferences prefs = ctx.getSharedPreferences("TOKEN", MODE_PRIVATE);

            try {
                token_value = prefs.getString("token", "");//"No name defined" is the default value.

            }catch (Exception e)
            {

            }




            //YYYY-MM-DD HH:MM:SS
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String today= dateFormatter.format(new Date());
            if(status==1000)
            {
                CallSms data=new CallSms(phonenumber,"","",today,"CALL","false",token_value,String.valueOf(1000));
                dataBaseHelper.add_details(data);
            }else
            {
                CallSms data=new CallSms(phonenumber,"","",today,"CALL","false",token_value,String.valueOf(count));
                dataBaseHelper.add_details(data);
            }







            if(internetPermissions.isInternetOn())
            {

                Gson gson=new Gson();
                dataList=new ArrayList<>();
                dataList=dataBaseHelper.getunSyncdata();
                String json= gson.toJson(dataList);


                pushValuesToServer(json);



            }else
            {

            }

            ringcount=0;
            count=0;

        }

    }

}

