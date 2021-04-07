package com.dhanuka.morningparcel.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;
import com.dhanuka.morningparcel.Helper.DbComplaintReportModel;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.Showallimages;
import java.util.List;

public class ComplaintReportAdapter extends RecyclerView.Adapter<ComplaintReportAdapter.ViewHolder> {

    private List<DbComplaintReportModel> mData;
    private int mSelectedPosition;
    private int mTouchedPosition;
    private boolean isClick = false;
    String id;
    String date;

    public ComplaintReportAdapter(List<DbComplaintReportModel> data) {
        mData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_complaint_report, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.tvComplaintID.setText(mData.get(i).getmComplaintID());
        viewHolder.tvFullname.setText(mData.get(i).getmFullname());
        viewHolder.tvVehiclenumber.setText(mData.get(i).getmVehiclenumber());
//        viewHolder.tvVehiclenumber.setText("HR55J2356");

        viewHolder.tvDrivername.setText(mData.get(i).getmDrivername());
        //viewHolder.tvDATE.setText(mData.get(i).getmDATE());
        viewHolder.tvCreatedDate.setText(mData.get(i).getmCreatedDate());
        //viewHolder.tvCreatedBy.setText(mData.get(i).getmCreatedBy());
        viewHolder.tvComment.setText(mData.get(i).getmComment());
        viewHolder.tvActionTaken.setText(mData.get(i).getmActionTaken());
        //viewHolder.tvTYPE.setText(mData.get(i).getmTYPE());
        viewHolder.tvAddress.setText(mData.get(i).getmReason());
        viewHolder.tvLat.setText(mData.get(i).getmLat());
        viewHolder.tvLong.setText(mData.get(i).getmLong());
        viewHolder.tvEmpPhoneNo.setText(mData.get(i).getmEmpPhoneNo());
  //      viewHolder.tvEmpPhoneNo.setText("7042741404");
        viewHolder.tvVendorName.setText(mData.get(i).getmVendorName());
        viewHolder.tvDriverNo.setText(mData.get(i).getmDriverNo());
        viewHolder.tvDept.setText(mData.get(i).getmDept());



/*
        viewHolder.frameLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //JKHelper.call(viewHolder.itemView.getContext(),mData.get(i).getmPhoneNumeber());
                if(mData.get(i).getLat()!=null) {
                    viewHolder.itemView.getContext().startActivity(new Intent(viewHolder.itemView.getContext(), ShowLatLongOnMapScreen.class).putExtra("latlong", mData.get(i).getLat() + ";" + mData.get(i).getLng()));
                }
            }
        });
*/
        if (mData.get(i).getmTYPE().equalsIgnoreCase("3"))
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           //id = mData.get(i).getmPaymentID();
                                                           Intent intent = new Intent(viewHolder.itemView.getContext(), Showallimages.class);
                                                           intent.putExtra("id", mData.get(i).getmComplaintID()); //3114
                                                           intent.putExtra("tble", "Contact_Complaint");
                                                           viewHolder.itemView.getContext().startActivity(intent);
                                                       }
                                                   }
            );
        }
    }


    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvComplaintID;
        private TextView tvFullname;
        private TextView tvVehiclenumber;
        private TextView tvDrivername;
        private TextView tvDATE;
        private TextView tvCreatedDate;
        private TextView tvCreatedBy;
        private TextView tvComment;
        private TextView tvActionTaken;
        private TextView tvTYPE;
        private TextView tvAddress;
        private TextView tvLat;
        private TextView tvLong;
        private TextView tvEmpPhoneNo;
        private TextView tvVendorName;
        private TextView tvDriverNo;
        private TextView tvDept;

        ImageView ivCall,ivMapCross,ivMapPin;
        FrameLayout frameCall,frameLocation;
        /*
                public ImageView arrow;
        */
        public LinearLayout llCall;

        public ViewHolder(View itemView) {
            super(itemView);

            tvComplaintID = (TextView)itemView.findViewById(R.id.tvComplaintID);
            tvFullname = (TextView)itemView.findViewById(R.id.tvFullname);
            tvVehiclenumber = (TextView)itemView.findViewById(R.id.tvVehiclenumber);
            tvDrivername = (TextView)itemView.findViewById(R.id.tvDrivername);
            //tvDATE = (TextView)itemView.findViewById(R.id.tvDATE);
            tvCreatedDate = (TextView)itemView.findViewById(R.id.tvCreatedDate);
            //tvCreatedBy = (TextView)itemView.findViewById(R.id.tvCreatedBy);
            tvComment = (TextView)itemView.findViewById(R.id.tv_comment);
            tvActionTaken = (TextView)itemView.findViewById(R.id.tvActionTaken);
            //tvTYPE = (TextView)itemView.findViewById(R.id.tvTYPE);
            tvAddress = (TextView)itemView.findViewById(R.id.tvAddress);  // check
            tvLat = (TextView)itemView.findViewById(R.id.tvLat);
            tvLong = (TextView)itemView.findViewById(R.id.tvLong);
            tvEmpPhoneNo = (TextView)itemView.findViewById(R.id.tvEmpPhoneNo);
            tvVendorName = (TextView)itemView.findViewById(R.id.tvVendorName); // check
            tvDriverNo = (TextView)itemView.findViewById(R.id.tvDriverNo);
            tvDept = (TextView)itemView.findViewById(R.id.tvDept);
            //ivMapPin=(ImageView)itemView.findViewById(R.id.iv_map_pin);

/*
             arrow = (ImageView)itemView.findViewById( R.id.arrow );
*/
        }
    }
}

