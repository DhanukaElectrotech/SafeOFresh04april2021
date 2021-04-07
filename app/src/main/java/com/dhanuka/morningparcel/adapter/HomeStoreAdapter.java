package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.StoreBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeStoreAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<StoreBean> list;
  //  ArrayList<ItemMasterhelper> mListProductsAll = new ArrayList<>();
String type;
    public HomeStoreAdapter(Context context, List<StoreBean> list,String type) {
        this.context = context;
        this.list = list;
        this.type = type;
        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        
    }

    public void addItems(List<StoreBean> postItems) {
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
        StoreBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.txtCatName1.setVisibility(View.VISIBLE);
        viewHolder.tvRecName.setVisibility(View.GONE);
        viewHolder.txtCatName1.setText(mCategoryBean.getmBean().getStrName());
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

       /* ArrayList<ItemMasterhelper> mListProducts = new ArrayList<>();
         for (int a = 0; a < mListProductsAll.size(); a++) {

         try {
             if (mListProductsAll.get(a).getGroupID().equalsIgnoreCase(mCategoryBean.getStrId())) {
                 mListProducts.add(mListProductsAll.get(a));
             }
         }catch (Exception e){
             e.printStackTrace();
         }
        }
         Log.e("mListProducts",mListProducts.size()+""+mCategoryBean.getStrId());
  */      //viewHolder.mRecyclerView.setAdapter(new StoreproductsAdapter(context, mCategoryBean.getmListProducts(),type));

        viewHolder.position = position;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtCatName)
        TextView tvRecName;
        @BindView(R.id.txtCatName1)
        TextView txtCatName1;
        @BindView(R.id.rv_category)
        RecyclerView mRecyclerView;

        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

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
