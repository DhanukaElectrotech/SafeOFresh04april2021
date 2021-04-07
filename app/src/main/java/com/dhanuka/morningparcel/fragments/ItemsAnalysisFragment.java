package com.dhanuka.morningparcel.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.HomeStoreProductsActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.SubCategoryPagerAdapterNew;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.utils.JKHelper;

/**
 *
 */
public class ItemsAnalysisFragment extends Fragment {

    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    @Nullable
    @BindView(R.id.tlSubCategory)
    SlidingTabLayout tlSubCategory;
    List<MainCatBean.CatBean> list = new ArrayList<>();


    public static ItemsAnalysisFragment newInstance() {
        ItemsAnalysisFragment fragment = new ItemsAnalysisFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_analysis, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
    Preferencehelper prefsNew;
    private void init(View view) {
        ButterKnife.bind(this, view);
        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        prefsNew = new Preferencehelper(getActivity());


        if (prefs.getString("resp1", "").isEmpty()) {
            if (prefsNew.getPrefsCountry().equalsIgnoreCase("India")) {
                getAllProductsindia();

            } else {
                getAllProducts();
            }
        } else {
            storeData();
        }


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    ArrayList<MainCatBean> mListCatMain = new ArrayList<>();
    ArrayList<MainCatBean.CatBean> mListCategories = new ArrayList<>();

    public void storeData() {
        mListCategories = new ArrayList<>();
        mListCatMain = new ArrayList<>();
        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("resp", ""));

            mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
            vpProducts.setVisibility(View.GONE);
            tlSubCategory.setVisibility(View.GONE);
        }

        for (int a = 0; a < mListCatMain.size(); a++) {
            //    for (int b = 0; b < mListCatMain.get(a).getmListCategories().size(); b++) {
            list.addAll(mListCatMain.get(a).getmListCategories());
            //  }
        }


        if (vpProducts != null) {
            vpProducts.setAdapter(new SubCategoryPagerAdapterNew(getChildFragmentManager(), list, Integer.valueOf("55")));
            tlSubCategory.setViewPager(vpProducts);
        }


    }

    @Override
    public void onPause() {

        super.onPause();
    }

    public void getAllProductsindia() {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        // response
                        //      swipeRefresh.setRefreshing(false);
                        Log.e("Response393", responses);


                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String response=jkHelper.Decryptapi(responses,getActivity());


                            mEditor.putString("resp1", response);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {

                                storeData();

                            } else {
                                getAllProductsindia();
                            }

                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(getActivity());

                Map<String, String> params = new HashMap<String, String>();

                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

//                    String param = AppUrls.GET_ITEM_MASTER_URL+"&ContactID=" +prefs.getPrefsContactId() + "&Type=" +"805";
//                    Log.d("Beforeencrptionproduct", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, getActivity());
                    if (prefs.getPrefsCountry().equalsIgnoreCase("India"))
                    {
                        try {
                            // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                            String param = AppUrls.GET_ITEM_MASTER_URL+"&supplierid=66738"
                                    +"&Type=0" +"&ContactID=" +"1";

                            Log.d("Beforeencrptionproduct", param);
                            JKHelper jkHelper = new JKHelper();
                            String finalparam = jkHelper.Encryptapi(param, getActivity());
                            params.put("val", finalparam);
                            Log.d("afterencrptionproduct", finalparam);
                            return params;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return params;
                        }


                    }
                    else
                    {
                          try {
                            // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                            String param = AppUrls.GET_ITEM_MASTER_URL+"&supplierid="+ prefs.getPrefsContactId()
                                    +"&Type=0" +"&ContactID=" +"1";

                            Log.d("Beforeencrptionproduct", param);
                            JKHelper jkHelper = new JKHelper();
                            String finalparam = jkHelper.Encryptapi(param, getActivity());
                            params.put("val", finalparam);
                            Log.d("afterencrptionproduct", finalparam);
                            return params;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return params;
                        }

                    }




            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(postRequest);
    }
    public void getAllProducts() {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //      swipeRefresh.setRefreshing(false);
                        Log.e("Response393", response);


                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,getActivity());


                            mEditor.putString("resp1", responses);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                     storeData();




                            } else {
                                getAllProducts();
                            }

                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(getActivity());

                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = AppUrls.GET_ITEM_MASTER_URL+"&ContactID=" +prefs.getPrefsContactId() + "&Type=" +"805";
                    Log.d("Beforeencrptionproduct", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getActivity());
                    params.put("val", finalparam);
                    Log.d("afterencrptionproduct", finalparam);
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }




            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(postRequest);
    }

}
