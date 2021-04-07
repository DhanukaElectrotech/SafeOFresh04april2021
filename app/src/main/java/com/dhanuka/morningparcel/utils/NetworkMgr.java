package com.dhanuka.morningparcel.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.dhanuka.morningparcel.AppController;
import com.dhanuka.morningparcel.Helper.DBImageUpload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created Mr.Mad on 07-05-2019.
 */
public class NetworkMgr {


    private static final String ENCODING_GZIP = "gzip";
    private static final int INPUT_STREAM_BUFFSIZE = 16384;
    private static String UTF8;

    static {
        UTF8 = "UTF-8";
    }

//    public static String httpPost(String postURL) {
//        InputStream is = null;
//        HttpURLConnection conn = null;
//        String response = BuildConfig.FLAVOR;
//        try {
//            conn = (HttpURLConnection) new URL(postURL).openConnection();
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Accept-Charset", UTF8);
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setConnectTimeout(60000);
//            conn.setReadTimeout(60000);
//            conn.setRequestProperty("Accept-Encoding", ENCODING_GZIP);
//            is = conn.getInputStream();
//            response = readIt(is, conn.getContentEncoding());
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                }
//            }
//            if (conn != null) {
//                conn.disconnect();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException ioe2) {
//                    ioe2.printStackTrace();
//                }
//            }
//            if (conn != null) {
//                conn.disconnect();
//            }
//        } catch (Throwable th) {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException ioe22) {
//                    ioe22.printStackTrace();
//                }
//            }
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//        return response;
//    }
      static String serverId;

    public static String uploadToServer(DBImageUpload p, Context ctx) {


        String valtype="";
        valtype=AppController.getValTypeFromDesc(p.getmDescription());
        String pNme = "";
        String imgPath = "";
        String strImageType = "";
        try {
            if (p.getmImageName() != null) {
                pNme = p.getmImageName();
            } else if (!p.getmImageName().isEmpty()) {
                pNme = p.getmImageName();
            } else {
                pNme = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  try {

            if (p.getmImagePath() != null) {
                imgPath = p.getmImagePath();
            } else if (!p.getmImagePath().isEmpty()) {
                imgPath = p.getmImagePath();
            } else {
                imgPath = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (p.getmImageType().equalsIgnoreCase("vehicle_master")) {
                strImageType = "1";
            } else if (p.getmImageType().equalsIgnoreCase("employee_master")) {
                strImageType = "2";
            } else {
                strImageType = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JKHelper.showLog("upload Phto:" +imgPath + "\n"+pNme);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Preferencehelper prefs = new Preferencehelper(ctx);
        serverId = p.getmServerId();
        if (serverId.equalsIgnoreCase("null")) {
            serverId = "0";
            //              params.put("tableid", "0");
        } else {
            serverId = serverId;
//                params.put("tableid", serverId);
        }

        if (p.getmDescription().equalsIgnoreCase("order_master"))
        {


                    valtype="11";
        }

        if (p.getmDescription().equalsIgnoreCase("ContactInfo"))
        {
            com.dhanuka.morningparcel.Helper.Preferencehelper preferencehelper=new com.dhanuka.morningparcel.Helper.Preferencehelper(ctx);

            serverId=preferencehelper.getPrefsContactId();
            valtype="1";
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody=null;
        try {
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/jpeg"), new File(imgPath));

        if (!pNme.toLowerCase().contains(".pdf")) {
            requestBody1 = RequestBody.create(MediaType.parse("image/jpeg"), new File(imgPath));
        } else {
            requestBody1 = RequestBody.create(MediaType.parse("image/pdf"), new File(imgPath));

        }
        Log.e("IIMMHHH", "otherdoc = " + strImageType + "\ntableid = " + serverId + "\nfilenm = " + pNme + "\npath = " + imgPath + "\nvaltype = " + AppController.getValTypeFromDesc(p.getmDescription()) + "\nentrytype = " + p.getmImageType());



        if (imgPath != null && !imgPath.isEmpty()) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tableid", serverId)
                    .addFormDataPart("phoneno", serverId)
                    .addFormDataPart("filenm", pNme)
                    .addFormDataPart("newval", "1")
                    .addFormDataPart("path", imgPath)
                    .addFormDataPart("valtype", valtype)
                    .addFormDataPart("entrytype", p.getmImageType())
                    .addFormDataPart("otherdoc", strImageType)
                    .addFormDataPart("image", new File(imgPath).getName(), requestBody1)
                    .build();
        } else {
            //  Log.e("IIMMHHH",serverId+"\n"+pNme+"\n"+"vehicleotherdocworked"+p.getmImagePath()+"\n"+AppController.getValTypeFromDesc(p.getmDescription())+"\n"+p.getmImageType());

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tableid", serverId)
                    .addFormDataPart("phoneno", serverId)
                    .addFormDataPart("filenm", pNme)
                    .addFormDataPart("otherdoc", strImageType)
                    .addFormDataPart("newval", "1")
                    .addFormDataPart("path", imgPath)
                    .addFormDataPart("valtype", AppController.getValTypeFromDesc(p.getmDescription()))
                    .addFormDataPart("entrytype", p.getmImageType())
                    .build();
        }

        Request request = new Request.Builder().url(AppController.URL_UPLOAD_PROFILE_PHOTO).post(requestBody).build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


         return null;
    }
    public static String uploadToServer1(DBImageUpload p, Context ctx, File mFile) {

try {
    if (!mFile.exists()) {
        mFile.createNewFile();
    }
}catch (Exception e){
    e.printStackTrace();
}

        if(mFile.exists()){
            Log.e("mFile21","mFile exists\n" + mFile.getName() + "\n" + mFile.getAbsolutePath());
        }else{
            Log.e("mFile21", "mFile Not exists\n" + mFile.getName() + "\n" + mFile.getAbsolutePath());

        }

            String pNme = "";
        String imgPath = "";
        String strImageType = "";
        try {
            if (p.getmImageName() != null) {
                pNme = p.getmImageName();
            } else if (!p.getmImageName().isEmpty()) {
                pNme = p.getmImageName();
            } else {
                pNme = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  try {

            if (p.getmImagePath() != null) {
                imgPath = p.getmImagePath();
            } else if (!p.getmImagePath().isEmpty()) {
                imgPath = p.getmImagePath();
            } else {
                imgPath = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (p.getmImageType().equalsIgnoreCase("vehicle_master")) {
                strImageType = "1";
            } else if (p.getmImageType().equalsIgnoreCase("employee_master")) {
                strImageType = "2";
            } else {
                strImageType = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JKHelper.showLog("upload Phto:" +imgPath + "\n"+pNme);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Preferencehelper prefs = new Preferencehelper(ctx);
        serverId = p.getmServerId();
        if (serverId.equalsIgnoreCase("null")) {
            serverId = "0";
            //              params.put("tableid", "0");
        } else {
            serverId = serverId;
//                params.put("tableid", serverId);
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody=null;
        try {
            RequestBody requestBody1 = null;

        if (!pNme.toLowerCase().contains(".pdf")) {
            requestBody1 = RequestBody.create(MediaType.parse("image/jpeg"), mFile);
     Log.e("SDSD","1");
        } else {
            requestBody1 = RequestBody.create(MediaType.parse("image/pdf"), mFile);
            Log.e("SDSD","2");

        }
        Log.e("IIMMHHH", "otherdoc = " + strImageType + "\ntableid = " + serverId + "\nfilenm = " + pNme + "\npath = " + imgPath + "\nvaltype = " + AppController.getValTypeFromDesc(p.getmDescription()) + "\nentrytype = " + p.getmImageType());

        if (imgPath != null && !imgPath.isEmpty()) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tableid", serverId)
                    .addFormDataPart("phoneno", serverId)
                    .addFormDataPart("filenm", pNme)
                    .addFormDataPart("newval", "1")
                    .addFormDataPart("path", imgPath)
                    .addFormDataPart("valtype", AppController.getValTypeFromDesc(p.getmDescription()))
                    .addFormDataPart("entrytype", p.getmImageType())
                    .addFormDataPart("otherdoc", strImageType)
                    .addFormDataPart("image", mFile.getName(), requestBody1)
                    .build();
        } else {
            //  Log.e("IIMMHHH",serverId+"\n"+pNme+"\n"+"vehicleotherdocworked"+p.getmImagePath()+"\n"+AppController.getValTypeFromDesc(p.getmDescription())+"\n"+p.getmImageType());

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tableid", serverId)
                    .addFormDataPart("phoneno", serverId)
                    .addFormDataPart("filenm", pNme)
                    .addFormDataPart("otherdoc", strImageType)
                    .addFormDataPart("newval", "1")
                    .addFormDataPart("path", imgPath)
                    .addFormDataPart("valtype", AppController.getValTypeFromDesc(p.getmDescription()))
                    .addFormDataPart("entrytype", p.getmImageType())
                    .build();
        }

        Request request = new Request.Builder().url(AppController.URL_UPLOAD_PROFILE_PHOTO).post(requestBody).build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


         return null;
    }


    public static String uploadCreateWorkOrderImage(File mFile, String... params) {


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody;
        if (mFile != null) {


            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("entrytype", "workOrder")
                    .addFormDataPart("tableid", params[0])
                    .addFormDataPart("phoneno", "9898989898")
                    .addFormDataPart("filenm", params[1])
                    .addFormDataPart("valtype", "17")
                    .addFormDataPart("path", params[2])

                    .addFormDataPart("", mFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), mFile))
                    .build();
        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("entrytype", "workOrder")
                    .addFormDataPart("tableid", params[0])
                    .addFormDataPart("phoneno", "9898989898")
                    .addFormDataPart("filenm", params[1])
                    .addFormDataPart("valtype", "17")
                    .addFormDataPart("path", params[2])
                    .build();
        }
        Request request = new Request.Builder().url(AppController.URL_UPLOAD_PROFILE_PHOTO).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadCreateWorkOrderImage1(File mFile, String... params) {


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody;
        if (mFile != null) {


            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("entrytype", "Work Order")
                    .addFormDataPart("tableid", params[0])
                    .addFormDataPart("phoneno", params[1])
                    .addFormDataPart("filenm", params[2])
                    .addFormDataPart("valtype", "17")
                    .addFormDataPart("path", params[3])

                    .addFormDataPart("", mFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), mFile))
                    .build();
        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("entrytype", "Work Order")
                    .addFormDataPart("tableid", params[0])
                    .addFormDataPart("phoneno", params[1])
                    .addFormDataPart("filenm", params[2])
                    .addFormDataPart("valtype", "17")
                    .addFormDataPart("path", params[3])
                    .build();
        }
        Request request = new Request.Builder().url(AppController.URL_UPLOAD_PROFILE_PHOTO).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String uploadOrderImage(File mFile, String... params) {


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody;
        if (mFile != null) {

            Log.e("IIMMHHH", params[0] + "\n" + mFile.getName() + "\n" + mFile.getAbsolutePath() + "\n" + "10\norder_master");

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tableid", params[0])
                    .addFormDataPart("phoneno", params[0])
                    .addFormDataPart("filenm", params[1])
                    .addFormDataPart("path", params[2])
                    .addFormDataPart("valtype", "10")
                    .addFormDataPart("entrytype", "order_master")

                    .addFormDataPart("image", new File(params[2]).getName(), RequestBody.create(MediaType.parse("image/jpeg"), new File(params[2])))
                    .build();
        } else {


            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("tableid", params[0])
                    .addFormDataPart("phoneno", params[0])
                    .addFormDataPart("filenm", params[1])
                    .addFormDataPart("path", params[2])
                    .addFormDataPart("valtype", "10")
                    .addFormDataPart("entrytype", "order_master")
                    .build();
        }

        Request request = new Request.Builder().url(AppController.URL_UPLOAD_PROFILE_PHOTO).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String readIt(InputStream is, String encoding) throws IOException, UnsupportedEncodingException {
        BufferedReader r;
        StringBuilder sb = new StringBuilder(INPUT_STREAM_BUFFSIZE);
        if (ENCODING_GZIP.equals(encoding)) {
            r = new BufferedReader(new InputStreamReader(new GZIPInputStream(is), UTF8), INPUT_STREAM_BUFFSIZE);
        } else {
            r = new BufferedReader(new InputStreamReader(is, UTF8), INPUT_STREAM_BUFFSIZE);
        }
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        r.close();
        return sb.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }
}
