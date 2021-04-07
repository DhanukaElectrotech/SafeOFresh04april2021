package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.fcm.Config;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.log;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class PromotionSelector extends AppCompatActivity {

    File mFile1;
    @BindView(R.id.llmsg)
    LinearLayout llmsg;
    @BindView(R.id.llnotif1)
    LinearLayout llnotif1;
    @BindView(R.id.llnotif2)
    LinearLayout llnotif2;
    @BindView(R.id.txttypespin)
    Spinner txttypespin;
    @BindView(R.id.composeid)
    EditText composeid;

    @BindView(R.id.notifheader)
    EditText notifheader;
    @BindView(R.id.notifcontent)
    EditText notifcontent;
    ArrayList<String> txttypelist= new ArrayList<>();
    @BindView(R.id.wordcountxt)
    TextView wordcountxt;
    Dialog previewdialog,sendpreview;
    TextView  errortext;
    Button buttonok;
    EditText enterpromono;
    Button oksend;
    String subtype,type;
    @BindView(R.id.sendnotifpreview)
    Button sendnotifpreview;
    @BindView(R.id.sendfinal)
    Button sendfinal;
    ImageView pickimagebtn;
    RecyclerView rvImages;
    SelectedImagesAdapter mAdapter;
    ArrayList<String> mListSelectedIamges = new ArrayList<>();
    Dialog CameraDialog;
    private String filePath;
    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;
    int masterDataBaseId;
    String lastRowMaterTable;
    private boolean addedToMasterTable = false;
    ArrayList<String> mImages;
    String imagetoken="0";
    @BindView(R.id.llnotif3)
    LinearLayout llnotif3;
    AlertDialog.Builder  builder;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {



            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
                    mAdapter.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }

            Bitmap bitmap = null;

            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
                    mAdapter.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }
          
           
          /*  if (requestCode == DROP_IN_REQUEST) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Log.e("SDA", result.getPaymentMethodType().getCanonicalName());
                displayNonce(result.getPaymentMethodNonce(), result.getDeviceData());
            }*/
        } else if (resultCode != RESULT_CANCELED) {
            //  showDialog(((Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR)).getMessage());
        }
      
    }
    String imagename="";

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("Status");
            try {
                JSONObject jsonObject=new JSONObject(message);
                if (jsonObject.getString("success").equalsIgnoreCase("1"))
                {
                    JSONArray jsonArray=jsonObject.getJSONArray("newusercreation");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject newjsonobj=jsonArray.getJSONObject(i);
                        imagename=  newjsonobj.getString("val1");


                    }
                    type=txttypespin.getSelectedItem().toString();
                    sendpromotions();





                }
                else {
                    Toast.makeText(getApplicationContext(),"Image Upload error",Toast.LENGTH_LONG).show();

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_selector);
        ButterKnife.bind(this);
        mImages = new ArrayList<>();
        pickimagebtn = findViewById(R.id.imagepick);
        rvImages = findViewById(R.id.rvImages);



        LocalBroadcastManager.getInstance(PromotionSelector.this).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
     //   registerReceiver(mMessageReceiver, new IntentFilter("GET_SIGNAL_STRENGTH"));

        CameraDialog = new Dialog(PromotionSelector.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            CameraDialog.show();
            }
        });
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PromotionSelector.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });
        rvImages.setLayoutManager(new LinearLayoutManager(PromotionSelector.this, RecyclerView.HORIZONTAL, false));
        mAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImages.setAdapter(mAdapter);

        txttypelist.add(0,"Message");
        txttypelist.add(1,"Notification");
        txttypelist.add(2,"Whatsapp");

        sendpreview=new Dialog(PromotionSelector.this);
        sendpreview.setContentView(R.layout.preview_entercontact);
        enterpromono=sendpreview.findViewById(R.id.enterpromono);
        oksend=sendpreview.findViewById(R.id.sendok);

        previewdialog = new Dialog(PromotionSelector.this);
        previewdialog.setContentView(R.layout.custom_dialogbox);

        buttonok = previewdialog.findViewById(R.id.clickok);
        errortext = previewdialog.findViewById(R.id.custom_dialogtext);

        oksend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=txttypespin.getSelectedItem().toString();
                subtype="preview";
                sendpreview.dismiss();
                sendpromotions();

            }
        });
        sendfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Message"))
                {

                    builder=new AlertDialog.Builder(PromotionSelector.this);
                    builder.setMessage("Are You Sure Want to Send the Message ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            type=txttypespin.getSelectedItem().toString();
                            subtype="final";
                            sendpromotions();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
                else if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Notification"))
                {


                    subtype="final";
                    if (mImages.size()>0)
                    {

                        builder=new AlertDialog.Builder(PromotionSelector.this);
                        builder.setMessage("Are You Sure Want to Send the Notification ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                imagetoken="1";
                                updateserverphotoid();
                                uploadimage();

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                    else
                    {
                        builder=new AlertDialog.Builder(PromotionSelector.this);
                        builder.setMessage("Are You Sure Want to Send the Notification ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                type=txttypespin.getSelectedItem().toString();
                                sendpromotions();

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();



                    }


                }

            }
        });
        sendnotifpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Message"))
                {
                    sendpreview.show();
                }

                else if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Notification"))
                {
                    subtype="preview";

                    if (mImages.size()>0)
                    {
                        imagetoken="1";
                        updateserverphotoid();
                        uploadimage();
                    }
                    else
                    {
                        type=txttypespin.getSelectedItem().toString();


                        type=txttypespin.getSelectedItem().toString();
                        sendpromotions();


                    }


                }
            }
        });
        ArrayAdapter txtarrayadapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,txttypelist);
        txtarrayadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        txttypespin.setAdapter(txtarrayadapter);
        txttypespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Message"))
                {
                    llmsg.setVisibility(View.VISIBLE);
                    llnotif1.setVisibility(View.GONE);
                    llnotif2.setVisibility(View.GONE);
                    wordcountxt.setVisibility(View.VISIBLE);
                    llnotif3.setVisibility(View.GONE);

                }
                if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Notification"))
                {
                    llmsg.setVisibility(View.GONE);
                    llnotif1.setVisibility(View.VISIBLE);
                    wordcountxt.setVisibility(View.GONE);
                    llnotif2.setVisibility(View.VISIBLE);
                    llnotif3.setVisibility(View.VISIBLE);

                }
                if (txttypespin.getSelectedItem().toString().equalsIgnoreCase("Whatsapp"))
                {
                    llmsg.setVisibility(View.GONE);
                    llnotif1.setVisibility(View.GONE);
                    wordcountxt.setVisibility(View.GONE);
                    llnotif2.setVisibility(View.GONE);

                    llnotif3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        composeid.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = editable.toString();
                int currentLength = currentText.length();
                wordcountxt.setText(  currentLength+" characters");
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sendpromotions() {



        final ProgressDialog mProgressBar = new ProgressDialog(PromotionSelector.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, getApplicationContext());
                        try {
                            mProgressBar.dismiss();

                            JSONObject jsonObject= new JSONObject(responses);
                            String returnds= jsonObject.getString("returnds");
                            errortext.setText(returnds);
                            previewdialog.show();
                            buttonok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    previewdialog.dismiss();
                                }
                            });

                        }
                        catch (Exception e)
                        {
                            mProgressBar.dismiss();
                            e.printStackTrace();
                        }







                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mProgressBar.dismiss();
                        Toast.makeText(getApplicationContext(),"Something went wong",Toast.LENGTH_LONG).show();


                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs= new Preferencehelper(getApplicationContext());


                Map<String, String> params = new HashMap<String, String>();

                if (type.equalsIgnoreCase("Message"))
                {
                    if (subtype.equalsIgnoreCase("preview"))
                    {
                        String param = AppUrls.SEND_SMS + "&userid=" + enterpromono.getText().toString() + "&contactid=" + prefs.getPrefsContactId() + "&messagebody=" + composeid.getText().toString()+ "&preview=1";
                        Log.e("BEFORE_PRODUCTS", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                        params.put("val", finalparam);
                        Log.d("afterencrptionmaster", finalparam);


                    }
                    else
                    {


                        String param = AppUrls.SEND_SMS + "&userid=" +"" + "&contactid=" + prefs.getPrefsContactId() + "&messagebody=" + composeid.getText().toString()+ "&preview=0" ;
                        Log.e("BEFORE_PRODUCTS", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                        params.put("val", finalparam);
                        Log.d("afterencrptionmaster", finalparam);
                    }


                }
                else if (type.equalsIgnoreCase("Notification"))
                {
                    SharedPreferences prefF = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String strtoken = prefF.getString("regId", "NO GCM");
                    if (subtype.equalsIgnoreCase("preview"))
                    {
                        String param = AppUrls.SEND_NOTIFICATION + "&ContactID=" + prefs.getPrefsContactId() +"&Type=1"+ "&title="+notifheader.getText().toString()+"&image="+imagename +"&message="+notifcontent.getText().toString()+"&MobileGCMId="+strtoken+"&ImageExist="+imagetoken+"&NotifyType=Promotional";
                        Log.e("BEFORE_PRODUCTS", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                        params.put("val", finalparam);
                        Log.d("afterencrptionmaster", finalparam);


                    }
                    else
                    {




                        String param = AppUrls.SEND_NOTIFICATION + "&ContactID=" +  prefs.getPrefsContactId()+"&Type=0"+"&title="+notifheader.getText().toString()+"&image=" +imagename+"&message="+notifcontent.getText().toString()+"&MobileGCMId="+"&ImageExist="+imagetoken+"&NotifyType=Promotional";
                        Log.e("BEFORE_PRODUCTS", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                        params.put("val", finalparam);
                        Log.d("afterencrptionmaster", finalparam);
                    }


                }





                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
    }


    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
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
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename(String imageUri) {
            File file = getApplicationContext().getExternalFilesDir("MmThinkBiz/Images");
            if (!file.exists()) {
                file.mkdirs();
            }

            String filename = imageUri.substring(imageUri.lastIndexOf("/") + 1);

            log.e("file name in compress image== " + filename);

            String uriSting = (file.getAbsolutePath() + "/" + filename);
            log.e("uri string compress image ==" + uriSting);

            mImagePathDataBase = uriSting;
            mImageNameDataBase = filename;
            mCurrentTimeDataBase = JKHelper.getCurrentDate();

            log.e("mimage path database==" + mImagePathDataBase);
            log.e("image name==" + mImageNameDataBase);
            log.e("current image type==" + mCurrentTimeDataBase);

            return uriSting;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            log.e("post excute  ");
            addToDatabase();
        }

        private void addToDatabase() {

            if (!addedToMasterTable) {
                final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
                DbImageMaster modle = new DbImageMaster();
                modle.setmDate(JKHelper.getCurrentDate());
                modle.setmUploadStatus(0);
                modle.setmDescription("order_master");
                modle.setmImageType("order_master");
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, PromotionSelector.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, PromotionSelector.this);
                    masterDataBaseId = dao.getlatestinsertedid();
                    lastRowMaterTable = String.valueOf(masterDataBaseId);
                    mListLastRows.add(lastRowMaterTable);
                    log.e("string id ============== " + masterDataBaseId);
                }
            });


            final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
            DbImageUpload modle = new DbImageUpload();
            modle.setmDate(mCurrentTimeDataBase);
            modle.setmImageUploadStatus(0);
            modle.setmDescription("order_master");
            modle.setmImageType("order_master");
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, PromotionSelector.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


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

    ArrayList<String> mListLastRows = new ArrayList<>();
    String orderId, catcodeid, catcodedesc, deliverycgarge;

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(PromotionSelector.this) && !JKHelper.isServiceRunning(PromotionSelector.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(PromotionSelector.this, ImageUploadService.class));
        } else {
            stopService(new Intent(PromotionSelector.this, ImageUploadService.class));
            startService(new Intent(PromotionSelector.this, ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, PromotionSelector.this);
                pd.setWorkIdToTable(String.valueOf("11112"), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, PromotionSelector.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf("11112"), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }

}