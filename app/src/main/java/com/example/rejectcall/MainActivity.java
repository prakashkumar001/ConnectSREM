package com.example.rejectcall;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rejectcall.database.DataBaseHelper;
import com.example.rejectcall.model.CallSms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends RuntimePermissionActivity {

    Button submit,write_textfile,token;
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmsCallLogs";
    private static final int REQUEST_PERMISSIONS = 20;

    EditText token_text;
    DataBaseHelper dataBaseHelper;
    String[] stringsarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper=new DataBaseHelper(getApplicationContext());
        submit=(Button)findViewById(R.id.submit);
        write_textfile=(Button)findViewById(R.id.exportdata);
        token_text=(EditText)findViewById(R.id.token_text);
        token=(Button)findViewById(R.id.token);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainActivity.super.requestAppPermissions(new
                            String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string
                            .runtime_permissions_txt
                    , REQUEST_PERMISSIONS);

        }else
        {

        }



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {

                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                startActivity(intent);
            }
        }

        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(token_text.getText().toString().length()>0)
                {
                    SharedPreferences.Editor editor = getSharedPreferences("TOKEN", MODE_PRIVATE).edit();
                    editor.putString("token", token_text.getText().toString());
                    editor.commit();

                    token.setText("");

                }else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Token",Toast.LENGTH_SHORT).show();
                }
            }
        });

        write_textfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                File f=new File(path);
                if (!f.exists()) {
                    f.mkdirs();
                }
                File file = new File (path + "/CallSmslog.txt");
                List<CallSms> rawData=dataBaseHelper.getallData();

                Log.i("DataDataData","DataDataData"+rawData);

                stringsarr=new String[rawData.size()];
       /* for (Contact cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);

           // stringsarr[]
        }*/


                for(int i=0;i<rawData.size();i++)
                {
                    String log = "api_id: "+rawData.get(i).getId()+System.getProperty("line.separator")+"api_type: " + rawData.get(i).getType() +System.getProperty("line.separator")+ "api_usrmobno: " + rawData.get(i).getSenderPhone()+System.getProperty("line.separator")+ "api_mobno: " +rawData.get(i).getOwnerPhone()+System.getProperty("line.separator")+"api_msg : " +rawData.get(i).getMessage() +System.getProperty("line.separator")+ "api_issync :" +rawData.get(i).getIsSync() +System.getProperty("line.separator")+"api_ts :"+rawData.get(i).getTime();

                    stringsarr[i]=log;


                }



                dialog("Your data has been exported as a file!!!",file,stringsarr);


                //exporttoText (file, stringsarr);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    dialog("Are you sure want to clear Call Log ?",null,null);

                }else
                {
                    Toast.makeText(getApplicationContext(),"You cant delete messages below KITKAT version",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void clearALL()
    {
        //delete all call logs
        Uri callLog = Uri.parse("content://call_log/calls");
        int rs1 = getContentResolver().delete(callLog, null, null);

//delete all sms
       // Uri inboxUri = Uri.parse("content://sms/");
       // int rs2 = getContentResolver().delete(inboxUri, Telephony.Sms._ID + "!=?", new String[]{"0"});

        try {
            Uri inboxUri = Uri.parse("content://sms/");
            int rs2 = getContentResolver().delete(inboxUri, Telephony.Sms._ID + "!=?", new String[]{"0"});


        }catch (Exception e)
        {

        }


    }

    public void exporttoText(File file, String[] data)
    {

        try
        {
            FileOutputStream fos = new FileOutputStream(file);

                    for (int i = 0; i<data.length; i++)
                    {
                        fos.write(data[i].getBytes());
                        if (i < data.length-1)
                        {
                            fos.write("\n".getBytes());
                            fos.write("====================================================".getBytes());
                            fos.write("\n\n".getBytes());

                        }
                    }




                } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void dialog(final String title, final File file, final String[] data)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage(title)
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                        if(title.equalsIgnoreCase("Are you sure want to clear Call Log ?"))
                        {
                            clearALL();
                        }else if(title.equalsIgnoreCase("Your data has been exported as a file!!!"))
                        {
                            exporttoText(file,data);
                        }
                        dialog.dismiss();


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


}
