package com.dhanuka.morningparcel.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.demo.Api;
import com.dhanuka.morningparcel.demo.Checksum;
import com.dhanuka.morningparcel.demo.Constant;
import com.dhanuka.morningparcel.utils.JKHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalleTransactionActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.llRs100)
    LinearLayout llRs100;
    @BindView(R.id.llRs500)
    LinearLayout llRs500;
    @BindView(R.id.llRs200)
    LinearLayout llRs200;
    @BindView(R.id.llRs1000)
    LinearLayout llRs1000;
    @BindView(R.id.edtAmount)
    EditText edtAmount;
    @BindView(R.id.btnSubmit)
    TextView btnSubmit;
    @BindView(R.id.txtWalletAmt)
    TextView txtWalletAmt;

    public void makeBackClick(View v) {
        onBackPressed();
    }

    Preferencehelper prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walle_transaction);
        ButterKnife.bind(this);
        prefs = new Preferencehelper(WalleTransactionActivity.this);
        if (getIntent().hasExtra("strAmt")) {
            edtAmount.setText("" + getIntent().getStringExtra("strAmt"));
        }
        else
        {
            edtAmount.setText(prefs.getPREFS_currentbal().trim().replace("₹","").replace("-",""));

        }

        llRs1000.setOnClickListener(this);
        llRs100.setOnClickListener(this);
        llRs500.setOnClickListener(this);
        llRs200.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        txtWalletAmt.setText("Available Balance    ₹" + prefs.getPREFS_currentbal());


    }

    String strTxnId = "";

    public void onLayoutCLick(String strAmt) {
        if (edtAmount.getText().toString().isEmpty()) {
            edtAmount.setText(strAmt);
        } else {
            edtAmount.setText((Integer.parseInt(edtAmount.getText().toString()) + Integer.parseInt(strAmt)) + "");
        }

    }

    @Override
    public void onClick(View v) {
        if (v == llRs1000) {
            onLayoutCLick("10000");
        } else if (v == llRs100) {
            onLayoutCLick("1000");

        } else if (v == llRs200) {
            onLayoutCLick("3000");

        } else if (v == llRs500) {
            onLayoutCLick("5000");

        } else if (v == btnSubmit) {
            if (edtAmount.getText().toString().isEmpty()) {
                edtAmount.setError("Please enter an valid amount");
            } else if (Double.parseDouble(edtAmount.getText().toString()) < 1) {
                edtAmount.setError("Please enter an valid amount");
            }/* else if (Float.parseFloat(edtAmount.getText().toString()) < 1000) {
                edtAmount.setError("Amount should be greater than Rs. 1000");

            } */else {
                //   strTxnId = "20200812111212800110168423940590828";
//
                // addMoneyToWallet();
                moveToTransection();
            }
        }

    }

    public String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        return orderId;
    }

    public void moveToTransection() {
        mOrderId = initOrderId() + "_user_" + new Preferencehelper(WalleTransactionActivity.this).getPrefsContactId();
        mCustId = new Preferencehelper(WalleTransactionActivity.this).getPrefsContactId();
        generateCheckSum();

    }

    String mOrderId = "";
    String mCustId = "";


    public void onStartTransaction(String checksumHash, com.dhanuka.morningparcel.demo.Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getProductionService();

        /* PaytmPGService Service = PaytmPGService.getStagingService();
         */
        HashMap<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters
        paramMap.put("MID", Constant.M_ID);
        paramMap.put("ORDER_ID", mOrderId);
        paramMap.put("CUST_ID", mCustId);
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl() + mOrderId);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
        paramMap.put("CHECKSUMHASH", checksumHash);


        PaytmOrder Order = new PaytmOrder(paramMap);
        Log.e("TRANS_PRMS", paramMap.toString());





/*

        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "210lkldfka2a27");
        paramMap.put("CUST_ID" , "mkjNYC1227");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/



		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);
/*

Array ( [MID] => ShriRa53937508289530
[ORDER_ID] => testab123 [CUST_ID] => CUST1234 [INDUSTRY_TYPE_ID] => Retail [CHANNEL_ID] => WAP [TXN_AMOUNT] => 1.00
 [WEBSITE] => APPSTAGING
 [CALLBACK_URL] => https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=testab123
 [CHECKSUMHASH] => INbxKqQWzUov2mAPGxj9CINwo4eTQ+x1oY0M4Ko01ET3bZna57tk8o1xfed4R6mm8HQb9FqiU9j1KprImdUC0E9PPDKj/9QrGZrWpu4wlX4= )







        {MID=ShriRa53937508289530,
        CALLBACK_URL=https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=c4512e1de13440a6901c2dbea4dee85f,
         TXN_AMOUNT=1, ORDER_ID=c4512e1de13440a6901c2dbea4dee85f, WEBSITE=APPSTAGING ,
          INDUSTRY_TYPE_ID=Retail, CHECKSUMHASH=IYDZws4WV2ewhs8O9pGCsMjzc9JGuYacCGpiZDu5CKMhNRMf8lLyvraOiqeAerD1ftI4iKTFDk+QyM7L2UNQeM26R3YMsRrmqMZRuyYnk5c=,
          CHANNEL_ID=WAP, CUST_ID=db77ade9cca449ada3aea7551ca4a718}
*/

        Service.startPaymentTransaction(WalleTransactionActivity.this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

					/*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                        try {
                      /*      String resp = inResponse.toString();
                            JSONArray jsonArray = new JSONArray(resp.replace("Bundle", ""));
                            JSONObject object = jsonArray.getJSONObject(0);
*/


                            if (inResponse.get("STATUS").toString().equalsIgnoreCase("TXN_SUCCESS")) {
                                strTxnId = inResponse.get("TXNID").toString();
                                // strTxnId = "20200812111212800110168423940590828";

                                addMoneyToWallet();
                            } else {
                                ErrorMsg(inResponse.get("RESPMSG").toString());

                            }       //[{STATUS=TXN_SUCCESS, BANKNAME=STATE BANK OF INDIA, ORDERID=ORDER10000211, TXNAMOUNT=1.00, TXNDATE=2019-01-28 14:38:08.0, MID=ShriRa53937508289530, TXNID=20190128111212800110168492800195667, RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=4036217121962950, CURRENCY=INR, GATEWAYNAME=HDFC, RESPMSG=Txn Success}]


                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMsg("Failed to Proceed The Order. please Contact The Admin");

                        }
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");


                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        // Error Message describes the reason for failure.
//                        Toast.makeText(CheckoutActivity.this, "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(WalleTransactionActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                        ErrorMsg("Transaction cancelled By you");
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);

                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");
                        //Toast.makeText(CheckoutActivity.this, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }

    double totalCartValue = 1.0;
    ProgressDialog progressDialog;

    public void generateCheckSum() {
        totalCartValue = Double.parseDouble(edtAmount.getText().toString().replace(" ", "").replace("₹", "").replace("$", ""));
        //getting the tax amount first.
        progressDialog = ProgressDialog.show(WalleTransactionActivity.this, getString(R.string.app_name), "Processing...");
        progressDialog.setCancelable(false);

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final com.dhanuka.morningparcel.demo.Paytm paytm = new com.dhanuka.morningparcel.demo.Paytm(
                Constant.M_ID,
                Constant.CHANNEL_ID,
                totalCartValue + "",
                Constant.WEBSITE,
                Constant.CALLBACK_URL,
                Constant.INDUSTRY_TYPE_ID
        );

        Log.e("CHK_PRM", paytm.getmId() + "\n" +
                mOrderId + "\n" +
                mCustId + "\n" +
                paytm.getChannelId() + "\n" +
                paytm.getTxnAmount() + "\n" +
                paytm.getWebsite() + "\n" +
                paytm.getCallBackUrl() + mOrderId + "\n" +
                paytm.getIndustryTypeId());

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                mOrderId,
                mCustId,
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl()
                        + mOrderId,
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                //          initializePaytmPayment(response.body().getChecksumHash(), paytm);
                try {
                    Log.e("chcksm", new Gson().toJson(response.body()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
//                {"CHECKSUMHASH":"Rnm3BlsA=","ORDER_ID":"ORDER100001284_user_1","payt_STATUS":"1","PAYTM_MERCHANT_KEYE":"_N%76LsTYDJFzvNm"}

                onStartTransaction(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });



    }

    public void ErrorMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WalleTransactionActivity.this);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void addMoneyToWallet() {


        final ProgressDialog mProgressBar = new ProgressDialog(WalleTransactionActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("please wait...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseSignup", response);
                        mProgressBar.dismiss();


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, WalleTransactionActivity.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                prefs.setPREFS_currentbal((Double.parseDouble(prefs.getPREFS_currentbal()) + Double.parseDouble(edtAmount.getText().toString())) + "");
                                Intent intent = new Intent();
                                intent.putExtra("mWalletAmount", (Double.parseDouble(prefs.getPREFS_currentbal()) + Double.parseDouble(edtAmount.getText().toString())) + "");
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WalleTransactionActivity.this);
                                builder.setMessage("Failed to add money to wallet , retry");
                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addMoneyToWallet();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.setCancelable(false);
                                dialog.show();

                            }

                        } catch (Exception e) {
                            //mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String servicetype;

                try {
                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);
                    String cntry = prefs1.getString("cntry", "India");
//method=CreateWalletTransaction_Web&Status=0&Type=1&=66280&=10&=bill1&=1&TransactionDate=08-11-2020
                    JKHelper jkHelper = new JKHelper();
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                    String mTodayDate = df.format(c.getTime());

                    String param = "method=CreateWalletTransaction&Status=0&Type=1&Contactid=" + prefs.getPrefsContactId() + "&Amount=" + edtAmount.getText().toString() + "&TransactionDate=" + mTodayDate + "&BillNo=" + strTxnId + "&CreatedBy=" + prefs.getPrefsContactId();
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, WalleTransactionActivity.this);
                    params.put("val", param);
                    Log.e("afterenc", param);
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






}