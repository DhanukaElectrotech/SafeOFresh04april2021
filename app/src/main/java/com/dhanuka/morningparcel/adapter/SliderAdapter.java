package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.EnquiryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 */

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private List<String> icon;
    private List<String> iconName;

    public SliderAdapter(Context context, List<String> icon, List<String> iconName) {
        this.context = context;
        this.icon = icon;
        this.iconName = iconName;
    }

    @Override
    public int getCount() {
        return icon.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imageView = view.findViewById(R.id.imageview);
        Button btnCallBack = view.findViewById(R.id.btn);
      /*  Glide.with(context)
                .load(icon.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(imageView);
   */     Picasso.with(context).load(icon.get(position)).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(imageView);

        // imageView.setBackgroundResource(icon.get(position));
        btnCallBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EnquiryActivity.class).putExtra("type", position + ""));
            }
        });
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
