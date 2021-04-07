package com.dhanuka.morningparcel.adapter;
/*
 *
 *
 *  */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.activity.BannerWebview;
import com.dhanuka.morningparcel.activity.OtherProductsActivity;

/*import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;*/

public class BannerAdapter extends PagerAdapter {


    int mPosition = 0;
    private ArrayList<CatcodeHelper> images;
    private LayoutInflater inflater;
    private Context context;

    public BannerAdapter(Context context, ArrayList<CatcodeHelper> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.item_banners, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!images.get(position).getClickurl().isEmpty()) {
                        if (images.get(position).getClickurl().equalsIgnoreCase("Deals of the Day")) {
                            context.startActivity(new Intent(context, OtherProductsActivity.class).putExtra("type", "1"));

                        } else if (images.get(position).getClickurl().equalsIgnoreCase("New Listing")) {
                            context.startActivity(new Intent(context, OtherProductsActivity.class).putExtra("type", "2"));

                        } else if (images.get(position).getClickurl().equalsIgnoreCase("Repeat Order")) {
                            context.startActivity(new Intent(context, OtherProductsActivity.class).putExtra("type", "3"));

                        } else {

                            context.startActivity(new Intent(context, BannerWebview.class).putExtra("clickurl", images.get(position).getClickurl()));
                        }
                    }
                    //https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN
                    //      Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    //       intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN"));
                    //       context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        //myImage.setImageResource(images.get(position));


        //Picasso.with(context).load(images.get(position).getBannerurl()).placeholder(R.drawable.no_image).into(myImage);
        Glide.with(context)
                .load(images.get(position).getBannerurl())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(myImage);

        // myImage.setImageResource(images.get(position).getImage());
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}/*extends FragmentStatePagerAdapter {

    private Context context;
    private String[] content;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        } else {
            return false;
        }
    }

    public BannerAdapter(FragmentManager fm,
                         Context context, String[] data) {
        super(fm);
        this.context = context;
        content = data;
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return BannerFragment.newInstance(content[position]);
    }

    @Override
    public int getCount() {
        return content == null ? 0 : content.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return content[position];
    }

}*/