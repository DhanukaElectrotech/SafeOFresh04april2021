package com.dhanuka.morningparcel.paypal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/*import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;*/
import com.dhanuka.morningparcel.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.paytm.Paytm;
import com.dhanuka.morningparcel.utils.Constant;
 /*import com.google.android.gms.wallet.ShippingAddressRequirements;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;*/
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
public class MainActivity extends AppCompatActivity {
    private EditText edt;
    private TextView txt;
    String amt = "00.00";
    static final String EXTRA_PAYMENT_RESULT = "payment_result";
    static final String EXTRA_DEVICE_DATA = "device_data";
    static final String EXTRA_COLLECT_DEVICE_DATA = "collect_device_data";
    private static final String KEY_NONCE = "nonce";

    private static final int DROP_IN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppl);
        edt = findViewById(R.id.edt);
        txt = findViewById(R.id.txt);

        if (savedInstanceState != null) {
           /* if (savedInstanceState.containsKey(KEY_NONCE)) {
                mNonce = savedInstanceState.getParcelable(KEY_NONCE);
            }*/
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       /* if (mNonce != null) {
            outState.putParcelable(KEY_NONCE, mNonce);
        }*/
    }

    public void launchDropIn(View v) {


        if (edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();

        } else if (Float.parseFloat(edt.getText().toString()) < 1) {
            Toast.makeText(this, "Please enter amount more than or equal 1 USD", Toast.LENGTH_SHORT).show();

        } else {


            amt = Float.parseFloat(edt.getText().toString()) + "";
            moveToTransection();


           // startActivityForResult(getDropInRequest().getIntent(this), DROP_IN_REQUEST);
        }
    }
    public String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        return orderId;
    }
    public void moveToTransection() {
        mOrderId = initOrderId() + "_user_" + new Preferencehelper(MainActivity.this).getPrefsContactId();
        mCustId = new Preferencehelper(MainActivity.this).getPrefsContactId();
        generateCheckSum();

    }

    String mOrderId = "";
    String mCustId = "";

    public void generateCheckSum() {

        //getting the tax amount first.
    //    progressDialog = ProgressDialog.show(MainActivity.this, "FarmKey", "Processing...");
      //  progressDialog.setCancelable(false);

        //creating a retrofit object.
     /*   Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
*/
        //creating the retrofit api service
      //  Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constant.M_ID,
                Constant.CHANNEL_ID,
                amt,
                Constant.WEBSITE,
                Constant.CALLBACK_URL+mOrderId,
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


            final ProgressDialog mProgressBar = new ProgressDialog(MainActivity.this);
            mProgressBar.setTitle("Safe'O'Fresh");
            mProgressBar.setMessage("Loading...");
            mProgressBar.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, "http://app-editor.farmykey.in/agriculture/admin799/api/generateChecksumG.php",


                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        mProgressBar.dismiss();

                            try {

JSONObject jsonObject=new JSONObject(response);

                                onStartTransaction(jsonObject.getString("CHECKSUMHASH"), paytm);


                             } catch (Exception e) {
                            }


                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mProgressBar.dismiss();
                            FancyToast.makeText(MainActivity.this, "Something went wrong with pay api.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("MID", paytm.getmId());
                    params.put("ORDER_ID", mOrderId);
                    params.put("CUST_ID", mCustId);
                    params.put("CHANNEL_ID", paytm.getChannelId());
                    params.put("TXN_AMOUNT", paytm.getTxnAmount());
                    params.put("WEBSITE", paytm.getWebsite());
                    params.put("CALLBACK_URL", paytm.getCallBackUrl()
                            + mOrderId);
                    params.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
                    Log.d("afterencrption", params.toString());
                    return params;


                }
            };

            Volley.newRequestQueue(this).add(postRequest);



    }


    public void onStartTransaction(String checksumHash, Paytm paytm) {
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


     /*   paramMap.put("MID", "ShriRa53937508289530");
        paramMap.put("ORDER_ID", "testtet123");
        paramMap.put("CUST_ID", "CUST1234");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", "1.00");
        paramMap.put("WEBSITE", "APPSTAGING");
        paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=testtet123");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHECKSUMHASH", "u5/DpHierzHWsXXB+YA4KMgm2SlFHWpOAUHF+toWULjK5Z+wcsspmFqtNUmXDTs0dAEeOtV1WMF5oW2eX7wDgP7P5ewNpC3jJt3Dt88XbpI=");

*/
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




        Service.initialize(Order, null);
/* */

        Service.startPaymentTransaction(MainActivity.this, true, true,
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
                              //  strTxnId = inResponse.get("TXNID").toString();
                              //  new AsyncSubmitOrder().execute();
                                ErrorMsg("Your Transaction id : "+inResponse.get("TXNID").toString());

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
//                        Toast.makeText(MainActivity.this, "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(MainActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                        ErrorMsg("Transaction cancelled By you");
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);

                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");
                        //Toast.makeText(MainActivity.this, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }


    public void ErrorMsg(String msg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }
  //  private PaymentMethodNonce mNonce;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if (resultCode == RESULT_OK) {
            if (requestCode == DROP_IN_REQUEST) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Log.e("SDA", result.getPaymentMethodType().getCanonicalName());
                displayNonce(result.getPaymentMethodNonce(), result.getDeviceData());
            }
        } else if (resultCode != RESULT_CANCELED) {
            showDialog(((Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR)).getMessage());
        }*/
    }

/*
    private void displayNonce(PaymentMethodNonce paymentMethodNonce, String deviceData) {
        mNonce = paymentMethodNonce;
String strr="Nonce : "+mNonce.getNonce()+"\n";
     */
/*   mNonceIcon.setImageResource(PaymentMethodType.forType(mNonce).getDrawable());
        mNonceIcon.setVisibility(VISIBLE);

        mNonceString.setText(getString(R.string.nonce_placeholder, mNonce.getNonce()));
        Log.e("nnounce", mNonce.getNonce());
        mNonceString.setVisibility(VISIBLE);
*//*

        String details = "";

            details = "";//PayPalActivity.getDisplayString((PayPalAccountNonce) mNonce);


        strr=strr+"details : "+details+"\ndevice data : "+deviceData+"\n";
Log.e("mNNCE",strr);
        txt.setText(strr);
       */
/* Intent intent = new Intent(this, CreateTransactionActivity.class).putExtra("amtamt", amt)
                .putExtra(CreateTransactionActivity.EXTRA_PAYMENT_METHOD_NONCE, mNonce);
        startActivity(intent);*//*


       //  clearNonce();
    }
*/

    protected void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
