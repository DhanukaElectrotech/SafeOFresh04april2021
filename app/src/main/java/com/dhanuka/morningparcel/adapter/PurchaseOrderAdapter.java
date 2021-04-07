package com.dhanuka.morningparcel.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dhanuka.morningparcel.Helper.PurchaseBean;
import com.dhanuka.morningparcel.activity.ItemStockActivity;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.Utility;


/**
 * Created by Mr.Mad on 4/5/2017.
 */

public class PurchaseOrderAdapter extends RecyclerView.Adapter {
    String currency = "";
    double dbDiscount = 0.0;
    boolean isSelectedAll = false;

    private Context context;
    private List<PurchaseBean> list;
    private List<PurchaseBean> filteredList;
    onAddCartListener monAddCartListener;

    public void filterList(ArrayList<PurchaseBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public void selectAll() {
        isSelectedAll = true;
       /* for (int a = 0; a < list.size(); a++) {
            list.get(a).setSetselected(true);
            list.get(a).setSetselected(true);
        }*/
        for (int a = 0; a < filteredList.size(); a++) {
            filteredList.get(a).setSetselected(true);
            filteredList.get(a).setSetselected(true);
        }
        notifyDataSetChanged();
    }

    public void unselectall() {
        isSelectedAll = false;
        for (int a = 0; a < filteredList.size(); a++) {
            filteredList.get(a).setSetselected(false);
            filteredList.get(a).setSetselected(false);
        }
        //   isSelectedAll = false;
        notifyDataSetChanged();
    }

    public PurchaseOrderAdapter() {
        selectAll();

    }

    public ArrayList<PurchaseBean> getSelected() {
        ArrayList<PurchaseBean> mListPurchase = new ArrayList<>();
        for (int a = 0; a < filteredList.size(); a++) {
            if (filteredList.get(a).isSetselected()) {
                mListPurchase.add(filteredList.get(a));
            }
        }

        return mListPurchase;
    }


    public PurchaseOrderAdapter(Context context, List<PurchaseBean> list, onAddCartListener mOnAddToCartListener, boolean isSelectedAll) {
        this.context = context;
        this.isSelectedAll = isSelectedAll;
        this.list = list;
        filteredList = new ArrayList<>();
        try {
            Log.e("mProducts_size", list.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        filteredList.addAll(this.list);
        this.monAddCartListener = mOnAddToCartListener;


        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);

        if (prefs.getString("discount", "0.0").isEmpty()) {
            dbDiscount = 0.0;

        } else {
            try {
                dbDiscount = Double.parseDouble(prefs.getString("discount", "0.0"));
            } catch (Exception e) {
                dbDiscount = 0.0;
            }
        }
        if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase_order, parent, false);
        OrderHistoryHolder holder = new OrderHistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        try {
            PurchaseBean product = filteredList.get(position);
            OrderHistoryHolder viewHolder = (OrderHistoryHolder) holder;
            viewHolder.setIsRecyclable(false);
            DecimalFormat precision = new DecimalFormat("0.00");
            DecimalFormat precision1 = new DecimalFormat("0");

            viewHolder.btnAddToCart.setOnCheckedChangeListener(null);
            if (product.isSetselected()) {
                viewHolder.btnAddToCart.setChecked(true);
            } else {
                viewHolder.btnAddToCart.setChecked(false);
            }


            try {
                viewHolder.txtInQty.setText(/*"In Qty. - "+*/new DecimalFormat("##").format(Double.parseDouble(product.getInQty())) );
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.txtInQty.setText(/*"In Qty. - "+*/product.getInQty());

            }
            try {
                viewHolder.txtOutQty.setText(/*"Out Qty. - "+*/ new DecimalFormat("##").format(Double.parseDouble(product.getOutQty())));
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.txtOutQty.setText(/*"Out Qty. - "+*/product.getOutQty());

            }
            viewHolder.tvName.setText(product.getItemName());
            viewHolder.branchName.setText(product.getBranchName());
            viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ItemStockActivity.class).putExtra("itemId", list.get(position).getItemid()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()));
                    // context.startActivity(new Intent(context, ItemHistoryActivity.class).putExtra("mData", list.get(position).getItemid()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()));

                }
            });
            viewHolder.btnAddToCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    product.setSetselected(isChecked);
                    product.setSetchoose("1");
                    // list.get(position).setSetselected(isChecked);
                    filteredList.get(position).setSetselected(isChecked);
                    notifyDataSetChanged();
                }
            });
            viewHolder.groupnameid.setText(product.getGroupname());
            viewHolder.purchaseweekid.setText(precision1.format(Double.parseDouble(String.valueOf(product.getTotalPurchase1()))));
            viewHolder.lastweeksaleid.setText(precision1.format(Double.parseDouble(String.valueOf(product.getTotalSale1()))));
            viewHolder.marginpid.setText(precision1.format(Double.parseDouble(String.valueOf(product.getMarginP()))) + " %");

            try {
                viewHolder.tvOriginalPrice2.setText(product.getCompanycosting());
            } catch (Exception e) {
                viewHolder.tvOriginalPrice2.setText(product.getCompanycosting());

            }
            viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        filteredList.get(position).setCurrentstock((int) (Double.parseDouble(product.getCurrentstock()) + 1) + "");

                    } catch (Exception e) {
                        e.printStackTrace();
                        filteredList.get(position).setCurrentstock("1");
                        // viewHolder.tvQty.setText((Integer.parseInt(product.getCurrentstock()) + 1) + "");
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.btnReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if ((int) (Double.parseDouble(product.getBalance())) > 0) {
                            filteredList.get(position).setCurrentstock((int) (Double.parseDouble(product.getCurrentstock()) - 1) + "");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        filteredList.get(position).setCurrentstock("0");
                        // viewHolder.tvQty.setText((Integer.parseInt(product.getCurrentstock()) + 1) + "");
                    }
                    notifyDataSetChanged();

                }
            });
            try {
                viewHolder.tvQty.setText((int) (Double.parseDouble(product.getCurrentstock())) + "");
                if ((int) (Double.parseDouble(product.getBalance())) < 5) {
                    viewHolder.cstockktxt.setText((int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockheader.setText(/*"Current Stock - "+*/(int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockktxt.setTextColor(context.getResources().getColor(R.color.red));


                } else {
                    viewHolder.cstockktxt.setText((int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockheader.setText(/*"Current Stock - "+*/(int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockktxt.setTextColor(context.getResources().getColor(R.color.colorAccent));


                }

            } catch (Exception e) {
                viewHolder.tvQty.setText(product.getCurrentstock() + "");
                if ((int) (Double.parseDouble(product.getBalance())) < 5) {
                    viewHolder.cstockktxt.setText((int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockheader.setText(/*"Current Stock - "+*/(int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockktxt.setTextColor(context.getResources().getColor(R.color.red));


                } else {
                    viewHolder.cstockktxt.setText((int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockheader.setText(/*"Current Stock - "+*/(int) (Double.parseDouble(product.getBalance())) + "");
                    viewHolder.cstockktxt.setTextColor(context.getResources().getColor(R.color.colorAccent));


                }

            }
//            if (!product.getItemImage().isEmpty()) {
//                Log.e("MIMGGG", product.getItemImage());
//            }
            Glide.with(context)
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(viewHolder.ivProduct);

            viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.Dialog_Confirmation(context, "http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath());
                }
            });


//        if (dbDiscount > 0) {
            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                    context.MODE_PRIVATE);

            if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {
                currency = context.getResources().getString(R.string.rupee);
            } else {
                currency = "$";
            }


            if (product.getMrp().isEmpty()) {
                double res;
                double amount = Double.parseDouble(product.getMrp());
                Float finalprice = Float.parseFloat(product.getMrp()) - Float.parseFloat(product.getCompanycosting());
                if (finalprice > 0) {
                    res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMrp());

                } else {

                    res = Double.parseDouble(String.valueOf("0"));
                }


                if (Float.parseFloat(String.valueOf(product.getMrp())) < Float.parseFloat(String.valueOf(product.getCompanycosting()))) {
                    viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##").format(Double.parseDouble(String.valueOf(product.getMrp()))));

                    viewHolder.tvSave.setText("");
                    viewHolder.tvOff.setText("");
                    viewHolder.tvOff.setVisibility(View.GONE);
                    viewHolder.tvOriginalPrice1.setText("");

                } else {


                    viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##").format(Double.parseDouble(String.valueOf(product.getMrp()))));
                    viewHolder.tvOriginalPrice.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.tvOriginalPrice1.setText(currency + " " + precision.format(Double.parseDouble(String.valueOf(product.getCompanycosting()))));
                   // viewHolder.tvOff.setVisibility(View.VISIBLE);
                    viewHolder.tvOff.setVisibility(View.GONE);
                    viewHolder.tvSave.setText("Save " + currency + new DecimalFormat("##").format(Double.parseDouble(String.valueOf(finalprice))));
                    viewHolder.tvOff.setText(new DecimalFormat("##").format(res) + "% OFF");

                }
            } else {
                double res;
                double amount = Double.parseDouble(product.getMrp());
                Float finalprice = Float.parseFloat(product.getMrp()) - Float.parseFloat(product.getCompanycosting());
                if (finalprice > 0) {
                    res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMrp());

                } else {

                    res = Double.parseDouble(String.valueOf("0"));
                }
                if (Float.parseFloat(String.valueOf(product.getMrp())) < Float.parseFloat(String.valueOf(product.getCompanycosting()))) {

                    viewHolder.tvSave.setText("");
                    viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##").format(Double.parseDouble(String.valueOf(product.getMrp()))));
                    viewHolder.tvOff.setText("");
                    viewHolder.tvOff.setVisibility(View.GONE);
                    viewHolder.tvOriginalPrice1.setText("");

                } else {

                    viewHolder.tvOriginalPrice.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##").format(Double.parseDouble(String.valueOf(product.getMrp()))));
                    viewHolder.tvOriginalPrice1.setText(currency + " " + precision.format(Double.parseDouble(String.valueOf(product.getCompanycosting()))));
                 //  viewHolder.tvOff.setVisibility(View.VISIBLE);
                    viewHolder.tvOff.setVisibility(View.GONE);
                    viewHolder.tvSave.setText("Save " + currency + new DecimalFormat("##").format(Double.parseDouble(String.valueOf(finalprice))));
                    viewHolder.tvOff.setText(new DecimalFormat("##").format(res) + "% OFF");

                }

            }


//        else {
//             viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
//             viewHolder.tvOff.setVisibility(View.GONE);
//             viewHolder.tvOriginalPrice.setText(currency + " " + precision.format(Double.parseDouble(product.getSaleRate())));
//
//        }
//            if (Integer.parseInt(product.getQuantity()) > 0) {
//                viewHolder.btnAddToCart.setVisibility(View.GONE);
//                viewHolder.tvQty.setVisibility(View.VISIBLE);
//                viewHolder.btnAdd.setVisibility(View.VISIBLE);
//                viewHolder.btnReduce.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.btnAddToCart.setVisibility(View.VISIBLE);
//                viewHolder.tvQty.setVisibility(View.GONE);
//                viewHolder.btnAdd.setVisibility(View.GONE);
//                viewHolder.btnReduce.setVisibility(View.GONE);
//            }
//            viewHolder.tvQty.setText(product.getQuantity());
//         viewHolder.tvOriginalPrice1.setVisibility(View.GONE);


            viewHolder.position = position;
            Log.e("positionposition", position + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProduct;
        TextView tvName;
        TextView tvSave;
        TextView tvOriginalPrice1;
        TextView tvOriginalPrice2;
        TextView txtInQty;
        TextView txtOutQty;
        TextView tvOriginalPrice;
        TextView tvOff;
        TextView branchName;
        TextView tvQty, purchaseweekid;
        TextView groupnameid, lastweeksaleid, marginpid, cstockheader,cstockktxt;

        //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
        CheckBox btnAddToCart;
        Button btnAdd;
        Button btnReduce;

        int position;

        public OrderHistoryHolder(View view) {
            super(view);
            ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
            tvName = (TextView) view.findViewById(R.id.tvName);
            branchName = (TextView) view.findViewById(R.id.branchName);
            cstockheader = (TextView) view.findViewById(R.id.cstockheader);
            groupnameid = (TextView) view.findViewById(R.id.groupnameid);
            lastweeksaleid = (TextView) view.findViewById(R.id.lastweeksaleid);
            marginpid = (TextView) view.findViewById(R.id.marginpid);
            purchaseweekid = (TextView) view.findViewById(R.id.purchaseweekid);
            tvSave = (TextView) view.findViewById(R.id.tvSave);
            tvOriginalPrice1 = (TextView) view.findViewById(R.id.tvOriginalPrice1);
            tvOriginalPrice2 = (TextView) view.findViewById(R.id.tvOriginalPrice2);
            tvOriginalPrice = (TextView) view.findViewById(R.id.tvOriginalPrice);
            tvOff = (TextView) view.findViewById(R.id.tvOff);
            txtInQty = (TextView) view.findViewById(R.id.txtInQty);
            txtOutQty = (TextView) view.findViewById(R.id.txtOutQty);
            tvQty = (TextView) view.findViewById(R.id.tvQty);
            cstockktxt = (TextView) view.findViewById(R.id.cstockktxt);

            //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
            btnAddToCart = view.findViewById(R.id.btnAddToCart);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            btnReduce = (Button) view.findViewById(R.id.btnReduce);


        }

        @Override
        public void onClick(View view) {
        }
    }

    public void filter(String text) {
        text = text.toLowerCase();
        filteredList.clear();
        if (text.length() == 0) {
            filteredList.addAll(list);
        } else {
            for (PurchaseBean product : list) {
                if (product.getItemName().toLowerCase().contains(text.toLowerCase()) || product.getItembarcode().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
