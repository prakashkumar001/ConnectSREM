package com.example.rejectcall.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Prakash on 4/16/2017.
 */

public class UploadDataToServer {

 Context ctx;

    public String params;
    public JSONObject object;

  public UploadDataToServer(Context context,String params)
  {
      ctx=context;
      this.params=params;
      try {
          object=new JSONObject(params);
          Log.i("JJJJJ","JJJJJJ"+object);
      }catch (Exception e)
      {

      }


     // pushValuesToServer();
  }





    public String pushValuesToServer()
    {
// Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

       String responsefromServer="";


        String url = "http://dev192.com/thabresh/test/json.php";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                       // pDialog.hide();
                         //responsefromServer=response.toString();
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

        return responsefromServer;
    }
}

