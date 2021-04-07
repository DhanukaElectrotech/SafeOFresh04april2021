package com.dhanuka.morningparcel.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.fcm.Config;

public class SendFCM extends Service {

    Preferencehelper prefs;
    public SendFCM() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefs = new Preferencehelper(getApplicationContext());
        SharedPreferences prefF = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String strtoken = prefF.getString("regId", "NO GCM");
        sendRegistrationToServer(strtoken);
        return super.onStartCommand(intent, flags, startId);


    }


    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.
        if(token!=null) {


            StringRequest postRequest = new StringRequest(Request.Method.POST,getString(R.string.URL_NOTIFICATION_REGISTER),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Response", response);
                            String res = response;
                            String jsonString = new String(response);

                            Intent myService = new Intent(getApplicationContext(), SendFCM.class);
                            stopService(myService);






                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error

                            //   Log.e("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    //params.add("contactid", "7580");


                    params.put("contactid","0");
                    params.put("GCMID", token);
                    Log.d("param for gcm registn","" + params.toString());

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(SendFCM.this).add(postRequest);
        }
        else{
            Toast.makeText(getApplicationContext(),token, Toast.LENGTH_SHORT).show();
        }
    }
}
