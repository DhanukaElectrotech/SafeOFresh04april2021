package com.dhanuka.morningparcel.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.WeeklyReportAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.WeeklySalesBean;
import com.dhanuka.morningparcel.utils.JKHelper;

/**
 *
 */
public class BranchWiseFragment extends Fragment {
    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    List<WeeklySalesBean> mListReport = new ArrayList<>();
    String startdate="",enddate="";

    public static BranchWiseFragment newInstance() {
        Bundle args = new Bundle();
        BranchWiseFragment fragment = new BranchWiseFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_sales, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void init(View view) {
        lvProducts = (RecyclerView) view.findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        getReports();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void getReports() {

        pbLoading.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getActivity());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("responseWEEKNew", responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mListReport = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<WeeklySalesBean>>() {
                                }.getType());
                                lvProducts.setAdapter(new WeeklyReportAdapter(getActivity(), mListReport,1));
                                pbLoading.setVisibility(View.GONE);
                                linearContinue.setVisibility(View.GONE);
                                lvProducts.setVisibility(View.VISIBLE);


                            } else {
                                linearContinue.setVisibility(View.VISIBLE);
                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            linearContinue.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        linearContinue.setVisibility(View.VISIBLE);


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());
                Preferencehelper prefs;
                prefs = new Preferencehelper(getActivity());
                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.WEEKLY_REPORTS) + "&contactid=" + prefs.getPrefsContactId()+"&strdt="+startdate+"&enddt="+enddate;
                    Log.d("Beforeencrptionpastodr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getActivity());
                    params.put("val", finalparam);
                    Log.d("afterencrptionpastodr", finalparam);
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
