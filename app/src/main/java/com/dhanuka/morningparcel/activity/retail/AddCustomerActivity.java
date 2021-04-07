package com.dhanuka.morningparcel.activity.retail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.activity.retail.beans.CustomerBean;


public class AddCustomerActivity extends AppCompatActivity {
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.imgCLose)
    ImageView imgCLose;
    @BindView(R.id.mobileNumber)
    EditText mobileNumber;
    @BindView(R.id.name)
    EditText name;
    String mobile = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        ButterKnife.bind(this);
      try{  mobile = getIntent().getStringExtra("mobile");
        if (!mobile.equalsIgnoreCase("1")) {
            mobileNumber.setText(mobile);
        }
        }catch (Exception e){
          e.printStackTrace();
      }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomer(v);
            }
        });
    }

    public void saveCustomer(View view) {
        if (mobileNumber.getText().toString().isEmpty()) {
            Toast.makeText(AddCustomerActivity.this, "Mobile Number Required." , Toast.LENGTH_SHORT).show();
          //  FancyToast.makeText(AddCustomerActivity.this, "", Toast.LENGTH_SHORT, FancyToast.WARNING, false).show();
        } else if (mobileNumber.getText().toString().length() < 9) {
            Toast.makeText(AddCustomerActivity.this, "Mobile Number Not Valid." , Toast.LENGTH_SHORT).show();
            //FancyToast.makeText(AddCustomerActivity.this, "Mobile Number Not Valid.", Toast.LENGTH_SHORT, FancyToast.WARNING, false).show();

        } else {
            makeSignUp(AddCustomerActivity.this);
        }

    }

    ArrayList<CustomerBean> mListCustomers = new ArrayList<>();
    SharedPreferences prefs;
    SharedPreferences.Editor mEditor;

    public void makeSignUp(Context ctx) {
        final ProgressDialog mProgressBar = new ProgressDialog(AddCustomerActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();


        String param = "http://mmthinkbiz.com/MobileService.aspx?method=NewRegistration_Retail_Web&fname=" + name.getText().toString() + "&lname=&gender=&country=India&city=atlanta&username=" + mobileNumber.getText().toString() + "&password=123456&email=add@gmail.com&bday=01/01/1800&mobileno=" + mobileNumber.getText().toString() + "&zip=121002&securityq=&securityp=&address=city&alternatephone=" + mobileNumber.getText().toString() + "&alternatemail=&security2=&securitya2=test&type=0&state=HARYANA&servicetype=CONSUMER&contactid=0&flatno=&city&building=&society=Atlanta";
        //  String param = getString(R.string.all_item_api) +"&ContactID=1&Type=2&supplierid=66738";

         StringRequest postRequest = new StringRequest(Request.Method.POST, param.replace(" ","%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        mProgressBar.dismiss();
                        try {

                            prefs = getSharedPreferences("MORNING_PARCEL_POS",
                                    MODE_PRIVATE);
                            mEditor = prefs.edit();

                            mListCustomers = new ArrayList<>();
                            mListCustomers = new Gson().fromJson(prefs.getString("resp2", ""), new TypeToken<List<CustomerBean>>() {
                            }.getType());  // startActivity(new Intent(NewBillActivity.this, NewBillActivity.class));

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("success") == 1) {
                                if (!jsonObject.getJSONArray("newusercreation").getJSONObject(0).getString("returnmessage").equalsIgnoreCase("UserID Already Exist")) {
                                    CustomerBean customerBean = new CustomerBean();
                                    customerBean.setAlartphonenumber(mobileNumber.getText().toString());
                                    customerBean.setEmpPhoneContact(mobileNumber.getText().toString());
                                    customerBean.setEmpNameContact(name.getText().toString());
                                    customerBean.setFullName(name.getText().toString());
                                    customerBean.setContactID(jsonObject.getJSONArray("newusercreation").getJSONObject(0).getString("returnmessage"));
                                    mListCustomers.add(customerBean);
                                    String mResp = new Gson().toJson(mListCustomers);
                                    mEditor.putString("resp2", mResp);
                                    mEditor.commit();
                                 //   finish();

                                    Intent output = new Intent();
                                    output.putExtra("custId", jsonObject.getJSONArray("newusercreation").getJSONObject(0).getString("returnmessage"));
                                    output.putExtra("mobileNumber", mobileNumber.getText().toString());
                                    setResult(RESULT_OK, output);
                                    finish();

                                } else {
                                   // FancyToast.makeText(AddCustomerActivity.this, jsonObject.getJSONArray("newusercreation").getJSONObject(0).getString("returnmessage"), Toast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                    Toast.makeText(AddCustomerActivity.this, jsonObject.getJSONArray("newusercreation").getJSONObject(0).getString("returnmessage"), Toast.LENGTH_SHORT).show();
                                }
                                //  [{"returnmessage":"UserID Already Exist","uid":null,"gcmstatus":"0","usercategory":"1057","val1":"","val2":null,"val3":null,"val4":null,"Blatlong":null,"CurrentBalance":null,"Country":null,"CustPayId":null,"Paymentoption":null,"OTP":null,"City":null}
                            }
                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("", "");
                try {
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }


    public void onBack(View v) {
        onBackPressed();
    }

}
