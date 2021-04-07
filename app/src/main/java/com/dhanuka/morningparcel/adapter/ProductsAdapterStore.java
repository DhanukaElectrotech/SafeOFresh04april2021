package com.dhanuka.morningparcel.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.AddCartEvent;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.Utility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * `1`
 */
public class ProductsAdapterStore extends ArrayAdapter<ItemMasterhelper> {

    List<ItemMasterhelper> list, filteredList;
    LayoutInflater inflater;
    onAddCartListener monAddCartListener;
String stype="Pending";
    public ProductsAdapterStore(Context ctx, List<ItemMasterhelper> objects, onAddCartListener monAddCartListener, String type) {
        super(ctx, R.layout.item_product, objects);
        this.monAddCartListener = monAddCartListener;
        list = objects;
        stype = type;
        inflater = LayoutInflater.from(getContext());
        filteredList = new ArrayList<>();
        filteredList.addAll(list);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddCart(AddCartEvent event) {
        ItemMasterhelper product = event.getProduct();
    }
int mPosition=0;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ItemMasterhelper product = filteredList.get(mPosition);
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_store_products, parent, false);
        } else {
            view = convertView;
        }
        mPosition=position;



        ItemMasterhelper mCategoryBean = list.get(mPosition);

         TextView tvQty= (TextView) view.findViewById(R.id.tvQty);
         TextView btnDetail= (TextView) view.findViewById(R.id.btnDetail);
         Button btnUpdate= (Button) view.findViewById(R.id.btnUpdate);
         TextView tvName= (TextView) view.findViewById(R.id.tvName);
         TextView tvOriginalPrice= (TextView) view.findViewById(R.id.tvOriginalPrice);
         TextView tvOriginalPrice2= (TextView) view.findViewById(R.id.tvOriginalPrice2);
         ImageView ivProduct= (ImageView) view.findViewById(R.id.ivProduct);


        tvName.setText(mCategoryBean.getItemName());
        tvOriginalPrice2.setText(mCategoryBean.getGroupID());
        tvQty.setText("Qty. : " + mCategoryBean.getAvailableQty());
        tvOriginalPrice.setText(getContext().getResources().getString(R.string.rupee) + " " + mCategoryBean.getSaleRate());


        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(getContext())
                .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(mPosition).getFileName() + "&filePath=" + mCategoryBean.getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(ivProduct);
        position = position;

        if (stype.equalsIgnoreCase("All")) {
            btnUpdate.setVisibility(View.GONE);
            if (!list.get(mPosition).getStoreSTatus().equalsIgnoreCase("In Store")) {
                btnDetail.setVisibility(View.VISIBLE);

            } else {
                btnDetail.setVisibility(View.GONE);

            }

        } else if (stype.equalsIgnoreCase("Pending")) {
            btnUpdate.setVisibility(View.GONE);
            btnDetail.setVisibility(View.VISIBLE);
            if (!list.get(mPosition).getStoreSTatus().equalsIgnoreCase("In Store")) {
                btnDetail.setVisibility(View.VISIBLE);

            } else {
                btnDetail.setVisibility(View.GONE);

            }
        } else {
            btnUpdate.setVisibility(View.VISIBLE);
            btnDetail.setVisibility(View.VISIBLE);
            if (mCategoryBean.getToShow().equalsIgnoreCase("0") && !mCategoryBean.getToShow().isEmpty()) {
                btnDetail.setText("Show");
            } else {
                btnDetail.setText("Hide");

            }
        }

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.Dialog_Confirmation(getContext(), "http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(mPosition).getFileName() + "&filePath=" + mCategoryBean.getFilepath());
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mClickType", "Update11");
                String qty = list.get(mPosition).getAvailableQty();
                String rate = list.get(mPosition).getSaleRate();
                if (rate.isEmpty()) {
                    rate = "0";
                }
                if (qty.isEmpty()) {
                    qty = "0";
                }
                if (btnDetail.getText().toString().equalsIgnoreCase("Hide")) {
                    list.get(mPosition).setToShow("1");
                    list.get(mPosition).setStoreSTatus("In Store");
                    updateProduct(getContext(), list.get(mPosition).getItemID(), qty, "1", rate, "2");
                } else if (btnDetail.getText().toString().equalsIgnoreCase("Show")) {
                    list.get(mPosition).setToShow("0");
                    list.get(mPosition).setStoreSTatus("In Store");
                    updateProduct(getContext(), list.get(mPosition).getItemID(), qty, "0", rate, "2");
                } else {
                  //  list.get(mPosition).setStoreSTatus("In Store");
                    updateProduct(getContext(), list.get(mPosition).getItemID(), qty, "0", rate, "1");
                    //ItemID=2627&VendorID=66270&Createdby=66270&AvailableQty=3&ToShow=0&SaleRate=50&SaleUOM=&SaleUOMID=0&Type=1
                    list.remove(mPosition);

                }


            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_edit_product);

                final EditText tvQty = (EditText) dialog.findViewById(R.id.tvQty);
                final EditText rg = (EditText) dialog.findViewById(R.id.edtPrice);
                final ImageView ivProduct = (ImageView) dialog.findViewById(R.id.ivProduct);
                final TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
                final TextView tvOriginalPrice = (TextView) dialog.findViewById(R.id.tvOriginalPrice);
                rg.setText(list.get(mPosition).getSaleRate());
                rg.requestFocus();
                rg.setFocusable(true);
                tvName.setText(list.get(mPosition).getItemName());
                if (!list.get(mPosition).getAvailableQty().isEmpty()) {
                    tvQty.setText(list.get(mPosition).getAvailableQty());
                } else {
                    tvQty.setText("0");
                }
                Glide.with(getContext())
                        .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(mPosition).getFileName() + "&filePath=" + list.get(mPosition).getFilepath())
                        .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                        .into(ivProduct);


                tvOriginalPrice.setText(getContext().getString(R.string.rupee) + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(mPosition).getSaleRate())));


                //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
                final Button btnAddToCart = (Button) dialog.findViewById(R.id.btnAddToCart);


                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Log.e("mClickType", "Update");
                        String status = rg.getText().toString();
                        if (status.isEmpty()) {
                            FancyToast.makeText(getContext(), "Please enter amount.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                        } else {
                            status=new DecimalFormat("##.##").format(Double.parseDouble(status))+"";
                            updateProduct(getContext(), list.get(mPosition).getItemID(), tvQty.getText().toString(), "0", status, "3");

                            dialog.dismiss();
                        }

                    }
                });


                // Display the custom alert dialog on interface
                dialog.show();


            }
        });
        
        
      


        //view.setContent(product);
        return view;
    }

    public void filter(String text) {
        text = text.toLowerCase();
        filteredList.clear();
        if (text.length() == 0) {
            filteredList.addAll(list);
        } else {
            for (ItemMasterhelper product : list) {
                if (product.getItemBarcode().toLowerCase().contains(text) || product.getItemName().toLowerCase().contains(text) || product.getHSNCode().toLowerCase().contains(text) || product.getItemSKU().toLowerCase().contains(text)) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }




    public void updateProduct(Context ctx, String ItemID, String AvailableQty, String ToShow, String SaleRate, String Type) {


        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST,ctx.getString(R.string.URL_BASE_URL) ,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                                notifyDataSetChanged();

                            } else {

                                FancyToast.makeText(ctx, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(ctx, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(ctx);
                Map<String, String> params = new HashMap<String, String>();
                 String mSaleRate = "";
                try {
                    mSaleRate = (int)(Double.parseDouble(SaleRate)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mSaleRate = SaleRate;
                }
                String mQty = "";
                try {
                    mQty = (int)(Double.parseDouble(AvailableQty)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mQty = AvailableQty;
                }

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = ctx.getString(R.string.CREATE_ORDER_ITEMS)+"&ItemID=" +ItemID + "&VendorID=" + prefs.getPrefsContactId()+"&Createdby="+prefs.getPrefsContactId()+"&AvailableQty="+mQty
                           +"&ToShow="+ToShow+"&SaleRate="+ToShow+"&SaleRate="+mSaleRate+"&Type="+Type+"&SaleUOMID="+"0"+"&SaleUOM="+"0" ;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
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

        Volley.newRequestQueue(ctx).add(postRequest);


    }


}
