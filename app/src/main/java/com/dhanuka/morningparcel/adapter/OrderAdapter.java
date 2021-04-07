package com.dhanuka.morningparcel.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhanuka.morningparcel.activity.InvoiceUpload;
import com.dhanuka.morningparcel.activity.NewOrderActivity;
import com.dhanuka.morningparcel.activity.OrderHistoryActivity;
import com.dhanuka.morningparcel.activity.Recurring_order;
import com.dhanuka.morningparcel.activity.ShopMapView;
import com.dhanuka.morningparcel.activity.Showallimages;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dhanuka.morningparcel.activity.OrderDetailActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.Clickevent;
import com.dhanuka.morningparcel.pdfsupport.EnglishNumberToWords;
import com.dhanuka.morningparcel.pdfsupport.ProductModel;
import com.dhanuka.morningparcel.pdfsupport.StaticValue;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.Utility;
import com.dhanuka.morningparcel.events.OrderClicklistner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<OrderBean> list;
    Preferencehelper prefs;
    Dialog CameraDialog;

    String getcatcodeid;
    Clickevent sendata;
    Dialog addmoneydialog;
    TextView addmoneybtn;
    EditText commenttxt, amounttxt;

    int mPOSI = 0;
    String isGR = "1";

    OrderClicklistner orderClicklistner;
    String currency = "";
    int mType = 0;
    float totalamount;

    int itemtotal = 0;
    String RequestedQty, RAmount, MRP, Rate, OrderID, CustomerPhone, ItemDescription, mPath,CustomerName, FlatNo, Building, society, comapnyname, Address, ContactNo, Email;


    public void setGR(String isGR) {
        this.isGR = isGR;
    }

    public OrderAdapter(Context context, List<OrderBean> list, OrderClicklistner mMapClick, Clickevent sendata, int mType) {
        this.context = context;
        this.mType = mType;
        this.list = list;
        this.sendata = sendata;
        this.orderClicklistner = mMapClick;
        prefs = new Preferencehelper(context);
        SharedPreferences prefs1 = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        try {
            prefs.setPrefsCurrency(list.get(mPOSI).getCurrency());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (list.get(mPOSI).getCurrency().equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

    }

    public void addItems(List<OrderBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<OrderBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        addmoneydialog = new Dialog(context);
        addmoneydialog.setContentView(R.layout.add_money_popup);
        addmoneybtn = addmoneydialog.findViewById(R.id.addamountbtn);
        viewHolder.txtcancellreason.setVisibility(View.GONE);
        commenttxt = addmoneydialog.findViewById(R.id.commenttxt);

        viewHolder.tatrecycler.setVisibility(View.GONE);

        viewHolder.showstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHolder.tatrecycler.getVisibility()==View.VISIBLE)
                {
                    viewHolder.tatrecycler.setVisibility(View.GONE);

                }
                else
                {
                    viewHolder.tatrecycler.setVisibility(View.VISIBLE);
                }

            }
        });

        if (mCategoryBean.getOrderType().equalsIgnoreCase("SO"))
        {

            viewHolder.showstat.setVisibility(View.VISIBLE);
        }
        else
        {

            viewHolder.showstat.setVisibility(View.GONE);
        }


        viewHolder.tatrecycler.setHasFixedSize(true);
        viewHolder.tatrecycler.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,true));
        viewHolder.tatrecycler.setAdapter(new SubTatAdapter(context,list.get(position).getTrackhistoryclasses()));
        viewHolder.createbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfcreatebill(list.get(position).getOrderID());
            }
        });


        amounttxt = addmoneydialog.findViewById(R.id.amounttxt);
        addmoneybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addMoneyToWallet(list.get(mPOSI).getOrderID(), list.get(mPOSI).getCustomerID());

            }
        });
        if (list.get(mPOSI).getCurrency().equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        String dat1 = "";

        if (mCategoryBean.getOrderType().equalsIgnoreCase("PO")) {

        }


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.ENGLISH);
            Date date1 = sdf.parse(mCategoryBean.getOrderDate());
            dat1 = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            dat1 = mCategoryBean.getOrderDate();
        }

//        if (mCategoryBean.getPaymentMode().toLowerCase().contains("cash")){
//            viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent_light));
//        }else{
//            viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.white));
//
//        }

        if (mCategoryBean.getOrderType().equalsIgnoreCase("SO") && (mCategoryBean.getPaymentMode().toLowerCase().equalsIgnoreCase("COD") || mCategoryBean.getPaymentMode().toLowerCase().equalsIgnoreCase("cash on delivery"))) {
            viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent_light));

        } else if (mCategoryBean.getOrderType().equalsIgnoreCase("PO")) {
            viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.color_green));


        } else {
            viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.white));
            if (mCategoryBean.getOrderType().equalsIgnoreCase("CN")) {
                viewHolder.txtcrn.setVisibility(View.VISIBLE);
                viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.red_light));


            } else {

                viewHolder.txtcrn.setVisibility(View.GONE);
            }

        }
        if (mCategoryBean.getDeliveryBoyName().isEmpty()) {
            viewHolder.txtDeliveryBoy.setVisibility(View.GONE);
        } else {
            viewHolder.txtDeliveryBoy.setVisibility(View.VISIBLE);
            viewHolder.txtDeliveryBoy.setText("Assigned To : " + mCategoryBean.getDeliveryBoyName());
        }
        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

            viewHolder.imgCustomer.setVisibility(View.GONE);


            if (mCategoryBean.getStatus().equalsIgnoreCase("10")) {
                viewHolder.btnReorder.setVisibility(View.GONE);

            } else {
                viewHolder.btnReorder.setVisibility(View.VISIBLE);

            }
            viewHolder.btnReorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    orderClicklistner.onRepeatOrder(mCategoryBean.getOrderID(), mCategoryBean);

                }
            });
            viewHolder.imgPayment.setVisibility(View.GONE);
            viewHolder.imgCamera.setVisibility(View.GONE);
            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
                viewHolder.imgDeliveryBoy.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgDeliveryBoy.setVisibility(View.GONE);

            }

            if (list.get(position).getOrderType().equalsIgnoreCase("RO")) {
                if (!mCategoryBean.getStatus().equalsIgnoreCase("91")) {
                    viewHolder.recuredit.setVisibility(View.GONE);
                } else {
                    viewHolder.recuredit.setVisibility(View.GONE);

                }
            } else {
                viewHolder.recuredit.setVisibility(View.GONE);

            }
            if (!list.get(position).getSupplierLat().isEmpty() && !list.get(position).getSupplierLong().isEmpty()) {
                viewHolder.maplocation.setVisibility(View.VISIBLE);

            } else {
                viewHolder.maplocation.setVisibility(View.GONE);
            }
        } else {

            if (list.get(position).getDeliveryBoyName() != null && !list.get(position).getDeliveryBoyName().equalsIgnoreCase("")) {
                viewHolder.txtdeliverboyname.setVisibility(View.VISIBLE);
                viewHolder.txtdeliverboyname.setText("Delivery Boy: " + list.get(position).getDeliveryBoyName());

            } else {
                viewHolder.txtdeliverboyname.setVisibility(View.GONE);

            }

            viewHolder.imgCustomer.setVisibility(View.VISIBLE);
            viewHolder.imgCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == 0) {
                        context.startActivity(new Intent(context, OrderHistoryActivity.class).putExtra("isCustomer", mCategoryBean.getCustomerID()));

                    } else {
                        context.startActivity(new Intent(context, NewOrderActivity.class).putExtra("isCustomer", mCategoryBean.getCustomerID()));


                    }
                }
            });
            if (!list.get(position).getDeliveryAddLat().isEmpty() && !list.get(position).getDeliveryAddLong().isEmpty()) {
                viewHolder.maplocation.setVisibility(View.VISIBLE);

            } else {
                viewHolder.maplocation.setVisibility(View.GONE);
            }
            viewHolder.imgCamera.setVisibility(View.VISIBLE);
            viewHolder.recuredit.setVisibility(View.GONE);
            viewHolder.imgPayment.setVisibility(View.GONE);
            viewHolder.btnReorder.setVisibility(View.GONE);
            if (mCategoryBean.getDeliveryBoyId().isEmpty()) {
                viewHolder.imgDeliveryBoy.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgDeliveryBoy.setVisibility(View.GONE);

            }
        }
        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
//            viewHolder.imgaddwallet.setVisibility(View.GONE);
            if (mCategoryBean.getPaymentMode().toLowerCase().equalsIgnoreCase("COD") || mCategoryBean.getPaymentMode().toLowerCase().equalsIgnoreCase("cash on delivery")) {

                viewHolder.changepayimg.setVisibility(View.VISIBLE);

            } else {
                viewHolder.changepayimg.setVisibility(View.GONE);
            }
        } else if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
            viewHolder.imgaddwallet.setVisibility(View.VISIBLE);
            viewHolder.changepayimg.setVisibility(View.VISIBLE);

        }


        viewHolder.imgaddwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPOSI = position;
                addmoneydialog.show();


            }
        });

        viewHolder.recuredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Recurring_order.class).putExtra("mData", list.get(position).getmListItems().get(0)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type", "2").putExtra("status", mCategoryBean.getStatus()).putExtra("orderId", mCategoryBean.getOrderID()));
            }
        });

        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mPOSI = position;

                loadHistory(mPOSI, 1);
            }
        });
        viewHolder.imgPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.opePaymentInfo(context, mCategoryBean.getCompanyID());
            }
        });
        viewHolder.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClicklistner.onPopOpen(position, mCategoryBean);
            }
        });
        viewHolder.imgDeliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClicklistner.onAssignOrder(position, mCategoryBean);
            }
        });
        viewHolder.edtinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, InvoiceUpload.class).putExtra("orderid", mCategoryBean.getOrderID()));
            }
        });
        viewHolder.orderimgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, Showallimages.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("id", mCategoryBean.getOrderID()); //3114
//                intent.putExtra("tble", "order_master");


                context.startActivity(new Intent(context, Showallimages.class).putExtra("tble", "order_master").putExtra("id", mCategoryBean.getOrderID()));

            }
        });

        CameraDialog = new Dialog(context);
        CameraDialog.setContentView(R.layout.popup_image_select);
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("orderidggg111", list.get(mPOSI).getOrderID() + "");
                orderClicklistner.onCameraClick("1", Integer.parseInt(list.get(mPOSI).getOrderID()));
                CameraDialog.dismiss();

            }
        });
        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("orderidggg111", list.get(mPOSI).getOrderID() + "");
                orderClicklistner.onCameraClick("2", Integer.parseInt(list.get(mPOSI).getOrderID()));
                CameraDialog.dismiss();


            }
        });

        viewHolder.imgclickpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPOSI = position;
                CameraDialog.show();

            }
        });

        if (mCategoryBean.getComment().isEmpty()) {
            viewHolder.txtComment.setVisibility(View.GONE);
            viewHolder.txtComment1.setVisibility(View.GONE);
        } else {
            viewHolder.txtComment.setVisibility(View.VISIBLE);
            viewHolder.txtComment.setText(mCategoryBean.getComment());
            viewHolder.txtComment1.setVisibility(View.VISIBLE);


        }
        viewHolder.maplocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                    context.startActivity(new Intent(context, ShopMapView.class).putExtra("mapbean", list.get(position)).putExtra("mmtype", "2").putExtra("subType", 111));
                } else {
                    context.startActivity(new Intent(context, ShopMapView.class).putExtra("mapbean", list.get(position)).putExtra("mmtype", "2").putExtra("subType", 222));

                }
            }
        });


        // viewHolder.txtSHop.setText("Shop : " + mCategoryBean.getSupplierName());
        try {
            if (mType == 0) {

                if (mCategoryBean.getOrderType().equalsIgnoreCase("PO")) {
                    viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent_light));

                }
                if (mCategoryBean.getOrderType().equalsIgnoreCase("STT")) {
                    viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.red_light));
                    viewHolder.txtcrn.setVisibility(View.VISIBLE);
                    viewHolder.txtcrn.setText("Stock Transfer");
                }

                if (mCategoryBean.getOrderType().equalsIgnoreCase("CN")) {
                    viewHolder.txtcrn.setVisibility(View.VISIBLE);
                    viewHolder.btnReorder.setVisibility(View.GONE);
                    viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.red_light));


                } else {

                    viewHolder.txtcrn.setVisibility(View.GONE);
                }


                viewHolder.txtSHop.setText(Html.fromHtml(context.getResources().getString(R.string.supplier_html1).replace("ASD", mCategoryBean.getSupplierName())));

            } else {
                viewHolder.txtSHop.setText(Html.fromHtml(context.getResources().getString(R.string.supplier_html).replace("ASD", mCategoryBean.getSupplierName())));


            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mType == 0) {
                viewHolder.txtSHop.setText("Supplier Name : " + mCategoryBean.getSupplierName());

            } else {
                viewHolder.txtSHop.setText("Shop : " + mCategoryBean.getSupplierName());

            }

        }
        viewHolder.txtId.setText("#00" + mCategoryBean.getOrderID());
        viewHolder.txtName.setText(mCategoryBean.getCustomerName());
        viewHolder.txtDate.setText(dat1);
        viewHolder.txtQty.setText(mCategoryBean.getRItemCOunt());
        if (isGR.equalsIgnoreCase("isGR")) {
            Double mTotal = 0.0;
            for (int i = 0; i < mCategoryBean.getmListItems().size(); i++) {
                // mTotal=mTotal+mCategoryBean.getmListItems().get(i).get
                try {
                    viewHolder.txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getRAmount())));
                } catch (Exception e) {
                    viewHolder.txtPrice.setText(currency + " 0.0");
                }
            }
        } else {
            try {
                viewHolder.txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getRAmount())));
            } catch (Exception e) {
                viewHolder.txtPrice.setText(currency + " 0.0");
            }
        }
        String mdt = mCategoryBean.getDevliveryDate();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(mCategoryBean.getDevliveryDate());
            mdt = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            mdt = mCategoryBean.getOrderDate();
        }

        try {
            if (mCategoryBean.getStatus().equalsIgnoreCase("83")) {
                viewHolder.txtStatus.setText("Out for delivery");
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.black));
                if (mCategoryBean.getDevliveryDate().isEmpty()) {
                    viewHolder.txtTagline.setText("Your Order will be delivered soon");

                } else {
                    viewHolder.txtTagline.setText("Delivery Time : " + mdt + " between " + mCategoryBean.getDeliveryTime());

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("0")) {
                viewHolder.txtStatus.setText("PROCESSING");
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.black));
                if (mCategoryBean.getDevliveryDate().isEmpty()) {
                    viewHolder.txtTagline.setText("Your Order will be delivered soon");

                } else {
                    viewHolder.txtTagline.setText("Delivery Time : " + mdt + " between " + mCategoryBean.getDeliveryTime());

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("91")) {
                viewHolder.txtStatus.setText("CANCELLED");
                viewHolder.imgCamera.setVisibility(View.GONE);


                if (!mCategoryBean.getCancelBy().equalsIgnoreCase("")) {
                    viewHolder.txtcancellreason.setVisibility(View.VISIBLE);
                    viewHolder.txtcancellreason.setText("Cancelled By :" + mCategoryBean.getCancelBy() + "\n" + "Cancelled Reason :" + mCategoryBean.getCancelReason() + "\n");

                } else {
                    viewHolder.txtcancellreason.setVisibility(View.GONE);

                }

                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.red));
                viewHolder.txtTagline.setText("Your Order has been Cancelled");
                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
                    viewHolder.imgDeliveryBoy.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.imgDeliveryBoy.setVisibility(View.GONE);

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("1")) {
                viewHolder.txtStatus.setText("ACCEPTED BY STORE");
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.black));

                if (mCategoryBean.getDevliveryDate().isEmpty()) {
                    viewHolder.txtTagline.setText("Your Order will be delivered soon");

                } else {
                    viewHolder.txtTagline.setText("Delivery Time : " + mdt + " between " + mCategoryBean.getDeliveryTime());

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("2")) {
                viewHolder.txtStatus.setText("ON HOLD");
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
                viewHolder.txtTagline.setText("Your Order is on hold will be delivered soon");
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("10")) {
                viewHolder.txtStatus.setText("DELIVERED");
                viewHolder.imgCamera.setVisibility(View.GONE);


                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txtTagline.setText("Your order has beed delivered ");
                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
                    viewHolder.imgDeliveryBoy.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.imgDeliveryBoy.setVisibility(View.GONE);

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("11")) {
                viewHolder.txtStatus.setText("ACCEPTED BY CUSTOMER");
                viewHolder.imgCamera.setVisibility(View.GONE);


                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txtTagline.setText("Your order has been Accepted by Customer ");
                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
                    viewHolder.imgDeliveryBoy.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.imgDeliveryBoy.setVisibility(View.GONE);

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("12")) {
                viewHolder.txtStatus.setText("ACCEPTED BY CUSTOMER");
                viewHolder.imgCamera.setVisibility(View.GONE);


                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txtTagline.setText("Your order has been Accepted by Customer ");
                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
                    viewHolder.imgDeliveryBoy.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.imgDeliveryBoy.setVisibility(View.GONE);

                }
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("81")) {
                viewHolder.txtStatus.setText("Packed");
                viewHolder.imgCamera.setVisibility(View.GONE);


                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txtTagline.setText("Your order has been Accepted by Packed ");
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("82")) {
                viewHolder.txtStatus.setText("Ready For Delivery");
                viewHolder.imgCamera.setVisibility(View.GONE);


                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txtTagline.setText("Your order is Ready For Delivery ");
            } else if (mCategoryBean.getStatus().equalsIgnoreCase("84")) {
                viewHolder.txtStatus.setText("Parcel Packed");
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txtTagline.setText("Your Order Parcel is Packed ");

            }

        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.txtStatus.setText("PROCESSING");
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.black));
            if (mCategoryBean.getDevliveryDate().isEmpty()) {
                viewHolder.txtTagline.setText("Your Order will be delivered soon");

            } else {
                viewHolder.txtTagline.setText("Delivery Time : " + mdt + " between " + mCategoryBean.getDeliveryTime());

            }
        }


        if (mType == 0) {
            viewHolder.txtTagline.setVisibility(View.GONE);
            viewHolder.orderstatuslinne.setVisibility(View.GONE);


        } else {
            viewHolder.orderstatuslinne.setVisibility(View.VISIBLE);
        }


        viewHolder.changepayimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.position = position;
                sendata.senddata(list.get(viewHolder.position), viewHolder.position);
            }
        });


        if (mCategoryBean.getOrderType().equalsIgnoreCase("CN")) {
            viewHolder.txtcrn.setVisibility(View.VISIBLE);
            viewHolder.mCardView.setBackgroundColor(context.getResources().getColor(R.color.red_light));


        } else {

            viewHolder.txtcrn.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtId)
        TextView txtId;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDeliveryBoy)
        TextView txtDeliveryBoy;
        @BindView(R.id.btnDetail)
        Button btnDetail;
        @BindView(R.id.btnReorder)
        Button btnReorder;
        @BindView(R.id.imgCustomer)
        ImageView imgCustomer;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtTagline)
        TextView txtTagline;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtQty)
        TextView txtQty;
        @BindView(R.id.txtDate)
        TextView txtDate;
        @BindView(R.id.txtSHop)
        TextView txtSHop;
        @BindView(R.id.txtComment)
        TextView txtComment;
        @BindView(R.id.txtComment1)
        TextView txtComment1;
        @BindView(R.id.imgPayment)
        ImageView imgPayment;
        int position;
        ImageView imgclickpick;
        @BindView(R.id.orderimgview)
        ImageView orderimgview;
        @BindView(R.id.edtinvoice)
        ImageView edtinvoice;
        @BindView(R.id.imgCamera)
        ImageView imgCamera;
        @BindView(R.id.imgDeliveryBoy)
        ImageView imgDeliveryBoy;
        @BindView(R.id.maplocation)
        ImageView maplocation;
        @BindView(R.id.mCardView)
        CardView mCardView;

        @BindView(R.id.recuredit)
        ImageView recuredit;
        @BindView(R.id.changepayimg)
        ImageView changepayimg;
        @BindView(R.id.orderstatuslinne)
        LinearLayout orderstatuslinne;
        @BindView(R.id.txtcrn)
        TextView txtcrn;

        @BindView(R.id.imgaddwallet)
        ImageView imgaddwallet;
        @Nullable
        @BindView(R.id.txtdeliverboyname)
        TextView txtdeliverboyname;
        @BindView(R.id.txtcancellreason)
        TextView txtcancellreason;

        @Nullable
        @BindView(R.id.createbill)
        ImageView createbill;

        @Nullable
        @BindView(R.id.tatdetail)
        RecyclerView tatrecycler;

        @Nullable
        @BindView(R.id.showstat)
        ImageView showstat;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgclickpick = itemView.findViewById(R.id.editupload);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.imageView2:

                    break;
                case R.id.tvName:

                    break;
            }

        }

    }

    public void addMoneyToWallet(String morderid, String mcustomerid) {


        final ProgressDialog mProgressBar = new ProgressDialog(context);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("please wait...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseSignup", response);
                        mProgressBar.dismiss();


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);
                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {

                                Toast.makeText(context, "Money added successfully", Toast.LENGTH_LONG).show();
                                addmoneydialog.dismiss();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Failed to add money to wallet , retry");
                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addMoneyToWallet(morderid, mcustomerid);
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.setCancelable(false);
                                dialog.show();


                            }


                        } catch (Exception e) {
                            //mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String servicetype;

                try {
                    SharedPreferences prefs1 = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                            context.MODE_PRIVATE);
                    String cntry = prefs1.getString("cntry", "India");
//method=CreateWalletTransaction_Web&Status=0&Type=1&=66280&=10&=bill1&=1&TransactionDate=08-11-2020
                    JKHelper jkHelper = new JKHelper();
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                    String mTodayDate = df.format(c.getTime());

                    String param = "method=CreateWalletTransaction&Status=0&Type=6&Contactid=" + mcustomerid + "&Amount=" + amounttxt.getText().toString() + "&TransactionDate=" + mTodayDate + "&BillNo=" + morderid + "&CreatedBy=" + prefs.getPrefsTempContactid() + "&Comment=" + commenttxt.getText().toString();
                    Log.e("beforeenc", param);
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

    ProgressDialog mProgressBar;

    public void pdfcreatebill(String morderid) {


        mProgressBar = new ProgressDialog(context);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Creating Pdf...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseSignup", response);
                        mProgressBar.dismiss();


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);

                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                JSONArray jsonarrya = jsonObject.getJSONArray("Headerds");

                                JSONArray jsonarrya1 = jsonObject.getJSONArray("Orderds");
                                for (int i = 0; i < jsonarrya.length(); i++) {
                                    JSONObject jsonObject1 = jsonarrya.getJSONObject(i);
                                    comapnyname = jsonObject1.getString("CompanyName");
                                    Address = jsonObject1.getString("Address");

                                    ContactNo = jsonObject1.getString("ContactNo");

                                    Email = jsonObject1.getString("Email");


                                }

                                for (int i = 0; i < jsonarrya1.length(); i++) {
                                    arrayListRProductModel = new ArrayList<ProductModel>();
                                    JSONObject jsonObject12 = jsonarrya1.getJSONObject(i);
                                    CustomerName = jsonObject12.getString("CustomerName");
                                    FlatNo = jsonObject12.getString("FlatNo");

                                    Building = jsonObject12.getString("Building");

                                    society = jsonObject12.getString("society");
                                    CustomerPhone = jsonObject12.getString("CustomerPhone");
                                    OrderID = jsonObject12.getString("OrderID");


                                    JSONArray Items = jsonObject12.getJSONArray("Items");
                                    itemtotal = Items.length();
                                    for (int m = 0; m < Items.length(); m++) {
                                        JSONObject jsonObject13 = Items.getJSONObject(m);

                                        ItemDescription = jsonObject13.getString("ItemDescription");
                                        Rate = jsonObject13.getString("Rate");

                                        MRP = jsonObject13.getString("MRP");
                                        RAmount = jsonObject13.getString("RAmount");
                                        RequestedQty = jsonObject13.getString("RequestedQty");
                                        totalamount = Float.parseFloat(MRP)*Float.parseFloat(RequestedQty) + totalamount;


                                        productModel = new ProductModel(ItemDescription, "", RequestedQty, MRP, Rate, Rate, "");
                                        arrayListRProductModel.add(productModel);


                                    }


                                }

                                createPDF(context, "Bill_" + OrderID);

                                File outputFile = new File(mPath);
                                Uri pdfUri;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", outputFile);
                                } else {
                                    pdfUri = Uri.fromFile(outputFile);
                                }

                                Intent share = new Intent();
                                share.setAction(Intent.ACTION_SEND);
                                share.setType("application/pdf");
                                share.putExtra(Intent.EXTRA_STREAM, pdfUri);
                                context.startActivity(Intent.createChooser(share, "Share"));

                            }


                        } catch (Exception e) {
                            //mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String servicetype;

                try {

                    JKHelper jkHelper = new JKHelper();
                    String param = context.getString(R.string.Companyheader) + "&ContactId=" + prefs.getPrefsContactId() + "&CompanyId=" + prefs.getCID() + "&OrderNo=" + morderid;
                    Log.e("beforeenc", param);
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


    private PdfWriter pdfWriter;

    //we will add some products to arrayListRProductModel to show in the PDF document
    private static ArrayList<ProductModel> arrayListRProductModel;
    ProductModel productModel;

    public boolean createPDF(Context context, String reportName) {

        try {
            //creating a directory in SD card


            File mydir = new File(Environment.getExternalStorageDirectory()
                    + "/SAFEOFRESH/BILLL_PRODUCT/"); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            //getting the full path of the PDF report name
             mPath = Environment.getExternalStorageDirectory().toString()
                    + "/SAFEOFRESH/BILLL_PRODUCT/"
                    + reportName + ".pdf"; //reportName could be any name

            //constructing the PDF file
            File pdfFile = new File(mPath);

            //Creating a Document with size A4. Document class is available at  com.itextpdf.text.Document
            Document document = new Document(PageSize.A4);

            //assigning a PdfWriter instance to pdfWriter
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            //PageFooter is an inner class of this class which is responsible to create Header and Footer
            PageHeaderFooter event = new PageHeaderFooter();
            pdfWriter.setPageEvent(event);

            //Before writing anything to a document it should be opened first
            document.open();

            //Adding meta-data to the document
            addMetaData(document);
            //Adding Title(s) of the document
            addTitlePage(document);
            //Adding main contents of the document
            addContent(document);


            //Closing the document
            document.close();


            mProgressBar.dismiss();
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    /**
     * iText allows to add metadata to the PDF which can be viewed in your Adobe Reader. If you right click
     * on the file and to to properties then you can see all these information.
     *
     * @param document
     */
    private void addMetaData(Document document) {
        document.addTitle("SafeoFresh");
        document.addSubject("none");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Safeofresh Market");
        document.addCreator("Jatin Sharma");
    }

    /**
     * In this method title(s) of the document is added.
     *
     * @param document
     * @throws DocumentException
     */
    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();

        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
        Paragraph childParagraph = new Paragraph(comapnyname, StaticValue.FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph("E-Mail:" + Email, StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        childParagraph.setSpacingBefore(20);
        paragraph.add(childParagraph);


        childParagraph = new Paragraph("Phone : " + ContactNo, StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph("Tax Invoice", StaticValue.FONT_SUBTITLE);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        document.add(paragraph);
        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setAlignment(Element.ALIGN_TOP);
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        document.add(new Chunk(lineSeparator));
        //End of adding several titles

    }

    /**
     * In this method the main contents of the documents are added
     *
     * @param document
     * @throws DocumentException
     */

    private void addContent(Document document) throws DocumentException {

        Paragraph reportBody = new Paragraph();
        Paragraph reportBodytwo = new Paragraph();

        reportBody.setFont(StaticValue.FONT_BODY); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);


        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Font regularHead = new Font(baseFont, 15, Font.BOLD, BaseColor.WHITE);
        Font regularReport = new Font(baseFont, 15, Font.BOLD);
        Font regularName = new Font(baseFont, 15, Font.BOLD, BaseColor.BLACK);
        Font regularAddress = new Font(baseFont, 15, Font.BOLD, BaseColor.BLACK);
        Font regularSub = new Font(baseFont, 15);
        Font regularTotal = new Font(baseFont, 15, Font.BOLD, BaseColor.BLACK);


        PdfPTable tableHeading = new PdfPTable(2);
        tableHeading.setSpacingBefore(2);
        tableHeading.setSpacingAfter(2);
        tableHeading.setWidthPercentage(95);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dfh = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c);
        String formattedtime = dfh.format(c);

        String theName = "", theAddress = "";

        theName = "Customer :" + CustomerName;
        theAddress = "Mobile :" + CustomerPhone;
        String thdate = "Date: " + formattedDate;
        String thebillno = "Bill No:" + OrderID;


        String thetime = "Time: " + formattedtime;
        String theusername = "User :" + CustomerName;

        PdfPCell preName = new PdfPCell(new Phrase(theName, regularName));
        PdfPCell preAddress = new PdfPCell(new Phrase(theAddress, regularAddress));
        PdfPCell preDate = new PdfPCell(new Phrase(thdate, regularAddress));
        PdfPCell preBill = new PdfPCell(new Phrase(thebillno, regularAddress));

        PdfPCell preuser = new PdfPCell(new Phrase(theusername, regularAddress));
        PdfPCell pretime = new PdfPCell(new Phrase(thetime, regularAddress));

        preBill.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preBill.setHorizontalAlignment(Element.ALIGN_RIGHT);


        pretime.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pretime.setHorizontalAlignment(Element.ALIGN_RIGHT);

        preDate.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preDate.setHorizontalAlignment(Element.ALIGN_RIGHT);


        preName.setBorder(Rectangle.NO_BORDER);
        preAddress.setBorder(Rectangle.NO_BORDER);
        preDate.setBorder(Rectangle.NO_BORDER);
        preuser.setBorder(Rectangle.NO_BORDER);
        pretime.setBorder(Rectangle.NO_BORDER);
        preBill.setBorder(Rectangle.NO_BORDER);

        try {
            tableHeading.addCell(preName);
            tableHeading.addCell(preBill);
            tableHeading.addCell(preAddress);
            tableHeading.addCell(preDate);
            tableHeading.addCell(preuser);
            tableHeading.addCell(pretime);
            document.add(tableHeading);
            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setAlignment(Element.ALIGN_TOP);
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            document.add(new Chunk(lineSeparator));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //   Creating a table
        createTable(reportBody);


        // now add all this to the document


        document.add(reportBody);


        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setAlignment(Element.ALIGN_TOP);
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 100));
        document.add(new Chunk(lineSeparator));

        botheader("Item: " + itemtotal, "Free Qty: 0", "Round Off:      " + "", "         " + "0", document);

        botheader("SubTotal:", totalamount + "0", "     " + "", "         " + "", document);

        botheader("CGST:", "        " + "0", "              " + "", "         " + "", document);

        botheader("SGST:", "        " + "0", "     " + "", "         " + "", document);

        botheader("IGST:", "        " + "0", "      " + "", "         " + "", document);

        botheadertwo("Cust GSTIN:", "        ", document);


        botheadertwo("Delivery Add:", FlatNo + Building + society + "", document);

        createTableTwo(reportBodytwo);
        document.add(reportBodytwo);

        BaseFont baseFontnu = null;
        try {
            baseFontnu = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font mOrderIdFont = new Font(baseFontnu, 15, Font.BOLD, BaseColor.BLACK);


        Paragraph newpara = new Paragraph("You Have saved on T.AMT Rs.", mOrderIdFont); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        newpara.setAlignment(Element.ALIGN_LEFT);
        newpara.setSpacingBefore(5);

        document.add(newpara);

        // LINE SEPARATOR
        LineSeparator lineSeparatorfinal = new LineSeparator();
        lineSeparatorfinal.setAlignment(Element.ALIGN_TOP);
        lineSeparatorfinal.setLineColor(new BaseColor(0, 0, 0, 68));
        document.add(new Chunk(lineSeparatorfinal));

        Font mOrderIdFontbold = new Font(baseFontnu, 15, Font.BOLD, BaseColor.BLACK);

        Paragraph newparam = new Paragraph(EnglishNumberToWords.convert((long) totalamount)+" Only", mOrderIdFontbold); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        newparam.setAlignment(Element.ALIGN_LEFT);
        newparam.setSpacingBefore(5);

        document.add(newparam);
        // LINE SEPARATOR
        LineSeparator lineSeparatorfinalm = new LineSeparator();
        lineSeparatorfinalm.setAlignment(Element.ALIGN_TOP);
        lineSeparatorfinalm.setLineColor(new BaseColor(0, 0, 0, 68));
        document.add(new Chunk(lineSeparatorfinalm));

        singlepara("TERMS & Conditions", document);
        singlepara("1.TOTAL SALE PRICE INCLUSIVE OF GST", document);
        singlepara("2.EXCHANGE WITH IN 7 DAYS OF PURCHASE", document);
        singlepara("3.PLEASE RETAIN THE BILL FOR EXCHANGE", document);
        singlepara("4.OUR CARRY BAGS ARE REFUNDABLE", document);
        singlepara("E.& O.E Safe'O'Kart Supermarket ", document);


    }

    public void singlepara(String basestr, Document document) {


        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Font mOrderIdFontbold = new Font(baseFont, 15, Font.BOLD, BaseColor.BLACK);

        Paragraph newparamagain = new Paragraph(basestr, mOrderIdFontbold); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        newparamagain.setAlignment(Element.ALIGN_LEFT);
        newparamagain.setSpacingBefore(5);

        try {
            document.add(newparamagain);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible to add table using iText
     *
     * @param reportBody
     * @throws BadElementException
     */
    private void createTable(Paragraph reportBody)
            throws BadElementException {

        float[] columnWidths = {3, 8, 4, 4, 4, 4, 4}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100); //set table with 100% (full page)
        table.getDefaultCell().setUseAscender(true);
        table.setSpacingBefore(10);


        //Adding table headers
        PdfPCell cell = new PdfPCell(new Phrase("SR.No", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        table.addCell(cell);

        //Adding table headers
        cell = new PdfPCell(new Phrase("Item Description", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("HSN CODE",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        // cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Qty",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("MRP",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Rate",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);


        //End of adding table headers

        //This method will generate some static data for the table


        //Adding data into table
        for (int i = 0; i < arrayListRProductModel.size(); i++) { //

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1)));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getName()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getQty()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getMrp()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getRate()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getAmount()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);
        }

        reportBody.add(table);


    }

    private void createTableTwo(Paragraph reportBody)
            throws BadElementException {

        float[] columnWidths = {4, 5, 4, 4, 4, 4}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
        PdfPTable tabletwo = new PdfPTable(columnWidths);
        tabletwo.setWidthPercentage(100); //set table with 100% (full page)
        tabletwo.getDefaultCell().setUseAscender(true);
        tabletwo.setSpacingBefore(30);


        //Adding table headers
        PdfPCell cell = new PdfPCell(new Phrase("Base Amount", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        tabletwo.addCell(cell);

        //Adding table headers
        cell = new PdfPCell(new Phrase("Description", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("CGST",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        // cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("SGST",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("IGST",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("Total Max",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);


        //End of adding table headers

        //This method will generate some static data for the table


        //Adding data into table
        for (int i = 0; i < 1; i++) { //

            cell = new PdfPCell(new Phrase(String.valueOf(totalamount)));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("GST 0.0%"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);


            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);


        }

        reportBody.add(tabletwo);


    }

    /**
     * This method is used to add empty lines in the document
     *
     * @param paragraph
     * @param number
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * This is an inner class which is used to create header and footer
     *
     * @author XYZ
     */

    class PageHeaderFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

        public void onEndPage(PdfWriter writer, Document document) {

            /**
             * PdfContentByte is an object containing the user positioned text and graphic contents
             * of a page. It knows how to apply the proper font encoding.
             */
            PdfContentByte cb = writer.getDirectContent();

            /**
             * In iText a Phrase is a series of Chunks.
             * A chunk is the smallest significant part of text that can be added to a document.
             *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
             */
            Phrase footer_poweredBy = new Phrase("Powered By SAFEOFRESH SUPERMARKET", StaticValue.FONT_HEADER_FOOTER); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
            Phrase footer_pageNumber = new Phrase("Page " + document.getPageNumber(), StaticValue.FONT_HEADER_FOOTER);

            // Header
            // ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
            // (document.getPageSize().getWidth()-10),
            // document.top() + 10, 0);

            // footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer_pageNumber,
                    (document.getPageSize().getWidth() - 10),
                    document.bottom() - 10, 0);
//			// footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer_poweredBy, (document.right() - document.left()) / 2
                            + document.leftMargin(), document.bottom() - 10, 0);
        }
    }

    /**
     * Generate static data for table
     */


    public void botheader(String first, String second, String third, String four, Document document) {

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        PdfPTable tablenew = new PdfPTable(5);
        tablenew.setWidthPercentage(95);
        tablenew.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
        PdfPCell cell3 = new PdfPCell(new Paragraph(""));
        PdfPCell cell4 = new PdfPCell(new Paragraph("Cell 4"));
        PdfPCell cell5 = new PdfPCell(new Paragraph("Cell 5"));

        Font boldfont = new Font(baseFont, 15, Font.BOLD, BaseColor.BLACK);


        cell1 = new PdfPCell(new Paragraph(first, boldfont));
        cell1.setBorder(0);
        cell2 = new PdfPCell(new Paragraph(second, boldfont));
        cell2.setBorder(0);
        cell4 = new PdfPCell(new Paragraph(third, boldfont));
        cell4.setBorder(0);
        cell5 = new PdfPCell(new Paragraph(four, boldfont));
        cell5.setBorder(0);
        cell3.setBorder(0);
        tablenew.addCell(cell1);
        tablenew.addCell(cell2);
        tablenew.addCell(cell3);
        tablenew.addCell(cell4);
        tablenew.addCell(cell5);

        try {
            document.add(tablenew);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void botheadertwo(String first, String second, Document document) {

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        float[] columnWidths = {2, 7};
        PdfPTable tablenew = new PdfPTable(columnWidths);
        tablenew.setWidthPercentage(95);
        tablenew.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));

        Font boldfont = new Font(baseFont, 15, Font.BOLD, BaseColor.BLACK);


        cell1 = new PdfPCell(new Paragraph(first, boldfont));
        cell1.setBorder(0);
        cell2 = new PdfPCell(new Paragraph(second, boldfont));
        cell2.setBorder(0);

        tablenew.addCell(cell1);
        tablenew.addCell(cell2);

        try {
            document.add(tablenew);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void loadHistory(int position, int ctype) {


        final ProgressDialog mProgressBar = new ProgressDialog(context);
        mProgressBar.setTitle("Safe'O'Kart");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String mTodayDate = df.format(c.getTime()) + " 23:59";
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTodayDate));

        Date newDate = calendar.getTime();
        mTodayDate = df.format(calendar.getTime());
        mTodayDate = mTodayDate;

        String finalMTodayDate = mTodayDate;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ArrayList<OrderBean> listOrders = new ArrayList<>();
                            list = new ArrayList<>();
                            ArrayList<OrderBean> list1 = new ArrayList<>();
                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                listOrders = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<OrderBean>>() {
                                }.getType());

                                Activity activity = (Activity) context;
                                mPOSI = position;

                                if (ctype == 2) {
                                    orderClicklistner.onRepeatOrder(listOrders.get(0).getOrderID(), listOrders.get(0));
                                } else {

                                    if (mType == 5) {

                                        Intent intent = new Intent(context, OrderDetailActivity.class);
                                        Bundle args = new Bundle();
                                        args.putSerializable("list", (Serializable) listOrders);
                                        args.putInt("mType", mType);
                                        args.putInt("mPos", mPOSI);
                                        // args.putSerializable("mListt",(Serializable) list);

                                        intent.putExtra("BUNDLE", args);
                                        ((Activity) context).startActivityForResult(intent, 122);

                                    }

                                    if (listOrders.get(0).getOrderType().equalsIgnoreCase("RO")) {
                                        if (listOrders.get(0).getReturnds().get(0).getmListItems().size() > 0) {
                                            context.startActivity(new Intent(context, Recurring_order.class).putExtra("mData", list.get(position).getmListItems().get(0)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type", "2").putExtra("status", listOrders.get(0).getStatus()).putExtra("orderId", listOrders.get(0).getOrderID()));
                                        }
                                    } else {

                                        DisplayMetrics metrics = new DisplayMetrics();
                                        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                        float yInches = metrics.heightPixels / metrics.ydpi;
                                        float xInches = metrics.widthPixels / metrics.xdpi;
                                        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
                                        if (diagonalInches >= 8.0) {
                                            // 8.0inch device or bigger
                                            sendata.senddata(listOrders.get(0), mPOSI);
                                        } else {
                                            // smaller device
                                            Intent intent = new Intent(context, OrderDetailActivity.class);
                                            Bundle args = new Bundle();
                                            args.putSerializable("list", (Serializable) listOrders.get(0));
                                            args.putInt("mType", mType);

                                            args.putInt("mPos", mPOSI);
                                            // args.putSerializable("mListt",(Serializable) list.get(position));

                                            intent.putExtra("BUNDLE", args);
                                            ((Activity) context).startActivityForResult(intent, 122);
                                        }

                                    }
                                }


                            } else {

                                FancyToast.makeText(context, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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


                        FancyToast.makeText(context, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1062")) {
//                        mType = "99";
//                    }

                    String param;

                    if(mType==0)
                    {
                        param = context.getString(R.string.GET_ORDER_DETAIL_GR_NEW) + "&contactid=" + prefs.getCID() + "&fdate=" + finalMTodayDate + "&tdate=" + finalMTodayDate + "&type=" + "99" + "&GrMasterId=" + list.get(position).getOrderID();

                    }
                    else
                    {
                        param = context.getString(R.string.GET_ORDER_DETAIL_SKART) + "&CID=" + prefs.getCID() + "&fdate=" + finalMTodayDate + "&tdate=" + finalMTodayDate + "&type=" + "99" + "&OrderNo=" + list.get(position).getOrderID();


                    }



                    Log.d("order_params", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, context);
                    params1.put("val", finalparam);
                    Log.d("afterencrptionhistory", finalparam);
                    return params1;


                } catch (Exception e) {
                    e.printStackTrace();
                    return params1;
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
