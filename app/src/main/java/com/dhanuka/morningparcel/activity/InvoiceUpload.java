package com.dhanuka.morningparcel.activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.NormalFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.InvoiceDAO;
import com.dhanuka.morningparcel.Helper.InvoiceHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.service.UpdateInvoice;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.log;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

@RequiresApi(api = Build.VERSION_CODES.M)
public class InvoiceUpload extends AppCompatActivity {
    LinearLayout iccamera, icfile;
    String date_of_installation, hoursstr;
    File mFile1;
    private int mYear, mMonth, mDay;
    String filePath;
    String orderid = "";
    ProgressDialog prgDialog;
    SelectedImagesAdapter mAdapter;
    RecyclerView rvImages;
    TextView tv_photo_count;
    MaterialEditText et_invoiceno,et_invoiceamount,et_invoicedt;

    private String PREFS_FILE_PATH = "capture_file_path";
    private final int REQUEST_CODE = 1010;
    private final int REQUEST_CODE_PDF = 2020;
    private final int PICK_IMAGE_MULTIPLE = 2120;
    String mImageNameDataBase = "";
    String mImagePathDataBase, mCurrentTimeDataBase;
    private boolean addedToMasterTable = false;
    int masterDataBaseId;
    String lastRowMaterTable;
    ArrayList<String> mListLastRows = new ArrayList<>();
    ArrayList<String> mListSelectedIamges = new ArrayList<>();
    Button btninvoice;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    ArrayList<String> mImages = new ArrayList<>();
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

            // if (requestCode == REQUEST_CODE_PDF) {

            if (requestCode == Constant.REQUEST_CODE_PICK_FILE) {

                ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                StringBuilder builder = new StringBuilder();
                for (NormalFile file : list) {
                    ArrayList<String> mImages = new ArrayList<>();
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
                    mAdapter.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    getFilename1(path);

                        addToDatabase();

                }

            }


            try {
                // When an Image is picked

                if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE) {
                    log.e("request code true  ");
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    StringBuilder builder = new StringBuilder();
                    for (ImageFile file : list) {
                        ArrayList<String> mImages = new ArrayList<>();
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

                /*              if (requestCode == PICK_IMAGE_MULTIPLE) {
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri = data.getParcelableExtra("path");
                        mImages.add(uri.toString());
                        mAdapter.addItems(mImages);

                        try {
                            final String path = uri.getPath();
                            new ImageCompressionAsyncTask(true).execute(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
  */
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_upload);
        iccamera = findViewById(R.id.cameradoc);
        icfile = findViewById(R.id.fileselect);
        rvImages = findViewById(R.id.rvImages);
        et_invoiceno = findViewById(R.id.et_invoiceno);
        et_invoiceamount = findViewById(R.id.et_invoiceamount);
        et_invoicedt = findViewById(R.id.et_invoicedt);
        tv_photo_count = findViewById(R.id.tv_photo_count);
        btninvoice=findViewById(R.id.btninvoice);
        tv_photo_count.setText("00");
        orderid=getIntent().getStringExtra("orderid");
        rvImages.setLayoutManager(new LinearLayoutManager(InvoiceUpload.this, RecyclerView.HORIZONTAL, false));
        mAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImages.setAdapter(mAdapter);
        getInvoiceDetail();
        et_invoicedt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                panelcleandatepicker();

            }
        });
        btninvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<InvoiceHelper> invoiceist= new ArrayList<>();


                InvoiceHelper invoiceHelper= new InvoiceHelper(Integer.parseInt(orderid),mImageNameDataBase,"",mImagePathDataBase,"invoice_master",et_invoicedt.getText().toString(),et_invoiceamount.getText().toString(),0,orderid,et_invoiceno.getText().toString());

                invoiceist.add(invoiceHelper);
                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        InvoiceDAO invoiceDAO = new InvoiceDAO(database, getApplicationContext());
                        ArrayList<InvoiceHelper> list = invoiceist;
//                invoiceDAO.deleteAll();
                        invoiceDAO.insert(invoiceist);
                    }
                });
                startService(new Intent(InvoiceUpload.this, UpdateInvoice.class).putExtra("orderid",orderid));

            }
        });
        icfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method to show file chooser
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), REQUEST_CODE_PDF);

                /*    Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, REQUEST_CODE_PDF);
*/
            }
        });

        iccamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Choose PDF Doc", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceUpload.this);
                    builder.setTitle("Choose your profile picture");

                    builder.setItems(options, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (options[item].equals("Take Photo")) {
                                Intent intent1 = new Intent(InvoiceUpload.this, ImagePickActivity.class);
                                intent1.putExtra(IS_NEED_CAMERA, true);
                                intent1.putExtra(Constant.MAX_NUMBER, 4);
                                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                                startActivityForResult(intent1, Constant.REQUEST_CODE_TAKE_IMAGE);

                                //   openCamera();
                            } else if (options[item].equals("Choose PDF Doc")) {
                          /*      Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), REQUEST_CODE_PDF);
*/
                                Intent intent4 = new Intent(InvoiceUpload.this, NormalFilePickActivity.class);
                                intent4.putExtra(Constant.MAX_NUMBER, 4);
                                intent4.putExtra(IS_NEED_FOLDER_LIST, true);
                                intent4.putExtra(NormalFilePickActivity.SUFFIX,
                                        new String[]{/*"xlsx", "xls", "doc", "dOcX", "ppt", ".pptx", */"pdf"});
                                startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);

                            } else if (options[item].equals("Choose from Gallery")) {
                           /*     Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
*/
                                Intent intent1 = new Intent(InvoiceUpload.this, ImagePickActivity.class);
                                intent1.putExtra(IS_NEED_CAMERA, true);
                                intent1.putExtra(Constant.MAX_NUMBER, 4);
                                intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                                startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
  /*                Intent intent = new Intent(InvoiceUpload.this, ImagePickerActivity.class);
                                intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
                                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
*/
                           /*     Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
*/
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();



            }
        });
    }

    public void openCamera() {
        SharedPreferences prefs;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(); // create a file to save the image
        prefs = PreferenceManager.getDefaultSharedPreferences(InvoiceUpload.this);
        prefs.edit().putString(PREFS_FILE_PATH, filePath).commit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath))); // set the image file name
        log.e("file path in open camera==" + filePath);
        startActivityForResult(intent, REQUEST_CODE);
        log.e("open camera is called");
    }
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Mmthinkbiz");
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

    public String getOutputMediaFileUri() {
        return getOutputMediaFile().getAbsolutePath();
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
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "MmThinkBiz/Images");

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


    }

    private void addToDatabase() {

        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmDescription("vehicle_master");
            modle.setmImageType("vehicle_master");
            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, InvoiceUpload.this);
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, InvoiceUpload.this);
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
        modle.setmDescription("vehicle_master");
        modle.setmImageType("vehicle_master");
        modle.setmImageId(masterDataBaseId);
        modle.setmImagePath(mImagePathDataBase);
        modle.setmImageName(mImageNameDataBase);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, InvoiceUpload.this);
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });
        //    setPhotoCount();

        tv_photo_count.setText(mListLastRows.size() + "");


    }

    private void addToDatabasedriver() {

        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmDescription("employee_master");
            modle.setmImageType("employee_master");
            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, InvoiceUpload.this);
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, InvoiceUpload.this);
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
        modle.setmDescription("employee_master");
        modle.setmImageType("employee_master");
        modle.setmImageId(masterDataBaseId);
        modle.setmImagePath(mImagePathDataBase);
        modle.setmImageName(mImageNameDataBase);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, InvoiceUpload.this);
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });
        //    setPhotoCount();

        tv_photo_count.setText(mListLastRows.size() + "");


    }

    private void updateserverphotoid(String returnval) {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, InvoiceUpload.this);
                pd.setWorkIdToTable(String.valueOf(returnval), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, InvoiceUpload.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(returnval), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }
        }
    }

    public void uploadimage() {
        if (JKHelper.isConnectedToNetwork(InvoiceUpload.this) && !JKHelper.isServiceRunning(InvoiceUpload.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(InvoiceUpload.this, ImageUploadService.class));
        } else {
            stopService(new Intent(InvoiceUpload.this, ImageUploadService.class));
            startService(new Intent(InvoiceUpload.this, ImageUploadService.class));
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

    public String getFilename1(String filenm) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MmThinkBiz/Images");

        if (!file.exists()) {
            file.mkdirs();
        }
//        filenm

        String[] fl = filenm.split("/");
        String filename = fl[fl.length - 1];
        log.e("filenamefilename== " + filename + "\n");

        String uriSting = (file.getAbsolutePath() + "/" + filename);
        log.e("uri string compress image ==" + uriSting);

        mImagePathDataBase = filenm;
        mImageNameDataBase = filename;
        mCurrentTimeDataBase = JKHelper.getCurrentDate();

        log.e("mimage path database==" + mImagePathDataBase);
        log.e("image name==" + mImageNameDataBase);
        log.e("current image type==" + mCurrentTimeDataBase);

        return uriSting;
    }

    public void panelcleandatepicker() {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(InvoiceUpload.this,
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



                            et_invoicedt.setText(date_of_installation);



                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

//       datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void getInvoiceDetail() {
        ArrayList<InvoiceHelper> invoicegetlist=new ArrayList<>();
        ArrayList<InvoiceHelper> invoicegetlist2=new ArrayList<>();


        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        InvoiceDAO invoiceDAO = new InvoiceDAO(database, this);


        ArrayList<InvoiceHelper> testqueue= new ArrayList<>();
        InvoiceHelper invoiceHelper= new InvoiceHelper();
        testqueue=invoiceDAO.getNewUploadList(orderid);




        Log.d("stringbuildinglist", String.valueOf(invoicegetlist.size()));
        for (int i = 0; i < testqueue.size(); i++) {
            et_invoiceamount.setText(testqueue.get(i).getInvoiceamount());
            et_invoiceno.setText(testqueue.get(i).getInvoiceno());
            et_invoicedt.setText(testqueue.get(i).getInvoicedate());


           // Toast.makeText(getApplicationContext(),testqueue.get(i).getInvoicedate(),Toast.LENGTH_LONG).show();
        }


    }
}
