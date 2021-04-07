package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.CategoryActivity;
import com.dhanuka.morningparcel.beans.MainCatBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MainCatBean.CatBean> list;

    public CategoryAdapter(Context context, List<MainCatBean.CatBean> list) {
        this.context = context;
        this.list = list;
    }
    public void addItems(List<MainCatBean.CatBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cat_item_list, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainCatBean.CatBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvRecName.setText(mCategoryBean.getStrName());
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        if (!list.get(position).getStrImage().isEmpty()){
            Log.e("MIMGGG","http://mmthinkbiz.com/ImageHandler.ashx?image="+list.get(position).getStrImage()+"&filePath="+mCategoryBean.getFilepath());
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(context)
                .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image="+list.get(position).getStrImage()+"&filePath="+mCategoryBean.getFilepath())
              .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
              .into(viewHolder.img2);
        viewHolder.position = position;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvName)
        TextView tvRecName;

        @BindView(R.id.imageView2)
        ImageView img2;
        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //img.setOnClickListener(this);
            img2.setOnClickListener(this);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            tvRecName.setTypeface(font);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                 case R.id.imageView2:
                     Log.e("listlist",list.size()+"");


                     Intent intent = new Intent(context, CategoryActivity.class);
                     Bundle args = new Bundle();
                     args.putSerializable("list", (Serializable) list);
                     intent.putExtra("BUNDLE",args);
                     intent.putExtra("mPosition",position);
                     context.startActivity(intent);

                    break;
                case R.id.tvName:
                   // context.startActivity(new Intent(context, ProductsActivity.class));

                    break;
            }

        }
    }


}
