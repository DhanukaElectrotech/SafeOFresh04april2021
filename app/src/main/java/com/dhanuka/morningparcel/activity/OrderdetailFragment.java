package com.dhanuka.morningparcel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.OrderItemsAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.OnUpdateOrderListener;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.Utility;
import com.dhanuka.morningparcel.utils.log;

import static android.content.Context.MODE_PRIVATE;


public class OrderdetailFragment extends Fragment implements OnUpdateOrderListener {
    String currency = "";

    @BindView(R.id.txtId)
    TextView txtId;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.txtTagline)
    TextView txtTagline;
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtQty)
    TextView txtQty;
    @BindView(R.id.txtDate)
    TextView txtDate;

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    ImageView txtPhone;
    @BindView(R.id.txtPhone1)
    ImageView txtPhone1;

    @BindView(R.id.txtName1)
    TextView txtName1;
    @BindView(R.id.btnCancel)
    Button btncancel;
    @BindView(R.id.btnReview)
    Button btnReview;


    @BindView(R.id.retailerLayout)
    LinearLayout retailerLayout;
    @BindView(R.id.reviewLayout)
    LinearLayout reviewLayout;


    @BindView(R.id.userLayout)
    LinearLayout userLayout;

    @BindView(R.id.ratigs)
    RatingBar ratigs;


    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtComment1)
    TextView txtComment1;
    @BindView(R.id.txtComment)
    TextView txtComment;
    @BindView(R.id.txtDel)
    TextView txtDel;
    @BindView(R.id.PaymentMode)
    TextView PaymentMode;
    @BindView(R.id.txnId)
    TextView txnId;

    OrderItemsAdapter mAdapter;
    @BindView(R.id.lvProducts)
    ListView lvProducts;
    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;
    @BindView(R.id.btnProceed)
    Button btnProceed;
    @BindView(R.id.imgPayment)
    ImageView imgPayment;
    OrderBean mOrderBean;
    ArrayList<OrderBean.OrderItemsBean> list;
    Preferencehelper prefs;
    String status = "";

    Dialog HelpDialog;
    TextView helpline, shopno, teamno, deliveredsort, pendingsort, holdsort, datesort;
    ImageView callteam, callshop, callhelpline;
    SharedPreferences prefsss;
    int selectedPosition = 0;
    @BindView(R.id.editupload)
    ImageView uploadbill;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orderdetail, container, false);

        //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

        ButterKnife.bind(getActivity(),v);
        prefs = new Preferencehelper(getActivity());
        String result = getArguments().getString("list");
        mOrderBean=new Gson().fromJson(result,OrderBean.class);
        Log.e("mOrderBean", result+"\n"/*+mOrderBean.getmListItems().size()*/);
        SharedPreferences prefs22 = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);


//        if (mOrderBean.getCurrency().equalsIgnoreCase("INR")) {
//            currency = getResources().getString(R.string.rupee);
//        } else {
//            currency = "$";
//        }



        list = mOrderBean.getmListItems();
        prefsss = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefsss.edit();
        status = mOrderBean.getStatus();
try{if(list.size()>0){

}else{
    list=new ArrayList<>();
}
}catch (Exception e){
    list=new ArrayList<>();

}
        mAdapter = new OrderItemsAdapter(getActivity(), list, status, this,0,mOrderBean.getDeliveryMode());
        lvProducts.setAdapter(mAdapter);
        uploadbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (mOrderBean.getComment().isEmpty()) {
            txtComment.setVisibility(View.GONE);
            txtComment1.setVisibility(View.GONE);
        } else {
            txtComment.setVisibility(View.VISIBLE);
            txtComment.setText(mOrderBean.getComment());
            txtComment1.setVisibility(View.VISIBLE);


        }

        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            try {
                if (!mOrderBean.getStatus().equalsIgnoreCase("10")) {
                    userLayout.setVisibility(View.VISIBLE);
                } else {
                    userLayout.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
                userLayout.setVisibility(View.VISIBLE);
            }
            retailerLayout.setVisibility(View.GONE);
            uploadbill.setVisibility(View.VISIBLE);
        } else {
            uploadbill.setVisibility(View.GONE);
            userLayout.setVisibility(View.GONE);
            retailerLayout.setVisibility(View.VISIBLE);

        }

        if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                if (mOrderBean.getCustomerRating().isEmpty()) {
                    reviewLayout.setVisibility(View.VISIBLE);
                    ratigs.setVisibility(View.GONE);

                } else {
                    ratigs.setEnabled(false);
                    ratigs.setRating(Float.parseFloat(mOrderBean.getCustomerRating()));
                    reviewLayout.setVisibility(View.GONE);
                    ratigs.setVisibility(View.VISIBLE);

                }
            } else {

                if (mOrderBean.getSupplierRating().isEmpty()) {
                    reviewLayout.setVisibility(View.VISIBLE);
                    ratigs.setVisibility(View.GONE);

                } else {
                    ratigs.setEnabled(false);
                    ratigs.setRating(Float.parseFloat(mOrderBean.getSupplierRating()));
                    reviewLayout.setVisibility(View.GONE);
                    ratigs.setVisibility(View.VISIBLE);

                }
            }
        } else {
            ratigs.setVisibility(View.GONE);
            reviewLayout.setVisibility(View.GONE);

        }


        String mdt = mOrderBean.getDevliveryDate();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(mOrderBean.getDevliveryDate());
            mdt = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            mdt = mOrderBean.getOrderDate();
        }


        /*       try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(mOrderBean.getDevliveryDate());
            mdt = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            mdt = mOrderBean.getDevliveryDate();
        }
*/
        txtDel.setText("Delivery Mode : " + mOrderBean.getDeliveryMode());
        PaymentMode.setText("Payment Mode : " + mOrderBean.getPaymentMode());

        if (mOrderBean.getDeliveryMode().equalsIgnoreCase("ONLINE DELIVERY")) {
            txnId.setText("Transaction ID : "+mOrderBean.getPaymentTxnId());
            txnId.setVisibility(View.VISIBLE);
        } else {
            txnId.setVisibility(View.GONE);

        }
        String dat1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.ENGLISH);
            Date date1 = sdf.parse(mOrderBean.getOrderDate());
            dat1 = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            dat1 = mOrderBean.getOrderDate();
        }
        txtId.setText("#00" + mOrderBean.getOrderID());
        txtName1.setText(mOrderBean.getSupplierName());
        txtName.setText(mOrderBean.getCustomerName());

        // txtAddress.setText("Flat no. - " + mOrderBean.getFlatNo() + ", Building - " + mOrderBean.getBuilding() + ", society - " + mOrderBean.getSociety());
        txtAddress.setText("Address - " + mOrderBean.getFlatNo() /*+ ", Building - " + mOrderBean.getBuilding()*/ + ", society - " + mOrderBean.getSociety());
        txtDate.setText(dat1);
        txtQty.setText(mOrderBean.getRItemCOunt());

        txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(mOrderBean.getRAmount())));
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                makeCall(mOrderBean.getSupplierPhone());
/*
                } else {
                    makeCall(mOrderBean.getCustomerPhone());

                }*/

            }
        });
        txtPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                makeCall(mOrderBean.getCustomerPhone());
/*
                } else {
                    makeCall(mOrderBean.getCustomerPhone());

                }*/

            }
        });
        try {
            if (mOrderBean.getStatus().equalsIgnoreCase("0") || mOrderBean.getStatus().equalsIgnoreCase(null)) {
                selectedPosition = 1;
                txtStatus.setText("PROCESSING");
                txtStatus.setTextColor(getResources().getColor(R.color.black));
                if (mOrderBean.getDevliveryDate().isEmpty()) {
                    txtTagline.setText("Your Order will be delivered soon");

                } else {
                    txtTagline.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

                }
                //   txtTagline.setText("Your Order will be delivered soon");
                btncancel.setVisibility(View.VISIBLE);

            } else if (mOrderBean.getStatus().equalsIgnoreCase("91")) {
                txtStatus.setText("CANCELLED");
                txtTagline.setText("Your Order has been Cancelled");
                txtStatus.setTextColor(getResources().getColor(R.color.red));
                selectedPosition = 0;
                btncancel.setVisibility(View.GONE);
                retailerLayout.setVisibility(View.GONE);

            } else if (mOrderBean.getStatus().equalsIgnoreCase("1")) {
                txtStatus.setText("ACCEPTED BY STORE");
                selectedPosition = 1;
                txtStatus.setTextColor(getResources().getColor(R.color.black));
                btncancel.setVisibility(View.GONE);
                if (mOrderBean.getDevliveryDate().isEmpty()) {
                    txtTagline.setText("Your Order will be delivered soon");

                } else {
                    txtTagline.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

                }
            } else if (mOrderBean.getStatus().equalsIgnoreCase("2")) {
                txtStatus.setText("ON HOLD");
                selectedPosition = 1;
                txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                btncancel.setVisibility(View.VISIBLE);
                txtTagline.setText("Your Order is on hold will be delivered soon");
            } else if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
                txtTagline.setText("Your order has beed delivered ");
                selectedPosition = 2;
                txtStatus.setText("COMPLETED");
                btncancel.setVisibility(View.GONE);
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                retailerLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            txtStatus.setTextColor(getResources().getColor(R.color.black));
            txtStatus.setText("PROCESSING");
            if (mOrderBean.getDevliveryDate().isEmpty()) {
                txtTagline.setText("Your Order will be delivered soon");

            } else {
                txtTagline.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

            }
        }
        spinnerStatus.setSelection(selectedPosition);
        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            imgPayment.setVisibility(View.VISIBLE);
        } else {
            imgPayment.setVisibility(View.GONE);
        }


        imgPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.opePaymentInfo(getActivity(), mOrderBean.getCompanyID());
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStts = "0";
                String status = spinnerStatus.getSelectedItem().toString();
                if (status.equalsIgnoreCase("PROCESSING")) {
                    txtStatus.setText("PROCESSING");
                    mStts = "0";
                } else if (status.equalsIgnoreCase("CANCELLED")) {
                    mStts = "91";
                    txtStatus.setText("CANCELLED");
                } else if (status.equalsIgnoreCase("ACCEPTED")) {
                    mStts = "1";
                    txtStatus.setText("ACCEPTED BY STORE");
                } else if (status.equalsIgnoreCase("hold")) {
                    mStts = "2";
                    txtStatus.setText("ON HOLD");
                } else if (status.equalsIgnoreCase("COMPLETED")) {
                    mStts = "10";
                    txtStatus.setText("COMPLETED");
                }

                updateOrderStatus(mOrderBean.getOrderID(), "1", mStts);
            }
        });


        HelpDialog = new Dialog(getActivity());
        HelpDialog.setContentView(R.layout.helplayout);
        helpline = HelpDialog.findViewById(R.id.helpline);
        teamno = HelpDialog.findViewById(R.id.teamcontact);
        callhelpline = HelpDialog.findViewById(R.id.cllhelpline);
        callshop = HelpDialog.findViewById(R.id.callph);
        callteam = HelpDialog.findViewById(R.id.callteam);
        shopno = HelpDialog.findViewById(R.id.shopno);
        shopno.setText(mOrderBean.getSupplierPhone());
        callshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall(mOrderBean.getSupplierPhone());

            }
        });
        callteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall("9910004715");


            }
        });
        callhelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("9910004715");


            }
        });

        return v;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {

                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                Log.d("filep", String.valueOf(filePath));
                new ImageCompressionAsyncTask(true).execute(path);
            }
        }


    }

    public void makeHelp(View v) {
        HelpDialog.show();
    }

    public void cancelOrder(View v) {
        status = "91";
        updateOrderStatus(mOrderBean.getOrderID(), "1", "91");

    }

    public void giveReview(View v) {
        Utility.DialogOrderFeedback(getActivity(), mOrderBean.getOrderID(), 0);
    }

    public void makeBackClick(View v) {
        getActivity().onBackPressed();
    }




    public void updateOrderStatus(String oid, String type, String status) {


        final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getActivity());
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //  txtStatus.setText("CANCELLED");
                                mAdapter.notifyDataSetChanged();
                                mAdapter = new OrderItemsAdapter(getActivity(), list, status, (OnUpdateOrderListener) getActivity(),0,mOrderBean.getDeliveryMode());
                                lvProducts.setAdapter(mAdapter);

                                userLayout.setVisibility(View.GONE);
                            } else {

                                FancyToast.makeText(getActivity(), "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(    getActivity(), "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());


                Preferencehelper prefs;
                prefs = new Preferencehelper(getActivity());
                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + oid + "&status=" + status + "&type=" + type + "&contactId=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getActivity());
                    params.put("val", finalparam);
                    Log.d("afterencrptionupdate", finalparam);
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

        Volley.newRequestQueue(getActivity()).add(postRequest);


    }


    public void makeCall(String str) {
        try {
            if (str != null && !str.isEmpty() && str.length() > 2) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + str));

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        PermissionModule permissionModule = new PermissionModule(getActivity());
                        permissionModule.checkPermissions();

                    } else {
                        startActivity(callIntent);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    FancyToast.makeText(getActivity(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }

            } else {
                FancyToast.makeText(getActivity(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            }
        } catch (Exception e) {
            FancyToast.makeText(getActivity(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelItem(OrderBean.OrderItemsBean beanData) {

    }

    @Override
    public void onUpdateOrder(List<OrderBean.OrderItemsBean> orderItem) {
        int qty = 0;
        double price = 0.0;
        for (int a = 0; a < orderItem.size(); a++) {
            qty = qty + Integer.parseInt(orderItem.get(a).getRequestedQty());
            price = price + Double.parseDouble(orderItem.get(a).getOrderRequestedAmount());
        }
        txtQty.setText(qty + "");

        txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(price));

    }

    @Override
    public void onPopupListener(int mPosition, OrderBean.OrderItemsBean mBeanItems) {

    }

    @Override
    public void Cancelclk(boolean cancelclk, OrderBean.OrderItemsBean orderItemsBean, int itemposition) {

    }





    Bitmap bitmap;
    Uri imageuri;
    String filePath;
    final int CROP_PIC = 2;
    private Uri picUri;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203;
    String url, imageFilePath;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private final int REQUEST_CODE = 1010;
    private String PREFS_FILE_PATH = "capture_file_path";

    public void openCamera() {
        SharedPreferences prefs;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(); // create a file to save the image
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.edit().putString(PREFS_FILE_PATH, filePath).commit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath))); // set the image file name
        Log.d("file path in open", String.valueOf(filePath));
        startActivityForResult(intent, REQUEST_CODE);
        Log.d("open camera is called", "called");
    }

    public String getOutputMediaFileUri() {
        return getOutputMediaFile().getAbsolutePath();
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {
                filePath = compressImage(params[0]);


            } catch (Exception e) {
                Log.e("exception", e.getMessage());

            }

            return filePath;
        }

        public String compressImage(String imageUri) {

            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename(imageUri);
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename(String imageUri) {
            File file =getActivity().getExternalFilesDir("MmThinkBiz/Images");

            if (!file.exists()) {
                file.mkdirs();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            addToDatabase();
        }

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Safe'O'Fresh");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "Mtb_" + timeStamp + ".jpg");
        return mediaFile;
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


    String path = "";
    private boolean addedToMasterTable = false;

    private void addToDatabase() {


        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();

            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmServerId(mOrderBean.getCustomerID());
            modle.setmDescription("Grocery_Order_MAster");
            modle.setmImageType("Grocery_Order_MAster");
            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, getActivity());
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, getActivity());
                dao.getlatestinsertedid();
                String.valueOf(mOrderBean.getOrderID());
            }
        });


        final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
        DbImageUpload modle = new DbImageUpload();
        modle.setmDate(JKHelper.getCurrentDate());
        modle.setmImageUploadStatus(0);
        modle.setmDescription("Roster_Closed_MAster");
        modle.setmImageType("Roster_Closed_MAster");
        modle.setmServerId(mOrderBean.getOrderID());
           /* modle.setmDescription("Document_Master");
            modle.setmImageType("Document_Master");
        */
        modle.setmImageId(Integer.parseInt(mOrderBean.getOrderID()));
        modle.setmImagePath(path);
        modle.setmImageName(path);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, getActivity());
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });

        //   setPhotoCount();
    }
}
