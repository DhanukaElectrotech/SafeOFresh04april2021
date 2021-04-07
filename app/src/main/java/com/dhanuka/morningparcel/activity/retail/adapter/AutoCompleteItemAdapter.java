package com.dhanuka.morningparcel.activity.retail.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhanuka.morningparcel.activity.retail.beans.dbitemmaster;
import com.dhanuka.morningparcel.utils.Preferencehelper;
import com.dhanuka.morningparcel.utils.log;


/**
 * Created by Mr.Mad on 10/3/2017.
 */
public class AutoCompleteItemAdapter extends BaseAdapter implements Filterable {
String strContactid="";
    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<dbitemmaster> resultList = new ArrayList<dbitemmaster>();
int searchType=0;
    public AutoCompleteItemAdapter(Context context,String strContactid,int searchType) {
        mContext = context;
       this. strContactid = strContactid;
       this. searchType = searchType;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public dbitemmaster getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.auto_complete_layout, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.countryName)).setText(getItem(position).getmItemName());
        return convertView;
    }

     List<dbitemmaster> books;
      @Override
    public Filter getFilter() {
          Filter    filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                  FilterResults   filterResults1=      new FilterResults();

                if (constraint.toString().length() >= 3 && searchType==1) {
                    Log.e("MY_DATA", constraint.toString());


                          
                         StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx/getds?method=All_Item_Master",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                    
                                        Log.e("Response1233", response);
                                        String res = response;
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            int success = jsonObject.getInt("success");
                                            log.e("success==" + success);
                                            if (success == 1) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                                //   DBItemUpload = new ArrayList<dbitemmaster>();
                                                     ArrayList<String> masterlist = new ArrayList<>();
                                                    books = new ArrayList<>();
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                                        String mItemID = loopObjects.getString("ItemID");
                                                        String mItemName = loopObjects.getString("ItemName");
                                                        String mcompanycosting = loopObjects.getString("ItemBarcode");// box barcode
                                                        String mByItemNumber = loopObjects.getString("ItemBarcode"); // item barcode
                                                        String mGroupID = loopObjects.getString("GroupID");
                                                        // String mRoundKG = loopObjects.getString("RoundKG");
                                                        // String mAddWeightKG = loopObjects.getString("AddWeightKG");
                                                        String HSNCode = loopObjects.getString("HSNCode");
                                                        String TaxRate = loopObjects.getString("StockUOM");
                                                        String Salerate = loopObjects.getString("SaleRate");
                                                        String MRP = loopObjects.getString("MRP");
                                                        String mAllString = mItemName;

                                                        dbitemmaster model = new dbitemmaster();
                                                        model.setmItemID(mItemID);
                                                        model.setmItemName(mItemName);
                                                        model.setmcompanycosting(mcompanycosting);
                                                        model.setmByItemNumber(mByItemNumber);
                                                        model.setmGroupID(mGroupID);
                                                        //     model.setmRoundKG(mRoundKG);
                                                        //   model.setmAddWeightKG(mAddWeightKG);
                                                        model.setmAllString(mAllString);
                                                        model.setMRP(MRP);
                                                        model.setSalerate(Salerate);
                                                        model.setHSNCode(HSNCode);
                                                        model.setTaxRate(TaxRate);
                                                        books.add(model);
                                                      //  masterlist.add(mItemName);
                                                    }


                                                if (books != null) {
                                                    FilterResults   filterResults=      new FilterResults();


                                                    filterResults.values = books;
                                                    filterResults.count = books.size();

                                                    if (filterResults != null && filterResults.count > 0) {
                                                        resultList = (List<dbitemmaster>) filterResults.values;
                                                        notifyDataSetChanged();
                                                    } else {
                                                        notifyDataSetInvalidated();
                                                    }

                                                    //               filterResults1=filterResults;
                                                }

                                                 //Message.message(mContext, "Data fetched Successfuly");
                                                //   checkBarcode("8906064651733", 2);
                                            }    
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                       
                                        String ss = "";
                                        //   Log.e("Error.Response", error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                Preferencehelper preferenceHelper=new Preferencehelper(mContext);
                                params.put("contactid", "9999" + strContactid);
                                params.put("SupplierID", "66738");
                                params.put("cat", "706");
                                params.put("filterItemName", constraint.toString());
                                //_web&=999966879&=&=&=8831234271902
                                Log.e("ppp", params.toString());
                                return params;
                            }
                        };
                        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                                60000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        Volley.newRequestQueue(mContext).add(postRequest);
                    
                   

                }
                return filterResults1;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
               /* if (results != null && results.count > 0) {
                    resultList = (List<GuideLinesBean>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }*/
            }
        };
        return filter;
    }

    //download mGuideLinesBean list
 }