package com.dhanuka.morningparcel.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.adapters.CategoryAdapter;
import com.dhanuka.morningparcel.restaurant.adapters.CircularBrandAdapter;
import com.dhanuka.morningparcel.restaurant.adapters.DiscountedCardAdapter;
import com.dhanuka.morningparcel.restaurant.adapters.MainCardAdapter;
import com.dhanuka.morningparcel.restaurant.adapters.SecondCardAdapter;
import com.dhanuka.morningparcel.restaurant.models.Categoryhelper;
import com.dhanuka.morningparcel.restaurant.models.Discoountedhelper;
import com.dhanuka.morningparcel.restaurant.models.MainDashboardHelper;
import com.dhanuka.morningparcel.restaurant.models.SecondaCardHelper;
import com.dhanuka.morningparcel.restaurant.models.circularbrandhelper;

public class Restaurantdashboard extends AppCompatActivity {
    @BindView(R.id.maincardcontainer)
    RecyclerView maincard;
    @BindView(R.id.secondarycontainer)
    RecyclerView secondarycard;
    @BindView(R.id.categorycard)
    RecyclerView categorycard;

    @BindView(R.id.discountcontainer)
    RecyclerView discountcontainer;
    @BindView(R.id.circularbrandcontainer)
    RecyclerView circularbrandcontainer;

    MainDashboardHelper mainDashboardHelper;
    SecondaCardHelper secondaCardHelper;
    Categoryhelper categoryhelper;
    Discoountedhelper discoountedhelper;
    circularbrandhelper brandhelper;
    ArrayList<MainDashboardHelper> mainarraylist = new ArrayList<>();
    ArrayList<SecondaCardHelper> secondarraylist = new ArrayList<>();
    ArrayList<Categoryhelper> categorylist = new ArrayList<>();
    ArrayList<Discoountedhelper> discountlist = new ArrayList<>();
    ArrayList<circularbrandhelper> brandlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantdashboard);
        ButterKnife.bind(this);
        for (int i = 0; i < 5; i++) {
            mainDashboardHelper = new MainDashboardHelper();

            mainDashboardHelper.setHeader("Heading1");
            mainDashboardHelper.setMaintext("This is heading");
            mainDashboardHelper.setImagelink("https://franchiseindia.s3.ap-south-1.amazonaws.com/uploads/news/fi/burger-king-joins-hands-with-onl-2b8ac3f381.gif");


            secondaCardHelper = new SecondaCardHelper();
            secondaCardHelper.setHeader("Heading1");
            secondaCardHelper.setMaintext("This is heading");
            secondaCardHelper.setImagelink("https://files.pitchbook.com/website/files/jpg/featured_in_post.jpg");
            mainarraylist.add(mainDashboardHelper);
            secondarraylist.add(secondaCardHelper);

            categoryhelper = new Categoryhelper();

            categoryhelper.setMaintext("Food");
            categoryhelper.setImagelink("https://files.pitchbook.com/website/files/jpg/featured_in_post.jpg");
            categorylist.add(categoryhelper);
            discoountedhelper = new Discoountedhelper();

            discoountedhelper.setMaintext("20%");
            discoountedhelper.setImagelink("https://files.pitchbook.com/website/files/jpg/featured_in_post.jpg");
            discountlist.add(discoountedhelper);
            brandhelper = new circularbrandhelper();
            brandhelper.setBrandname("KFC");
            brandhelper.setTimplace("30 Mins");
            brandhelper.setBrandimage("http://logok.org/wp-content/uploads/2014/06/KFC-logo-1024x768.png");
            brandlist.add(brandhelper);

        }

        secondarycard.setHasFixedSize(true);
        secondarycard.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        secondarycard.setAdapter(new SecondCardAdapter(getApplicationContext(), secondarraylist));
        maincard.setHasFixedSize(true);
        maincard.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        maincard.setAdapter(new MainCardAdapter(getApplicationContext(), mainarraylist));
        categorycard.setHasFixedSize(true);
        categorycard.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        categorycard.setAdapter(new CategoryAdapter(getApplicationContext(), categorylist));

        discountcontainer.setHasFixedSize(true);
        discountcontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        discountcontainer.setAdapter(new DiscountedCardAdapter(getApplicationContext(), discountlist));

        circularbrandcontainer.setHasFixedSize(true);
        circularbrandcontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        circularbrandcontainer.setAdapter(new CircularBrandAdapter(getApplicationContext(), brandlist));




    }
}
