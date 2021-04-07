package com.dhanuka.morningparcel.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dhanuka.morningparcel.R;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;


public class
SplashActivity extends AppCompatActivity {
    ImageView imageView;
    Handler handler;
    /*  @BindView(R.id.lgnbtn)
      TextView lgnbtn;
      @BindView(R.id.signupbtn)
      Button signupbtn;
  */
    public static Preferencehelper prefs;
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //  setContentView(R.layout.new_homescreen);
        ButterKnife.bind(this);
        prefs = new Preferencehelper(this);
        // startActivity(new Intent(getApplicationContext(),NewSignUp.class));
       /* signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewSignUp.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        lgnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

//       startActivity(new Intent(getApplicationContext(),NewOrderActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//       startActivity(new Intent(getApplicationContext(),Recurring_order.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        handler = new Handler();
        check();
    }

    public void check() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = null;
                prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);
                mEditorL = prefsL.edit();
                prefsL.edit().remove("categorysup").clear();

                if (prefs.getPrefsTempContactid()==null)
                {
                    prefs.setPrefsTempContactid(prefs.getPrefsContactId());
                }
                prefs.setPrefsContactId(prefs.getPrefsTempContactid());
                if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {

                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057"))

                    {
                        prefs.setPrefsContactId(prefs.getPrefsTempContactid());
                        intent = new Intent(SplashActivity.this, HomeActivity.class);

                        startActivity(intent);
                    }
                    else if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058") || prefs.getPrefsUsercategory().equalsIgnoreCase("1062"))
                    {
                        prefs.setPrefsContactId(prefs.getPrefsTempContactid());
                        intent = new Intent(SplashActivity.this, HomeStoreActivity.class);
                        startActivity(intent);
                    }
                    else
                    {


                        prefs.setPrefsContactId(prefs.getPrefsTempContactid());
                        intent = new Intent(SplashActivity.this, StartActivityNew.class);
                        startActivity(intent);
                    }

                } else if (prefs.getPrefsCountry().equalsIgnoreCase("USA") || prefsL.getString("cntry", "").equalsIgnoreCase("United States")){

                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057"))

                    {
                        prefs.setPrefsContactId(prefs.getPrefsTempContactid());
                        intent = new Intent(SplashActivity.this, OptionChooserActivity.class);

                        startActivity(intent);
                    }
                    else if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")  || prefs.getPrefsUsercategory().equalsIgnoreCase("1062"))
                    {
                        prefs.setPrefsContactId(prefs.getPrefsTempContactid());
                        intent = new Intent(SplashActivity.this, HomeStoreActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        prefs.setPrefsContactId(prefs.getPrefsTempContactid());

                        intent = new Intent(SplashActivity.this, StartActivityNew.class);
                        startActivity(intent);
                    }


                } else
                {

                    intent = new Intent(SplashActivity.this, StartActivityNew.class);
                    startActivity(intent);
                }



            }

        }, 2000);


    }
}
