package com.dhanuka.morningparcel.restaurant.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
//import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import org.json.JSONObject;

import com.dhanuka.morningparcel.InterfacePackage.TriggerClick;
import com.dhanuka.morningparcel.activity.Message;
import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.activity.AddLatLong;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;
import com.dhanuka.morningparcel.utils.JKHelper;


public class SavedAdressadapter extends RecyclerView.Adapter {

    List<GeoFenceBean> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;
    TriggerClick selectAddress;
    int listsizecounter;

    public void filterList(ArrayList<GeoFenceBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public SavedAdressadapter(Context context, List<GeoFenceBean> objects, int listsizecounter, TriggerClick selectAddress) {

        this.context = context;
        this.selectAddress = selectAddress;
        list = objects;
        this.listsizecounter = listsizecounter;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_save_address, parent, false);
        categorybean holder = new categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GeoFenceBean adressact = list.get(position);

        categorybean viewHolder = (categorybean) holder;
        viewHolder.cityname.setText(adressact.getStriconname());
        try {
            String splithome[] = adressact.getStrdescription().split(":");
            viewHolder.completeaddress.setText(splithome[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.chooseadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddLatLong.class).putExtra("geofencebean", list.get(position)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type", "6"));

            }
        });
        viewHolder.frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddress.makeclick(list.get(position).getStrdescription(), list.get(position).getStrlat(), list.get(position).getStrlong());
                // context.startActivity(new Intent(context, CartActivity.class).putExtra("addrstr", list.get(position).getStrdescription()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure you want to delete this address?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();

                                creategeofence("3", list.get(position).getStrgeocodeid());

                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialogBuilder.create();
                alertDialogBuilder.show();   // selectAddress.makeclick(list.get(position).getStrdescription());
                // context.startActivity(new Intent(context, CartActivity.class).putExtra("addrstr", list.get(position).getStrdescription()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
        if (position == 0) {
            viewHolder.frame.setBackgroundResource(R.drawable.frame_color);
        } else {
            viewHolder.frame.setBackgroundResource(R.drawable.blank_frame);

        }


    }

    @Override
    public int getItemCount() {

       /* if (listsizecounter == 1) {
            return 1;

        } else {*/
        return list.size();
        //    }
    }

    class categorybean extends RecyclerView.ViewHolder {

        TextView cityname, completeaddress, chooseadd;
        LinearLayout frame;
        ImageView imgDelete;

        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cityname = itemView.findViewById(R.id.cityname);
            frame = itemView.findViewById(R.id.frame);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            chooseadd = itemView.findViewById(R.id.chooseadd);
            completeaddress = itemView.findViewById(R.id.completeaddress);

            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface boldfont = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Bold.ttf");

            cityname.setTypeface(boldfont);
            chooseadd.setTypeface(font);
            completeaddress.setTypeface(font);


        }


    }


    public void creategeofence(String type, String geofenceid) {

        ProgressDialog prgDialog;
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        prgDialog.dismiss();

                        prgDialog.dismiss();
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, context);
                        if (!responses.equalsIgnoreCase("")) {


                            try {
                                JSONObject jsonObject = new JSONObject(responses);
                                String successstr = jsonObject.getString("success");
                                if (successstr.equalsIgnoreCase("1")) {
                                    selectAddress.makeclick("", "", "");
                                    Toast.makeText(context, "address deleted successfully", Toast.LENGTH_LONG).show();

                                    prgDialog.dismiss();

                                } else {
                                    Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG).show();
                                    prgDialog.dismiss();
                                }

                            } catch (Exception e) {
                                prgDialog.dismiss();
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG).show();

                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(context, "Failed to Uploaded", Toast.LENGTH_LONG).show();
                            prgDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prgDialog.dismiss();
                        // error
                        Message.message(context, "Failed to Upload Status");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Preferencehelper prefs = new Preferencehelper(context);
//                params.put("GeofenceID", );
//                params.put("type", );
//                params.put("contactid", );
//                params.put("DeviceCode", );
//                params.put("Description", );
//                params.put("Lat", );
//                params.put("Long",  + "");
//                params.put("RadiousinKM", );
//                params.put("IconName", "office");
//                params.put("Status", "");
                try {

                    String param = context.getString(R.string.URL_CREATE_GEOFENCE) + "&GeofenceID=" + geofenceid + "&type=" + type + "&contactid=" + prefs.getPrefsContactId() +
                            "&DeviceCode=" + "0" + "&putdevicecode=0" + "&Description=" + "" + "&Lat=" + "" + "&RadiousinKM=" + "0"
                            + "&Long=" + "" + "&IconName=" + "" + "&Status=0";
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, context);
                    params.put("val", param);
                    Log.e("afterenc", param);
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

        Volley.newRequestQueue(context).add(postRequest);
    }

}