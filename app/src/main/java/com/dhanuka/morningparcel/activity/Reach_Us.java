package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.FancyToast;


public class Reach_Us extends AppCompatActivity {

    private WebView mWebView;
    private boolean lg;
    String regId;
    Button button;
    TextView textView;
    private ProgressDialog pd2;
    LinearLayout toolbarbackbtn_layout;
    ImageView imgback, data_update, interchange, searchicon;
    TextView set_Title;

String url="http://safeobuddy.com/";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aboutus);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Reach_Us.this);
        if (getIntent().getStringExtra("type").equalsIgnoreCase("rch")){
            url="http://www.safeokart.com/";
        }else   if (getIntent().getStringExtra("type").equalsIgnoreCase("hlp")){
            url=sharedPreferences.getString("hlp","http://www.safeokart.com/");
        }else   if (getIntent().getStringExtra("type").equalsIgnoreCase("abt")){
            url=sharedPreferences.getString("abt","http://www.safeokart.com/");
        }else   if (getIntent().getStringExtra("type").equalsIgnoreCase("lgl")){
            url=sharedPreferences.getString("lgl","http://www.safeokart.com/");
        }else   if (getIntent().getStringExtra("type").equalsIgnoreCase("trms")){
            url= "http://www.safeokart.com/terms-conditions.php";
        }else   if (getIntent().getStringExtra("type").equalsIgnoreCase("plc")){
            url=sharedPreferences.getString("pp","http://www.safeokart.com/");
        }else   if (getIntent().getStringExtra("type").equalsIgnoreCase("msn")){
            url=sharedPreferences.getString("msn","http://www.safeokart.com/");
        }
        else   if (getIntent().getStringExtra("type").equalsIgnoreCase("contactus")){
            url=sharedPreferences.getString("contactus","http://www.safeokart.com/");
        }
        else   if (getIntent().getStringExtra("type").equalsIgnoreCase("refund")){
            url=sharedPreferences.getString("refund","http://www.safeokart.com/");
        }

        intialization();
        listners();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void listners() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            if (TextUtils.isEmpty(regId)) {
                Log.d("RegisterActivity", "FCM RegId: " + regId);

                try {
                  //  mWebView.loadUrl("https://www.kiasaint.com/contact-us");
                    mWebView.loadUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
             //   mWebView.loadUrl("https://www.kiasaint.com/contact-us");
                mWebView.loadUrl(url);
            }
        } else {
            //  btn.setVisibility(View.VISIBLE);
            FancyToast.makeText(Reach_Us.this, "No Internet Connection Available", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            //  txt1.setVisibility(View.VISIBLE);

        }


    }

    public class myWebClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.getSettings().setBuiltInZoomControls(false);
                    view.loadUrl(url);
                    return true;
                } else {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } else {
//                if (url.startsWith("tel:")){
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }

                view.loadUrl("file:///android_asset/offline.html");
                FancyToast.makeText(Reach_Us.this, "No Internet Connection Available", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                return true;

            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!lg) {


                lg = true;
            } else {
                pd2.dismiss();
                pd2.cancel();
                lg = false;
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {


            // Toast.makeText(MyActivity.this, "page finished", Toast.LENGTH_LONG).show();

            pd2.dismiss();

            super.onPageFinished(view, url);
        }
    }

    public void intialization() {
        pd2 = new ProgressDialog(Reach_Us.this);
        pd2.setTitle("Safe'O'Fresh");
        pd2.setMessage("Loading...");
         pd2.show();

        toolbarbackbtn_layout = findViewById(R.id.backbtn_layout);
        toolbarbackbtn_layout.setVisibility(View.VISIBLE);
        data_update = findViewById(R.id.data_update);
        data_update.setVisibility(View.GONE);
        interchange = findViewById(R.id.interchange);
        interchange.setVisibility(View.GONE);
        searchicon = findViewById(R.id.searchicon);
        searchicon.setVisibility(View.GONE);

        set_Title = findViewById(R.id.txt_set);
        set_Title.setVisibility(View.VISIBLE);
        set_Title.setText(getIntent().getStringExtra("title"));

        imgback = findViewById(R.id.imgback);
        imgback.setVisibility(View.VISIBLE);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        mWebView = (WebView) findViewById(R.id.webview);
//        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);


        //WebSettings webSettings = mWebView.getSettings();

        WebView web = new WebView(this);
        web.setPadding(0, 0, 0, 0);

        // ProgressDialog pd = ProgressDialog.show(this, "","Loading...", true);
        mWebView.setWebViewClient(new myWebClient());
        // pd = ProgressDialog.show(MyActivity.this, "","Please wait..", true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // Button btn= (Button) findViewById(R.id.store);
        //  TextView txt1=(TextView)findViewById(R.id.txt1);

        // btn.setVisibility(View.INVISIBLE);
        //   txt1.setVisibility(View.INVISIBLE);
    }


}