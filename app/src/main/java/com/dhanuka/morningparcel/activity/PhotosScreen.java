package com.dhanuka.morningparcel.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dhanuka.morningparcel.Helper.DBImageUpload;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.WorkOrderPhotoAdapter;

import java.util.ArrayList;

public class PhotosScreen extends AppCompatActivity implements View.OnClickListener, WorkOrderPhotoAdapter.EditPlayerAdapterCallback {

    private TextView tvScreenTitle;
    private TextView tvPhotoCount;
    private RecyclerView recyclerviewHomeFragment;
    private Button btnCancel;
    private Button btnSave;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ArrayList<DBImageUpload> mImageCapturedList;
    WorkOrderPhotoAdapter mAdapter;
    String  lastMasterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_screen);
        findViews();
        Bundle b = getIntent().getExtras();

        if (b != null) {
            lastMasterId = b.getString("id");
        }

        setPhotoCount();
        deletePressed();
    }

    private void findViews() {
        tvScreenTitle = (TextView) findViewById(R.id.tv_screen_title);
        tvPhotoCount = (TextView) findViewById(R.id.tv_photo_count);
        recyclerviewHomeFragment = (RecyclerView) findViewById(R.id.recyclerView);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel) {
            // Handle clicks for btnCancel
            finish();
        } else if (v == btnSave) {
            // Handle clicks for btnSave
        }
    }


    @Override
    public void deletePressed() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerviewHomeFragment.setLayoutManager(staggeredGridLayoutManager);

        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ImageUploadDAO pd = new ImageUploadDAO(database, PhotosScreen.this);
        mImageCapturedList = pd.selectAllLastIdPhotos(lastMasterId);
        mAdapter = new WorkOrderPhotoAdapter(mImageCapturedList, PhotosScreen.this, this);
        recyclerviewHomeFragment.setAdapter(mAdapter);


        setPhotoCount();

    }

    private void setPhotoCount() {
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, PhotosScreen.this);
                int count = dao.getCurrentWorkOrderImageCount(lastMasterId);
                tvPhotoCount.setText(count + " Photos");
            }
        });
    }
}
