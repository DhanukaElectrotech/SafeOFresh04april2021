package com.dhanuka.morningparcel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.OrderAdapter;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.Clickevent;
import com.dhanuka.morningparcel.events.OrderClicklistner;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.Preferencehelper;
import com.dhanuka.morningparcel.utils.log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class Activity_MettingPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout layout_decision, layout_followup;
    String hoursstr, date_of_installation;
    private int mYear, mMonth, mDay;
    private static String PREFS_FILE_PATH = "capture_file_path";
    LinearLayout camera1;
    private static String filePath;
    ArrayList<String> arrayListProduct = new ArrayList<>();

    ArrayList<String> reason_list = new ArrayList<>();
    ArrayList<String> product_list = new ArrayList<>();

    ArrayList<String> arrayListCategory = new ArrayList<>();
    private static final int REQUEST_CODE = 1010;
    public static final int REQUEST_CODE_GALLERY = 10;
    public static final int REQUEST_CODE_PHONE = 100;
    EditText et_visit_tm, et_followdt;
    private String lastRowMaterTable;
    ProgressDialog prgDialog;
    int masterDataBaseId;
    Dialog Localdialog;
    android.app.AlertDialog.Builder builder;
    Spinner reason, product;

    String catvalue, productvalue;
    ImageView photo1, photo2, photo3;

    Button btn_save, btn_cancel;
    TextView custom_dialog;
    SelectedImagesAdapter mAdapter;
    Button clickok;
    ImageView bctbtn;
    ArrayList<String> mListImages = new ArrayList<>();

    String meetingid;

    EditText comapny_name, company_add, meeting_person, phoneno1, designation, decisionmaker, phoneno2, designation_second, comment, followcmt, et_email;


    com.dhanuka.morningparcel.Helper.Preferencehelper prefs;
    File mFile1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> mImages = new ArrayList<>();
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
                mAdapter.notifyDataSetChanged();
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
                new Activity_MettingPage.ImageCompressionAsyncTask(true).execute(path);
            }
            //   }
        }
    }


    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;


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

        }

    }


    String mtype = "0";
    ArrayList<String> mListLastRows = new ArrayList<>();
    private ImageLoadingUtils utils;
    String path = "";
    private boolean addedToMasterTable = false;

    private void addToDatabase() {


        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();

            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmServerId(meetingid);

            modle.setmDescription("MeetingDetail");
            modle.setmImageType("MeetingDetail");
            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, Activity_MettingPage.this);
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, Activity_MettingPage.this);
                dao.getlatestinsertedid();
                masterDataBaseId = dao.getlatestinsertedid();
                lastRowMaterTable = String.valueOf(masterDataBaseId);
                mListLastRows.add(lastRowMaterTable);
                // String.valueOf(mBeanItems.getOrderdetailID());
            }
        });


        final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
        DbImageUpload modle = new DbImageUpload();
        modle.setmDate(JKHelper.getCurrentDate());
        modle.setmImageUploadStatus(0);

        modle.setmDescription("MeetingDetail");
        modle.setmImageType("MeetingDetail");
        modle.setmServerId(meetingid);
        modle.setmImageId(Integer.parseInt(meetingid));


        modle.setmImagePath(mImagePathDataBase);
        modle.setmImageName(mImageNameDataBase);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, Activity_MettingPage.this);
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });

        //   setPhotoCount();
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

    //fetch
    //order_detail

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(Activity_MettingPage.this) && !JKHelper.isServiceRunning(Activity_MettingPage.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(Activity_MettingPage.this, ImageUploadService.class));
        } else {
            stopService(new Intent(Activity_MettingPage.this, ImageUploadService.class));
            startService(new Intent(Activity_MettingPage.this, ImageUploadService.class));
        }
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

    RecyclerView rvImagescollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_page);
        prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(Activity_MettingPage.this);
        layout_decision = findViewById(R.id.add_page);
        layout_followup = findViewById(R.id.add_follow);
        rvImagescollect = findViewById(R.id.rvImagescollectmeet);
        rvImagescollect.setLayoutManager(new LinearLayoutManager(Activity_MettingPage.this, RecyclerView.HORIZONTAL, false));
        mListImages.clear();
        mAdapter = new SelectedImagesAdapter(this, mListImages);

        rvImagescollect.setAdapter(mAdapter);
        requstforcategory();
        requestforproduct();

        et_visit_tm = findViewById(R.id.et_visit_tm);
        et_followdt = findViewById(R.id.et_followdt);
        camera1 = findViewById(R.id.camera1);
        reason = findViewById(R.id.spinner);
        product = findViewById(R.id.spinner2);

        comapny_name = findViewById(R.id.et_companyname);
        bctbtn=findViewById(R.id.bctbtn);
        company_add = findViewById(R.id.et_companyadd);
        meeting_person = findViewById(R.id.et_personname);
        phoneno1 = findViewById(R.id.et_phone1);
        designation = findViewById(R.id.et_designation1);
        decisionmaker = findViewById(R.id.et_decisionmaker);
        phoneno2 = findViewById(R.id.et_phone2);
        designation_second = findViewById(R.id.et_destination2);
        comment = findViewById(R.id.et_comment);
        followcmt = findViewById(R.id.et_followcmt);
        et_email = findViewById(R.id.et_email);


        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);



        Localdialog = new Dialog(Activity_MettingPage.this);
        builder = new android.app.AlertDialog.Builder(this);
        Localdialog.setContentView(R.layout.custom_dialogbox);
        Localdialog.setCancelable(false);
        Localdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_dialog = Localdialog.findViewById(R.id.custom_dialogtext);
        clickok = Localdialog.findViewById(R.id.clickok);
        clickok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bctbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Activity_MettingPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_MettingPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(Activity_MettingPage.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Activity_MettingPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_MettingPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(Activity_MettingPage.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Activity_MettingPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_MettingPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(Activity_MettingPage.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestForData();


            }
        });


//        camera1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCamera(v,getApplicationContext(),Activity_MettingPage.this);
//
//            }
//        });
        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i <= arrayListCategory.size(); i++) {
                    catvalue = arrayListCategory.get(position);
//                    Toast.makeText(getApplicationContext(),catvalue,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i <= arrayListProduct.size(); i++) {
                    productvalue = arrayListProduct.get(position);
                    //Toast.makeText(getApplicationContext(),productvalue,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void on_add(View view) {
        if (layout_decision.getVisibility() == View.VISIBLE) {
            layout_decision.setVisibility(View.GONE);
        } else if (layout_decision.getVisibility() == View.GONE) {
            layout_decision.setVisibility(View.VISIBLE);
        }


    }

    public void on_followbtn(View view) {
        if (layout_followup.getVisibility() == View.VISIBLE) {
            layout_followup.setVisibility(View.GONE);
        } else if (layout_followup.getVisibility() == View.GONE) {
            layout_followup.setVisibility(View.VISIBLE);
        }


    }

    public void visit_tm(View view) {

        showHourPicker();
    }

    public void on_followdt(View view) {
        panelcleandatepicker();
    }


    public void showHourPicker() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hoursstr = Integer.toString(hourOfDay);


                String minutestr = Integer.toString(minute);
                if (view.isShown()) {

                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                }


                SimpleDateFormat format = new SimpleDateFormat("HH", Locale.US);
                String hour = format.format(new Date());


                Calendar calendar = Calendar.getInstance();
                int hourOfDayy = calendar.get(Calendar.HOUR_OF_DAY);

                if (hourOfDay < 10) {


                    hoursstr = "0" + hourOfDay;

                }
                if (minute < 10) {

                    minutestr = "0" + minutestr;
                }

                et_visit_tm.setText(hoursstr + ":" + minutestr);
                et_visit_tm.setError(null);


                //    Toast.makeText(getApplicationContext(),String.valueOf(hourOfDayy)+"more",Toast.LENGTH_LONG).show();

                //   timePicker1.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_MettingPage.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void panelcleandatepicker() {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_MettingPage.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String monthOfYears = String.valueOf(monthOfYear + 1);
                        String years = String.valueOf(year);
                        String dayOfMonths = String.valueOf(dayOfMonth);


                        if (dayOfMonths.length() == 1)
                            dayOfMonths = "0" + dayOfMonths;

                        if (years.length() == 1)
                            years = "0" + years;

                        if (monthOfYears.length() == 1)
                            monthOfYears = "0" + monthOfYears;
                        date_of_installation = (monthOfYears) + "/" + dayOfMonths + "/" + years;
                        //   date_of_stoppage =   (monthOfYears) + "/" + dayOfMonths + "/" + years;


                        et_followdt.setText(date_of_installation);

                    }
                }, mYear, mMonth, mDay);


//
//        if (DatePickereditstr.equalsIgnoreCase("1"))
//        {
//            edt_startdt.setText(date_of_installation);
//        }
//        else if (DatePickereditstr.equalsIgnoreCase("2"))
//        {
//            edt_enddt.setText(date_of_installation);
//        }
        datePickerDialog.show();

        //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 1000);

//       datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    private void requestForData() {
        prgDialog = new ProgressDialog(Activity_MettingPage.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, getApplicationContext());
                        Log.e("Response45", responses);
                        try {
                            JSONObject jsonObject = new JSONObject(responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("0")) {
                                prgDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed to upload detail", Toast.LENGTH_SHORT).show();

                            } else {

                                meetingid = jsonObject.getString("returnval");
                                custom_dialog.setText(jsonObject.getString("returnmessage"));
                                Localdialog.show();
                                Log.d("MAsterDatabaseID", String.valueOf(masterDataBaseId));
                                Log.d("LastRowMAsterTable", String.valueOf(lastRowMaterTable));

                                addToDatabase();
                                uploadimage();

                            }

                        } catch (Exception e) {
                            prgDialog.dismiss();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
//                        prgDialog.dismiss();


                        Message.message(Activity_MettingPage.this, "Failed to Upload Status");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String param = getString(R.string.ACTIVITY_MEETING) + "&CompanyName=" + comapny_name.getText().toString() + "&ComapnyAddress=" + company_add.getText().toString() + "&MeetingPersonName=" + meeting_person.getText().toString() + "&PhoneNumber=" + phoneno1.getText().toString()
                        + "&Designation=" + designation.getText().toString() + "&DecisionMakingPerson=" + decisionmaker.getText().toString() + "&PhoneNumberDeciMaker=" + phoneno2.getText().toString() + "&DesignationDeciMaker=" + designation_second.getText().toString()
                        + "&VisitTime=" + et_visit_tm.getText().toString() + "&Reason=" + catvalue + "&Comment=" + comment.getText().toString() + "&ProductsOffered=" + productvalue
                        + "&Lat=28.231184194" + "&Long=70.330164790" + "&CID=" + prefs.getPrefsContactId() + "&NextFollowupDt=" + et_followdt.getText().toString() + "&FollowupComment=" + followcmt.getText().toString() + "&EmailId=" + et_email.getText().toString() + "&createdby=" + prefs.getPrefsContactId();
                Log.d("Beforeencrptionpay", param);
                Log.d("BeforeencrptionCHECKOUT", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                params.put("val", finalparam);
                Log.d("afterencrptionCHECKOUT", finalparam);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void requstforcategory() {
        final ProgressDialog prgDialog = new ProgressDialog(Activity_MettingPage.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        prgDialog.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            Log.e("Response45", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String catcodeid = jsonObject1.getString("CatCodeID");
                                String catmaster = jsonObject1.getString("CategoryMaster");
                                String catdescription = jsonObject1.getString("CategoryDescription");
                                arrayListCategory.add(catcodeid);
                                reason_list.add(catdescription);
                                ArrayAdapter listarray = new ArrayAdapter(Activity_MettingPage.this, android.R.layout.simple_spinner_item, reason_list);
                                listarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                reason.setAdapter(listarray);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        prgDialog.dismiss();

                        // Message.message(DEMOCarpoolnow.this, "Failed To Retrieve Data");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String param = getString(R.string.PRODUCT_OFFER) + "&CompanyID=" + prefs.getCID() + "&CreatedBy=" + prefs.getPrefsContactId() + "&CategoryType=" + "CLIENT MEETING REASON";
                Log.d("Beforeencrptionpay", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                params.put("val", finalparam);
                Log.d("afterencrptionCHECKOUT", finalparam);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }


    private void requestforproduct() {
        final ProgressDialog prgDialog = new ProgressDialog(Activity_MettingPage.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        prgDialog.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            Log.e("Response45", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String catcodeid = jsonObject1.getString("CatCodeID");
                                String catmaster = jsonObject1.getString("CategoryMaster");
                                String catdescription = jsonObject1.getString("CategoryDescription");
                                arrayListProduct.add(catcodeid);
                                product_list.add(catdescription);
                                ArrayAdapter listproduct = new ArrayAdapter(Activity_MettingPage.this, android.R.layout.simple_spinner_item, product_list);
                                listproduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                product.setAdapter(listproduct);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        prgDialog.dismiss();

                        // Message.message(DEMOCarpoolnow.this, "Failed To Retrieve Data");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String param = getString(R.string.PRODUCT_OFFER) + "&CompanyID=" + prefs.getCID() + "&CreatedBy=" + prefs.getPrefsContactId() + "&CategoryType=" + "PRODUCT OFFERED";
                Log.d("Beforeencrptionpay", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                params.put("val", finalparam);
                Log.d("afterencrptionCHECKOUT", finalparam);


                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
}
