package com.dhanuka.morningparcel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.GRReportActivity;
import com.dhanuka.morningparcel.activity.NewOrderActivity;
import com.dhanuka.morningparcel.beans.MeetingBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.Returnlistner;
import com.dhanuka.morningparcel.utils.JKHelper;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MeetingAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MeetingBean> list;
    int mPos = 0;
    SharedPreferences prefss;


    public MeetingAdapter(Context context, List<MeetingBean> list) {
        this.context = context;
        this.list = list;

        prefss = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);


    }


    public void addItems(List<MeetingBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<MeetingBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meeting, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MeetingBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;


        viewHolder.meetpersontxt.setText(mCategoryBean.getMeetingPersonName() + " ");
        viewHolder.contactnumbertxt.setText(mCategoryBean.getContactNumber() + " ");
        viewHolder.emailtxt.setText(mCategoryBean.getEmailId() + " ");
        viewHolder.desgtxt.setText(mCategoryBean.getDesignation() + " ");
        viewHolder.decisionmakingpersontxt.setText(mCategoryBean.getDecisionMakingPerson() + " ");
        viewHolder.decisionmakercontacttxt.setText(mCategoryBean.getDecisionMakerContactNo() + " ");
        viewHolder.descmakerdesgtxt.setText(mCategoryBean.getDecisionMakerDesignation() + " ");
        viewHolder.reasontxt.setText(mCategoryBean.getReason() + " ");
        viewHolder.productofferedtxt.setText(mCategoryBean.getProductOffered() + " ");
        viewHolder.contactnumbertxt.setText(mCategoryBean.getContactNumber() + " ");
        viewHolder.createdbytxt.setText(mCategoryBean.getCreatedby() + " ");
        viewHolder.visittimetxt.setText(mCategoryBean.getVisitTime() + " ");
        viewHolder.nextfollowuptxt.setText(mCategoryBean.getNextFollowupdate() + " ");
        viewHolder.createdbytxt.setText(mCategoryBean.getCreatedby() + " ");
        viewHolder.createddatetimetxt.setText(mCategoryBean.getCreateddatetime() + " ");






//        try {
//
//
//            viewHolder.txtinv.setText(mCategoryBean.getCreatedDate());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        if (Double.parseDouble(String.valueOf(mCategoryBean.getBalance())) < 0.0) {
//            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.red_light));
//        } else {
//            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }



        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.meetpersontxt)
        TextView meetpersontxt;
        @Nullable
        @BindView(R.id.contactnumbertxt)
        TextView contactnumbertxt;
        @Nullable
        @BindView(R.id.emailtxt)
        TextView emailtxt;
        @Nullable
        @BindView(R.id.desgtxt)
        TextView desgtxt;
        @Nullable
        @BindView(R.id.descmakerdesgtxt)
        TextView descmakerdesgtxt;

        @Nullable
        @BindView(R.id.decisionmakingpersontxt)
        TextView decisionmakingpersontxt;
        @Nullable
        @BindView(R.id.decisionmakercontacttxt)
        TextView decisionmakercontacttxt;

        @Nullable
        @BindView(R.id.reasontxt)
        TextView reasontxt;
        @Nullable
        @BindView(R.id.productofferedtxt)
        TextView productofferedtxt;
        @Nullable
        @BindView(R.id.createdbytxt)
        TextView createdbytxt;
        @Nullable
        @BindView(R.id.commenttxt)
        TextView commenttxt;
        @Nullable
        @BindView(R.id.visittimetxt)
        TextView visittimetxt;
        @Nullable
        @BindView(R.id.nextfollowuptxt)
        TextView nextfollowuptxt;

        @Nullable
        @BindView(R.id.createddatetimetxt)
        TextView createddatetimetxt;

        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {


        }
    }



}

