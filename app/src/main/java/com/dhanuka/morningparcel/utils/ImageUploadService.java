package com.dhanuka.morningparcel.utils;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dhanuka.morningparcel.Helper.DBImageUpload;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;*/

public class ImageUploadService extends Service {

    ArrayList<DBImageUpload> list;
    int cntr = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        JKHelper.showLog("Upload Service Create");
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                                       @Override
                                                       public void run(SQLiteDatabase database) {
                                                           ImageUploadDAO dao = new ImageUploadDAO(database, getApplicationContext());
                                                           list = dao.selectUploadPhotos();
                                                       }
                                                   }
        );
        if (JKHelper.isConnectedToNetwork(getApplicationContext())) {

            Log.e("listlist",list.size()+"");
            uploadToServermain();
        } else {
            stopSelf();
        }

    }

    private void uploadToServermain() {
        for (int j = 0; j < list.size(); j++) {
            DBImageUpload img = list.get(j);
            String serverId = img.getmServerId();
            String sImagePath = img.getmImagePath();
            String sImageName = img.getmImageName();
            if (img.getmDescription().equalsIgnoreCase("ContactInfo"))
            {
                com.dhanuka.morningparcel.Helper.Preferencehelper preferencehelper=new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());

                serverId=preferencehelper.getPrefsContactId();

            }
            if (serverId.equalsIgnoreCase("null") || serverId.isEmpty() || serverId.equalsIgnoreCase("0")) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, getApplicationContext());
                pd.deleteUploadedPhotoById(img.getmId());
                int count = pd.getMasterTableItems(img.getmImageId());
                if (count == 0) {
                    JKHelper.showLog("Delete All Data of COunt=" + count);
                    ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                    iDao.deleteMaterPhotoById(img.getmImageId());
                }
                DatabaseManager.getInstance().closeDatabase();
                // Ignore
            } else if (sImagePath == "null") {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, getApplicationContext());
                pd.deleteUploadedPhotoById(img.getmId());
                int count = pd.getMasterTableItems(img.getmImageId());
                if (count == 0) {
                    JKHelper.showLog("Delete All Data of COunt=" + count);
                    ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                    iDao.deleteMaterPhotoById(img.getmImageId());
                }
                DatabaseManager.getInstance().closeDatabase();
                // Ignore
            } else if (sImageName == "null") {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, getApplicationContext());
                pd.deleteUploadedPhotoById(img.getmId());
                int count = pd.getMasterTableItems(img.getmImageId());
                if (count == 0) {
                    JKHelper.showLog("Delete All Data of COunt=" + count);
                    ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                    iDao.deleteMaterPhotoById(img.getmImageId());
                }
                DatabaseManager.getInstance().closeDatabase();
                // Ignore
            } else {
                Log.e("KJKJKJ","KLKLKJKJ");
                cntr = j;
                uploadToServer(j);
            }
        }
    }


    public class UploadImageToServer extends AsyncTask<Void, Void, String> {
        String userResponse;
        DBImageUpload dbImageUpload;

        private UploadImageToServer(DBImageUpload mp) {
            this.dbImageUpload = mp;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            String msg = null;
            if (dbImageUpload.getmImageName()!=null){
            /*   if (dbImageUpload.getmImageName().contains(".pdf")){
                   UploadPdfHelper uploadPdfHelper=new UploadPdfHelper();
                   userResponse=      uploadPdfHelper.KYC_Upload(ImageUploadService.this,dbImageUpload);
               }else{*/


                userResponse = NetworkMgr.uploadToServer(dbImageUpload, ImageUploadService.this);
           // }
            }


                Intent intent = new Intent("GPSLocationUpdates");
                // You can also include some extra data.
                intent.putExtra("Status", userResponse);
                Bundle b = new Bundle();
                intent.putExtra("Location", b);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            Log.d("ImageUpload", "Response: " + userResponse);

            return userResponse;

        }

        protected void onPostExecute(String result1) {
            try {

                JSONObject jsonObject = new JSONObject(userResponse);
                int result = jsonObject.getInt("success");
                if (result == 1) {
                    log.e("one photo uploaded success");
                    SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                    ImageUploadDAO pd = new ImageUploadDAO(database, getApplicationContext());
                    Preferencehelper prefs = new Preferencehelper(getApplicationContext());
                    pd.setServerDetails(dbImageUpload.getmId(), prefs.getWorkOrderId(), 1);
                    //TODO delete all data from database and folder
                    pd.deleteUploadedPhotoById(dbImageUpload.getmId());

                    int count = pd.getMasterTableItems(dbImageUpload.getmImageId());
                    if (count == 0) {
                        JKHelper.showLog("Delete All Data of COunt=" + count);
                        ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                        iDao.deleteMaterPhotoById(dbImageUpload.getmImageId());
                    }
                    DatabaseManager.getInstance().closeDatabase();
                    File file =getApplicationContext().getExternalFilesDir("MmThinkBiz/Images");

                         String filePath = (file.getAbsolutePath() + "/" + dbImageUpload.getmImageName());
                    //Log.e(" fp in success uplodd=", "" + filePath);
                    File deleteFile = new File(filePath);
                    boolean deletefileb = deleteFile.delete();
                    log.e("boolen result in success==" + deletefileb);
                }

            } catch (Exception e) {
                if (list.size() - 1 == cntr) {
                    stopSelf();
                }
            }

        }


    }

    private void uploadToServer(int counter) {


        final DBImageUpload img = list.get(counter);


        new UploadImageToServer(img).execute();

/*
        NetworkMgr.uploadToServer(img, ImageUploadService.this);

        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = generateUploadParams(img);
        JKHelper.showLog("Request Param:" + params.toString());
        client.post(AppController.URL_UPLOAD_PROFILE_PHOTO, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        String jsonString = new String(response);
                        log.e("response success == " + jsonString);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        JKHelper.showLog("ERROR:" + e.getMessage());
                        if (JKHelper.isConnectedToNetwork(getApplicationContext())) {
                            //uploadToServer();
                        } else {
                            stopSelf();
                        }
                    }
                });

*/
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
