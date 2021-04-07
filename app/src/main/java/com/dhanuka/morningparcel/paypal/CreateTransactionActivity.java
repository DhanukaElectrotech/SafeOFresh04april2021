package com.dhanuka.morningparcel.paypal;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/*
import com.braintreepayments.api.models.PaymentMethodNonce;
*/
import com.dhanuka.morningparcel.R;

import java.text.DecimalFormat;


public class CreateTransactionActivity extends AppCompatActivity {

    public static final String EXTRA_PAYMENT_METHOD_NONCE = "nonce";
    String amtamt = "0.00";
    private ProgressBar mLoadingSpinner;
    SharedPreferences mData;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.putExtra("transactionId", txnId);
        setResult(RESULT_OK, intent);



        super.onDestroy();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_transaction_activity);
        ActionBar actionBar = getSupportActionBar();
        mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLoadingSpinner = findViewById(R.id.loading_spinner);
        setTitle(R.string.processing_transaction);
        amtamt = getIntent().getStringExtra("amtamt");
        Log.e("AMTAMT", amtamt);

        if (Double.parseDouble(amtamt) < 1) {
            amtamt = mData.getString("mData", "1.0");
        }


        amtamt=df2.format(Double.parseDouble(amtamt));

      /*  PaymentMethodNonce mnnc=     (PaymentMethodNonce) getIntent().getParcelableExtra(EXTRA_PAYMENT_METHOD_NONCE);
        sendNonceToServer(mnnc);
*/




    }

    private void sendNonceToServer(/*PaymentMethodNonce nonce*/) {
    /*    Callback<Transaction> callback = new Callback<Transaction>() {
            @Override
            public void success(Transaction transaction, Response response) {
                if (transaction.getMessage() != null &&

                        transaction.getMessage().startsWith("created")) {

                    Log.e("dsfsdf",response.getReason()+"\n"+response.getBody().toString()+"\n"+response.getStatus()+"");
                    setStatus(R.string.transaction_complete);
                    setMessage(transaction.getMessage());
                } else {
                    setStatus(R.string.transaction_failed);
                    if (TextUtils.isEmpty(transaction.getMessage())) {
                        setMessage("Server response was empty or malformed");
                    } else {
                        setMessage(transaction.getMessage());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setStatus(R.string.transaction_failed);
                setMessage("Unable to create a transaction. Response Code: " +
                        error.getResponse().getStatus() + " Response body: " +
                        error.getResponse().getBody());
            }
        };
*/

    /*    if (Settings.isThreeDSecureEnabled(this) && Settings.isThreeDSecureRequired(this)) {
        Log.e("123321","1");
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(),
                    Settings.getThreeDSecureMerchantAccountId(this), true, callback);
        } else if (Settings.isThreeDSecureEnabled(this)) {
            Log.e("123321","2");
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(),
            Settings.getThreeDSecureMerchantAccountId(this), callback);
        } else if (nonce instanceof CardNonce && ((CardNonce) nonce).getCardType().equals("UnionPay")) {
            Log.e("123321","3");
            DemoApplication.getApiClient(this).createTransaction(nonce.getNonce(),
                    Settings.getUnionPayMerchantAccountId(this), callback);
        } else {*/
        //creating a retrofit object.

          /*     if (response.body().getmStatus().equalsIgnoreCase("1")) {

                        // Log.e("dsfsdf",response.getReason()+"\n"+response.getBody().toString()+"\n"+response.getStatus()+"");
                        setStatus(R.string.transaction_complete);
                        setMessage(response.body().getMessage());
                    } else {
                        setStatus(R.string.transaction_failed);
                        if (TextUtils.isEmpty(response.body().getMessage())) {
                            setMessage("Server response was empty or malformed");
                        } else {
                            setMessage(response.body().getMessage());
                        }
                    }
             */


        //requestForSubmittingRate(nonce.getNonce(), amtamt);

        Log.e("123321", "4");
      /*      DemoApplication.getApiClient1(this).createTransactionOne(nonce.getNonce(), "24.25",
                    callback1);
    */
    }
    //  }

    private void setStatus(int message) {
        mLoadingSpinner.setVisibility(View.GONE);
        setTitle(message);
        TextView status = findViewById(R.id.transaction_status);
        status.setText(message);
        status.setVisibility(View.VISIBLE);
    }

    //mukesh1076
//jaipur@2020
    private void setMessage(String message) {
        mLoadingSpinner.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.transaction_message);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    String txnId = "failed";

  /*  private void requestForSubmittingRate(final String nonce, final String amt) {


        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://webtecnoworld.com/brainOld/pay.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            Log.e("Response", response);
                            String res = response;
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                                // Log.e("dsfsdf",response.getReason()+"\n"+response.getBody().toString()+"\n"+response.getStatus()+"");
                                setStatus(R.string.transaction_complete);
                                setMessage(jsonObject.getString("msg"));
                                txnId = jsonObject.getString("info");

                            } else {
                                txnId = "failed";
                                setStatus(R.string.transaction_failed);
                                if (TextUtils.isEmpty(jsonObject.getString("msg"))) {
                                    setMessage("Server response was empty or malformed");
                                } else {
                                    setMessage(jsonObject.getString("msg"));
                                }
                            }


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("transactionId", txnId);
                                    setResult(RESULT_OK, intent);
                                    finish();//   finish();
                                }
                            }, 2000);

                        } catch (Exception e) {
                            e.printStackTrace();
                            onBackPressed();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("payment_method_nonce", nonce);
                params.put("amount", amt);
                Log.e("paramsas", params.toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(CreateTransactionActivity.this).add(postRequest);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }
}
