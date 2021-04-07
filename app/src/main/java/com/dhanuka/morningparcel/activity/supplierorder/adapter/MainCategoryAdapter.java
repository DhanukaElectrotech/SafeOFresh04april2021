package com.dhanuka.morningparcel.activity.supplierorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.MainCatBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainCategoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MainCatBean> list;

    public MainCategoryAdapter(Context context, List<MainCatBean> list) {
        this.context = context;
        this.list = list;
    }

    public void addItems(List<MainCatBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_grocery, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainCatBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvRecName.setText(mCategoryBean.getStrName());
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        viewHolder.mRecyclerView.setAdapter(new CategoryAdapter(context, mCategoryBean.getmListCategories()));

        viewHolder.position = position;
if (position==list.size()-1){
    viewHolder.arogya.setVisibility(View.VISIBLE);
    viewHolder.arogya.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                //https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN"));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    viewHolder.arogya.setVisibility(View.GONE);

}else{
    viewHolder.arogya.setVisibility(View.GONE);

}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtCatName)
        TextView tvRecName;
        @BindView(R.id.arogya)
        ImageView arogya;
        @BindView(R.id.rv_category)
        RecyclerView mRecyclerView;

        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Bold.ttf");
            tvRecName.setTypeface(font); }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.imageView2:

                    break;
                case R.id.tvName:

                    break;
            }

        }
    }


}
