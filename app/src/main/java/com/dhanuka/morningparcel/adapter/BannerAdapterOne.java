package com.dhanuka.morningparcel.adapter;
/*
 *
 *
 *  */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

/*import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;*/

public class BannerAdapterOne extends PagerAdapter {


    int mPosition=0;
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public BannerAdapterOne(Context context, ArrayList<String> images) {
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
        View myImageLayout = inflater.inflate(R.layout.item_banners_one, view, false);
        RelativeLayout card = (RelativeLayout) myImageLayout.findViewById(R.id.card);
        RelativeLayout card1 = (RelativeLayout) myImageLayout.findViewById(R.id.card1);
        RelativeLayout card2 = (RelativeLayout) myImageLayout.findViewById(R.id.card2);
      if (position==0){
          card.setVisibility(View.VISIBLE);
          card1.setVisibility(View.GONE);
          card2.setVisibility(View.GONE);
      }else  if (position==1){
          card1.setVisibility(View.VISIBLE);
          card.setVisibility(View.GONE);
          card2.setVisibility(View.GONE);
      }else  if (position==2){
          card2.setVisibility(View.VISIBLE);
          card.setVisibility(View.GONE);
          card1.setVisibility(View.GONE);
      }

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