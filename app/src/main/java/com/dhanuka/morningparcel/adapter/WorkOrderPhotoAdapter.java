package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;

import com.dhanuka.morningparcel.Helper.DBImageUpload;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.utils.Preferencehelper;
import com.dhanuka.morningparcel.utils.log;

import java.io.File;
import java.util.ArrayList;


public class WorkOrderPhotoAdapter extends RecyclerView.Adapter<WorkOrderPhotoAdapter.DemoViewHolder> {

    ArrayList<DBImageUpload> list = new ArrayList<>();
    int counter = 0;
    private Context mContext;
    private long albumId;
    Preferencehelper prefs;
    ImageLoader imageLoader;
    private EditPlayerAdapterCallback callback;

    public  interface EditPlayerAdapterCallback {

        public void deletePressed();
    }

    public void setCallback(EditPlayerAdapterCallback callback){

        this.callback = callback;
    }

    public WorkOrderPhotoAdapter(ArrayList<DBImageUpload> list, Context context, EditPlayerAdapterCallback callback) {
        this.list = list;
        this.mContext = context;
        this.callback = callback;
        prefs = new Preferencehelper(context);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grid_work_order_item, parent, false);
        return new DemoViewHolder(view, list);

    }

    @Override
    public void onBindViewHolder(final DemoViewHolder holder, final int position) {

        Bitmap bmp = BitmapFactory.decodeFile(list.get(position).getmImagePath());
        holder.ivPhoto.setImageBitmap(bmp);
        holder.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.e("image path=="+list.get(position).getmImagePath());

                File deleteFile=new File(list.get(position).getmImagePath());
                boolean deletefileb =deleteFile.delete();
                log.e("boolen result=="+deletefileb);
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database,holder.itemView.getContext());
                pd.deleteUploadedPhotoByName(list.get(position).getmImageName());
                DatabaseManager.getInstance().closeDatabase();

                if(callback != null)
                {
                    callback.deletePressed();
                }


            }
        });

    }

    public ArrayList<DBImageUpload> getAdapterList() {
        return list;
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        return list.size();
    }

    public class DemoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView ivCross;
        public DemoViewHolder(View itemView, final ArrayList<DBImageUpload> list) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.image);
            ivCross=(ImageView)itemView.findViewById(R.id.iv_cross);
        }
    }

}
