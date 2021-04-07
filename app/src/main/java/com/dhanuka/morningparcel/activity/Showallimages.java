package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.InterfacePackage.CheckForSDCard;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.adapter.ImageAdapter;
import com.dhanuka.morningparcel.events.onGalleryClickListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


// import com.github.barteksc.pdfviewer.PDFView;
import com.github.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
public class Showallimages extends AppCompatActivity implements onGalleryClickListener {
    private PhotoView layoutClientImages;
    private String id, tble;
    LinearLayout noImgL;
    ImageView gif;
String TAG="SHOW IMAGES";
    String mPath = "";

    @Nullable
    @BindView(R.id.pdfLayout)
    RelativeLayout pdfLayout;
    @Nullable
    @BindView(R.id.txt)
    TextView txt;
    // private PhotoView imageView;
    RecyclerView imagesRV;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    ArrayList<String> mListImages = new ArrayList<>();
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    File pdfFile;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    private ProgressDialog pd2;
    private boolean lg;

    public void makeBackClick(View v) {
        onBackPressed();
    }


    /*private ScaleGestureDetector scaleGestureDetector;
        private Matrix matrix = new Matrix();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_images);
     ButterKnife.bind(this);
     findViews();

        pd2 = new ProgressDialog(Showallimages.this);
        pd2.setTitle("SafeOBuddy");
        pd2.setMessage("Loading...");
        pd2.setCancelable(false);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getString("id");  // tableid 3219 ,  doc ID 12746, ids 12682
            tble = b.getString("tble");
            //type = b.getString("type");
        }
     /*   Glide.with(showallimages.this)
                .load("https://docs.google.com/uc?id=1QLXca0OrpWF4mn9zT3ztSM7YNBTn7tRF")
                .apply(new RequestOptions())
                .into(gif);
*/
        clientImagesRequest();
        /* scaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());*//**/
    }



    private void findViews() {
        gif = (ImageView) findViewById(R.id.gif);
        noImgL = (LinearLayout) findViewById(R.id.noImgL);
        layoutClientImages = (PhotoView) findViewById(R.id.layout_client_images);
        imagesRV = (RecyclerView) findViewById(R.id.imagesRV);
        imagesRV.setLayoutManager(new LinearLayoutManager(Showallimages.this, RecyclerView.HORIZONTAL, false));

    }

    private void clientImagesRequest() {


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);

                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses= jkHelper.Decryptapi(response, Showallimages.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(i);
                                    String systemfilename = loopObjects.getString("systemfilename");
                                    String filepath = loopObjects.getString("filepath");
                                    String ips = loopObjects.getString("ips");
                                mPath=filepath;
                                    String url = "http://" + ips + ".com/ImageHandler.ashx?image=" + systemfilename + "&filePath=" + filepath;
                                    mListImages.add(url);
                                                }

                             /*   if (mListImages.size() > 0) {
                                    imagesRV.setAdapter(new ImageAdapter(showallimages.this, mListImages, showallimages.this::onGalleryClick));
                                    Picasso.with(showallimages.this).load(mListImages.get(0)).into(layoutClientImages);
                                    noImgL.setVisibility(View.GONE);

                                } else {
                                    noImgL.setVisibility(View.VISIBLE);

                                }*/

                                imagesRV.setAdapter(new ImageAdapter(Showallimages.this, mListImages, Showallimages.this::onGalleryClick));
                             //   imagesRV.setAdapter(new DocumentImageAdapter(showallimages.this, mListImages, showallimages.this::onGalleryClick, docname, path, mDocId));

                                String imageUrl = "http://mmthinkbiz.com/ImageHandler.ashx?image=" + mListImages.get(0) + "&filePath=" + mPath;
                                Log.e("imageUrl", imageUrl);


                                if (mListImages.get(0).contains(".pdf")) {
                                    try {

                                        try {
                                            pdfLayout.setVisibility(View.VISIBLE);
                                            fileName = mListImages.get(0).replace("http://mmthinkbiz.com/ImageHandler.ashx?image=","").replace("&filePath="+mPath,"");
                                            mUrl = imageUrl;
                                            txt.setText("File Name : " + mListImages.get(0).replace("http://mmthinkbiz.com/ImageHandler.ashx?image=","").replace("&filePath="+mPath,""));  //   new DownloadTask(showallimages.this, imageUrl, mListImages.get(0));

                                            downloadUrl = mUrl;
                                            downloadFileName = fileName;
                                            new DownloadingTask().execute();
                                            //  new DownloadTask(showallimages.this, mUrl, fileName);
                                            //   pd2.show();
                                            //pdf.setVisibility(View.VISIBLE);
                                            //  new RetrievePdfStream().execute(imageUrl);
                                        } catch (Exception e) {
                                            pd2.dismiss();
                                            e.printStackTrace();
                                            //  Toast.makeText(this, "Failed to load Url :" + e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        //   displaypdf(imageUrl.replace("D:","D%3A").replace("\\","%5C"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //displaypdf(imageUrl);
                                    }
                                } else {
                                    pdfLayout.setVisibility(View.GONE);

                                    Picasso.with(Showallimages.this).load( mListImages.get(0)).into(layoutClientImages);
                                 }


                            } else if (success == 0) {
                                noImgL.setVisibility(View.VISIBLE);
                                Message.message(Showallimages.this, "No Data Exist");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            noImgL.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        noImgL.setVisibility(View.VISIBLE);

                        Message.message(Showallimages.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_ALL_IMAGES) + "&id=" + id + "&tble=" + tble;
                    ;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Showallimages.this);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
                } catch (Exception e) {
                    e.printStackTrace();

                }

                JKHelper.showLog("PARAMS:" + params.toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(Showallimages.this).add(postRequest);
    }

/*
    @Override
    public void onGalleryClick(String img) {
        Picasso.with(showallimages.this).load(img).into(layoutClientImages);

    }
*/



    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            // imageView.setImageMatrix(matrix);
            return true;
        }
    }




    public void viewPdfFile(View view) {
        if (pdfFile != null) {
            PDFView.with(Showallimages.this)
                    .fromfilepath(pdfFile.getAbsolutePath())
                    .start();
        } else {
            new DownloadingTask().execute();
        }


        //new DownloadTask(showallimages.this, mUrl, fileName);

    }

    @Override
    public void onGalleryClick(String img) {
        String imageUrl =img;/* "http://mmthinkbiz.com/ImageHandler.ashx?image=" + img + "&filePath=" + mPath;*/
        Log.e("MSDTRT", imageUrl);
        if (img.contains(".pdf")) {
             try {
                String query = URLEncoder.encode(img /*+ "&filePath=" + mPath*/, "utf-8");
                String url = "http://mmthinkbiz.com/ImageHandler.ashx?image=" + query;
                fileName = img.replace("http://mmthinkbiz.com/ImageHandler.ashx?image=","").replace("&filePath="+mPath,"");
                mUrl = imageUrl;

                // new DownloadTask(showallimages.this, mUrl, fileName);
                if (downloadFileName.equalsIgnoreCase(fileName)) {
                    if (pdfFile != null) {
                        PDFView.with(Showallimages.this)
                                .fromfilepath(pdfFile.getAbsolutePath())
                                .start();
                    } else {
                        downloadUrl = mUrl;
                        downloadFileName = fileName;
                        new DownloadingTask().execute();

                    }
                } else {
                    downloadUrl = mUrl;
                    downloadFileName = fileName;
                    new DownloadingTask().execute();
                }
                try {
                    pdfLayout.setVisibility(View.VISIBLE);
                    txt.setText("File Name : " + img);  //   pd2.show();
                    //  pdf.setVisibility(View.VISIBLE);
                    //   new RetrievePdfStream().execute(imageUrl);
                } catch (Exception e) {
                    pd2.dismiss();
                    e.printStackTrace();
                    //  Toast.makeText(this, "Failed to load Url :" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                //      displaypdf(imageUrl.replace("D:","D%3A").replace("\\","%5C"));
            } catch (Exception e) {
                e.printStackTrace();
           //     displaypdf(imageUrl);
            }
            layoutClientImages.setVisibility(View.GONE);
        } else {
            //  pdf.setVisibility(View.GONE);
             pdfLayout.setVisibility(View.GONE);
            layoutClientImages.setVisibility(View.VISIBLE);
          //  Picasso.with(showallimages.this).load(img).into(layoutClientImages);
            Picasso.with(Showallimages.this).load(imageUrl).into(layoutClientImages);
        }
        //  Picasso.with(showallimages.this).load(imageUrl).into(layoutClientImages);

    }


ProgressDialog progressDialog;
    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Showallimages.this);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                  //  pdfFile = new File(Environment.getExternalStorageDirectory() + "/SafeOBuddy/" + downloadFileName);  // -> filename = maven.pdf

                  //  File file =getApplicationContext().getExternalFilesDir("MmThinkBiz/Images");
                    apkStorage =getApplicationContext().getExternalFilesDir("SafeOBuddy");

                    pdfFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    if (!pdfFile.exists()) {
                        pdfFile.mkdirs();
                    }

                    Uri path = Uri.fromFile(pdfFile);
                    PDFView.with(Showallimages.this)
                            .fromfilepath(pdfFile.getAbsolutePath())
                            .start();

                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl.replace(" ", "%20"));//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }
                if (new CheckForSDCard().isSDCardPresent()) {
                    apkStorage =getApplicationContext().getExternalFilesDir("SafeOBuddy");

                 //   apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "SafeOBuddy");
                } else
                    Toast.makeText(Showallimages.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }



    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;

            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            //      pdf.fromStream(inputStream).load();
            pd2.dismiss();
        }
    }


    private String downloadUrl = "", downloadFileName = "";


    String fileName = "";
    String mUrl = "";

}

