package com.dhanuka.morningparcel.utils;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.CartOrderDAO;
import com.dhanuka.morningparcel.Helper.DBCartDataUpload;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;*/

public class OrderUploadService extends Service {

    ArrayList<DBCartDataUpload> list;
    int cntr = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        JKHelper.showLog("Upload Service Create");
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                                       @Override
                                                       public void run(SQLiteDatabase database) {
                                                           CartOrderDAO dao = new CartOrderDAO(database, getApplicationContext());
                                                           list = dao.selectAll();
                                                       }
                                                   }
        );
        if (JKHelper.isConnectedToNetwork(getApplicationContext())) {

            Log.e("listlist", list.size() + "");
            uploadToServermain();
        } else {
            stopSelf();
        }

    }

    private void uploadToServermain() {
        for (int j = 0; j < list.size(); j++) {
            DBCartDataUpload img = list.get(j);

            Log.e("KJKJKJ", "KLKLKJKJ");
            cntr = j;
            if (img.getmStatus() == 0) {
                uploadToServer(j);
            }

        }
    }


    public void checkoutOrderDetails(DBCartDataUpload mBean) {



        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderUploadService.this);
                            Log.e("KJHASJ", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                                CartOrderDAO iDao = new CartOrderDAO(database, getApplicationContext());
                                iDao.deleteMaterPhotoById(mBean.getmId());
                             DatabaseManager.getInstance().closeDatabase();


                            }
                        } catch (Exception e) {
                             e.printStackTrace();
                         }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                com.dhanuka.morningparcel.Helper.Preferencehelper prefs;
                prefs = new Preferencehelper(OrderUploadService.this);

                Map<String, String> params = new HashMap<String, String>();


                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
/*
                    String param = getString(R.string.CREATE_ORDER_DETAIL) + "&OID=" + orderId + "&CID=" + prefs.getPrefsContactId()
                            + "&DetailID=" + "0" + "&CreatedBy=" + prefs.getPrefsContactId() + "&ItemID=" + mArrayList.get(checkoutTime).getItemID() + ""
                            + "&ItemDesc=" + mArrayList.get(checkoutTime).getItemName() + "" + "&RQty=" + mArrayList.get(checkoutTime).getQuantity() + ""
                            + "&UOM=" + mArrayList.get(checkoutTime).getSaleUOM() + "&Rate=" + mArrayList.get(checkoutTime).getSaleRate() +
                            "&RAmount=" + (mArrayList.get(checkoutTime).getQuantity() * Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate())) + ""
                            + "&Status=" + "0" + "&Type=" + "0";
                    Log.d("Beforeencrptioncreate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderUploadService.this);
              */      params.put("val", mBean.getmDescription());
                    Log.d("afterencrptioncreate", mBean.getmDescription());
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }



    private void uploadToServer(int counter) {


        final DBCartDataUpload img = list.get(counter);
        checkoutOrderDetails(img);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        JKHelper.showLog("Upload Service Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        JKHelper.showLog("Upload Service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
   /* private RequestQueue rQueue;
    private void uploadPDF(Context ctx, final String pdfname, Uri pdffile){

        InputStream iStream = null;
        try {

            iStream = ctx.getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppController.URL_UPLOAD_PROFILE_PHOTO,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.e("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                               // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                jsonObject.toString().replace("\\\\","");

                             } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                          //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                *//*
     * If you want to add more parameters with the image
     * you can do it here
     * here we have only one parameter with the image
     * which is tags
     * *//*
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }

                *//*
     *pass files using below method
     * *//*
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                   // params.put("filename", new DataPart(pdfname ,inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(MainActivity.this);
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
*/


}
