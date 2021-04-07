package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.events.onGalleryClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> list;
    onGalleryClickListener mListener;
    public ImageAdapter(Context context, List<String> list,onGalleryClickListener mListener) {
        this.mListener = mListener;
        this.context = context;
        this.list = list;
    }
    public void addItems(List<String> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_images, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
         //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(context)
                .load(mCategoryBean)
              .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
              .into(viewHolder.img2);
        viewHolder.position = position;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView img2;
        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //img.setOnClickListener(this);
            img2.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                 case R.id.image:

                     mListener.onGalleryClick(list.get(getAdapterPosition()));
                    /* Log.e("listlist",list.size()+"");


                     Intent intent = new Intent(context, CategoryActivity.class);
                     Bundle args = new Bundle();
                     args.putSerializable("list", (Serializable) list);
                     intent.putExtra("BUNDLE",args);
                     intent.putExtra("mPosition",position);
                     context.startActivity(intent);
*/
                    break;
                  }

        }
    }


}
