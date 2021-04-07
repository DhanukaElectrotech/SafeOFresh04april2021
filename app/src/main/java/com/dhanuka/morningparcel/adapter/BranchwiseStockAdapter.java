package com.dhanuka.morningparcel.adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.BranchwiseHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.CategoryActivity;
import com.dhanuka.morningparcel.activity.ItemHistoryActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.events.OnProductEditListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.Utility;

public class BranchwiseStockAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<BranchwiseHelper> list;
    String type;
    OnAddToSToreListener onAddToSToreListener;
    OnProductEditListener mOnProductEditListener;
    int mPos = 0;
    String currency;
    Preferencehelper prefs;
    int mType = 0;
    SharedPreferences prefss;
    SharedPreferences.Editor mEditor;

    public BranchwiseStockAdapter(Context context, List<BranchwiseHelper> list, String type, OnAddToSToreListener onAddToSToreListener, OnProductEditListener mOnProductEditListener, int mType) {
        this.mType = mType;
        this.context = context;
        this.onAddToSToreListener = onAddToSToreListener;
        this.mOnProductEditListener = mOnProductEditListener;
        this.type = type;
        this.list = list;
        prefs = new Preferencehelper(context);
        prefss = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);

        mEditor = prefss.edit();
    }

    public void addItems(List<BranchwiseHelper> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void changeToEdit(int intType) {
        mType = intType;
        notifyDataSetChanged();
    }

    public void clearAll(int intType) {
        mType = intType;
        for (int a = 0; a < list.size(); a++) {
            list.get(a).setChecked(false);
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_branchwise, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BranchwiseHelper mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvName.setText(mCategoryBean.getItemName());
        viewHolder.tvOriginalPrice2.setText(mCategoryBean.getGroupID());
        viewHolder.txtBranchName.setText(mCategoryBean.getBranchName());

        viewHolder.tvQty.setText("Qty. : " + mCategoryBean.getAvailableQty());

        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        try {
            viewHolder.tvOriginalPrice.setText(/*context.getResources().getString(R.string.rupee) +*/ currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mCategoryBean.getSaleRate()))));
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.tvOriginalPrice.setText(/*context.getResources().getString(R.string.rupee) +*/ currency + mCategoryBean.getSaleRate());
        }
        viewHolder.tvBarcode.setText(mCategoryBean.getItemBarcode());
//          viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        if (!list.get(position).getFileName().isEmpty()) {
            Log.e("MIMGGG", "http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath());
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context)
                .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(viewHolder.ivProduct);
        viewHolder.position = position;
        viewHolder.btnDetail.setVisibility(View.VISIBLE);
        type="pending";
        if (type.equalsIgnoreCase("All")) {
            viewHolder.btnUpdate.setVisibility(View.GONE);
//            if (!list.get(position).getStoreSTatus().equalsIgnoreCase("In Store")) {
//
//
//            } else {
//                viewHolder.btnDetail.setVisibility(View.GONE);
//
//            }

        } else if (type.equalsIgnoreCase("Pending")) {
            viewHolder.btnUpdate.setVisibility(View.GONE);
            viewHolder.cbIsNew.setVisibility(View.GONE);
            viewHolder.llName.setVisibility(View.GONE);
            viewHolder.cbIsDeal.setVisibility(View.GONE);
            viewHolder.deletebtn.setVisibility(View.GONE);
            viewHolder.btnDetail.setVisibility(View.VISIBLE);
//            if (!list.get(position).getStoreSTatus().equalsIgnoreCase("In Store")) {
//                viewHolder.btnDetail.setVisibility(View.VISIBLE);
//
//            } else {
//                viewHolder.btnDetail.setVisibility(View.GONE);
//
//            }
        } else {
            viewHolder.cbIsNew.setVisibility(View.VISIBLE);
            viewHolder.llName.setVisibility(View.VISIBLE);
            viewHolder.cbIsDeal.setVisibility(View.VISIBLE);
            viewHolder.btnUpdate.setVisibility(View.VISIBLE);
            viewHolder.deletebtn.setVisibility(View.VISIBLE);
            viewHolder.btnDetail.setVisibility(View.VISIBLE);
            try {
                if (mCategoryBean.getIsDeal().equalsIgnoreCase("1")) {
                    viewHolder.cbIsDeal.setChecked(true);

                } else {
                    viewHolder.cbIsDeal.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (mCategoryBean.getIsImage().equalsIgnoreCase("1")) {
                    viewHolder.cbImage.setChecked(true);

                } else {
                    viewHolder.cbImage.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (mCategoryBean.getIsname().equalsIgnoreCase("1")) {
                    viewHolder.cbName.setChecked(true);

                } else {
                    viewHolder.cbName.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (mCategoryBean.getIsNewListing().equalsIgnoreCase("1")) {
                    viewHolder.cbIsNew.setChecked(true);

                } else {
                    viewHolder.cbIsNew.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewHolder.cbIsNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMsg = "";
                    if (viewHolder.cbIsNew.isChecked()) {
                        strMsg = "Are you sure you want to remove from New Listing?";
                    } else {
                        strMsg = "Are you sure you want to add to New Listing?";

                    }

                    new AlertDialog.Builder(context)


                            .setTitle("Safe'O'Fresh")

                            .setMessage(strMsg)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (viewHolder.cbIsNew.isChecked()) {
                                        list.get(position).setIsNewListing("1");
                                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "6C");
                                    } else {
                                        list.get(position).setIsNewListing("0");
                                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "6D");
                                    }
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();


                }
            });

            viewHolder.cbIsDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMsg = "";

                    if (viewHolder.cbIsDeal.isChecked()) {
                        strMsg = "Are you sure you want to remove from Deals of the day?";
                    } else {
                        strMsg = "Are you sure you want to add to Deals of the day?";

                    }

                    new AlertDialog.Builder(context)


                            .setTitle("Safe'O'Fresh")

                            .setMessage(strMsg)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (viewHolder.cbIsDeal.isChecked()) {
                                        list.get(position).setIsDeal("1");
                                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "5A");
                                    } else {
                                        list.get(position).setIsDeal("0");
                                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "5B");
                                    }

                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();

                }
            });
            viewHolder.cbImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMsg = "";

                    if (viewHolder.cbImage.isChecked()) {
                        strMsg = "Are you sure you want to remove from Is Image?";
                    } else {
                        strMsg = "Are you sure you want to add to to Is Image?";

                    }

                /*    new AlertDialog.Builder(context)


                            .setTitle("Safe'O'Fresh")

                            .setMessage(strMsg)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
*/
                    if (viewHolder.cbImage.isChecked()) {
                        list.get(position).setIsImage("1");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "9A");
                    } else {
                        list.get(position).setIsImage("0");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "9B");
                    }

                    notifyDataSetChanged();
                         /*           //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();*/

                }
            });

    /*        viewHolder.cbimagell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMsg = "";

                    if (viewHolder.cbImage.isChecked()) {
                        strMsg = "Are you sure you want to remove from Is Image?";
                    } else {
                        strMsg = "Are you sure you want to add to to Is Image?";

                    }

                *//*    new AlertDialog.Builder(context)


                            .setTitle("Safe'O'Fresh")

                            .setMessage(strMsg)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
*//*
                    if (viewHolder.cbImage.isChecked()) {
                        list.get(position).setIsImage("1");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "9A");
                    } else {
                        list.get(position).setIsImage("0");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "9B");
                    }

                    notifyDataSetChanged();
                         *//*           //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();*//*

                }
            });
   */
            viewHolder.cbName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMsg = "";

                    if (viewHolder.cbName.isChecked()) {
                        strMsg = "Are you sure you want to remove from Is name?";
                    } else {
                        strMsg = "Are you sure you want to add to to Is Name?";

                    }
/*

                    new AlertDialog.Builder(context)


                            .setTitle("Safe'O'Fresh")

                            .setMessage(strMsg)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
*/

                    if (viewHolder.cbName.isChecked()) {
                        list.get(position).setIsname("1");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "10A");
                    } else {
                        list.get(position).setIsname("0");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "10B");
                    }

                    notifyDataSetChanged();
                    //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                         /*       }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();*/

                }
            });
   /*         viewHolder.cbnamell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strMsg = "";

                    if (viewHolder.cbName.isChecked()) {
                        strMsg = "Are you sure you want to remove from Is name?";
                    } else {
                        strMsg = "Are you sure you want to add to to Is Name?";

                    }
*//*

                    new AlertDialog.Builder(context)


                            .setTitle("Safe'O'Fresh")

                            .setMessage(strMsg)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
*//*

                    if (viewHolder.cbImage.isChecked()) {
                        list.get(position).setIsname("1");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "10A");
                    } else {
                        list.get(position).setIsname("0");
                        updateProduct(context, list.get(position).getItemID(), list.get(position).getAvailableQty(), list.get(position).getToShow(), list.get(position).getSaleRate(), "10B");
                    }

                    notifyDataSetChanged();
                    //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                         *//*       }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                    //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();*//*

                }
            });
*/
            if (mCategoryBean.getToShow().equalsIgnoreCase("0") && !mCategoryBean.getToShow().isEmpty()) {
                viewHolder.btnDetail.setText("Hide");
                viewHolder.btnDetail.setBackground(context.getResources().getDrawable(R.drawable.round_btn_neww));
            } else {
                viewHolder.btnDetail.setText("Show");
                viewHolder.btnDetail.setBackground(context.getResources().getDrawable(R.drawable.green_back));

            }
        }
        viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.Dialog_Confirmation(context, "http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath());
            }
        });

        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mClickType", "Update11");
                String qty = list.get(position).getAvailableQty();
                String rate = list.get(position).getSaleRate();
                if (rate.isEmpty()) {
                    rate = "0";
                }
                if (qty.isEmpty()) {
                    qty = "0";
                }
                if (viewHolder.btnDetail.getText().toString().equalsIgnoreCase("Hide")) {
                    list.get(position).setToShow("1");
                    list.get(position).setStoreSTatus("In Store");
                    updateProduct(context, list.get(position).getItemID(), qty, "1", rate, "2");
                } else if (viewHolder.btnDetail.getText().toString().equalsIgnoreCase("Show")) {
                    list.get(position).setToShow("0");
                    updateProduct(context, list.get(position).getItemID(), qty, "0", rate, "2");
                } else {
                    onAddToSToreListener.onAddToSTore(list.get(position).getItemID());
                    updateProduct(context, list.get(position).getItemID(), qty, "0", rate, "1");
                    //ItemID=2627&VendorID=66270&Createdby=66270&AvailableQty=3&ToShow=0&SaleRate=50&SaleUOM=&SaleUOMID=0&Type=1
                    list.remove(position);
                }


            }
        });

        viewHolder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)


                        .setTitle("Safe'O'Fresh")

                        .setMessage("Are you sure you want to delete?")

                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String qty = list.get(position).getAvailableQty();
                                String rate = list.get(position).getSaleRate();
                                if (rate.isEmpty()) {
                                    rate = "0";
                                }
                                if (qty.isEmpty()) {
                                    qty = "0";
                                }
                                mPos = position;
                                dialog.dismiss();
                                String mId = list.get(position).getItemID();
                                list.remove(position);
                                updateProduct(context, mId, qty, "0", rate, "4");
                                //  Toast.makeText(MainActivity.this, "You Clicked on Yes", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //  Toast.makeText(MainActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .show();
            }
        });

        if (mCategoryBean.getChecked()) {
            viewHolder.checkedLayout.setBackgroundColor(context.getResources().getColor(R.color.selected));
        } else {
            viewHolder.checkedLayout.setBackgroundColor(context.getResources().getColor(R.color.un_selected));
        }
        if (mType == 1) {
            viewHolder.checkedLayout.setVisibility(View.VISIBLE);
            viewHolder.checkedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCategoryBean.getChecked()) {
                        list.get(position).setChecked(false);

                    } else {
                        list.get(position).setChecked(true);


                    }
                    notifyDataSetChanged();
                }
            });

        } else {
            viewHolder.checkedLayout.setVisibility(View.GONE);

        }

             viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getMRP())));

//        if (!list.get(position).getSaleRate().isEmpty() && !list.get(position).getMRP().isEmpty()) {
//            if (Float.parseFloat(list.get(position).getSaleRate()) < Float.parseFloat(list.get(position).getMRP())) {
//                viewHolder.tvOff.setVisibility(View.VISIBLE);
//                viewHolder.tvOriginalPrice1.setVisibility(View.VISIBLE);
//                Float finalprice = Float.parseFloat(list.get(position).getMRP()) - Float.parseFloat(list.get(position).getSaleRate());
//
//
//                viewHolder.tvOriginalPrice1.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                viewHolder.tvOriginalPrice1.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getMRP())));
//                Float res = ((finalprice * 100.0f)) / Float.parseFloat(list.get(position).getMRP());
//                viewHolder.tvOff.setText(" " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(res))) + "% OFF");
//                viewHolder.tvSave.setText("Save " + currency + "" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))) + "");
//
//            } else {
//                viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
//                viewHolder.tvOff.setVisibility(View.GONE);
//                viewHolder.tvBarcode.setVisibility(View.GONE);
//                viewHolder.tvSave.setVisibility(View.GONE);
//
//            }
//        } else {
//            viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
//            viewHolder.tvOff.setVisibility(View.GONE);
//            viewHolder.tvBarcode.setVisibility(View.GONE);
//            viewHolder.tvSave.setVisibility(View.GONE);
//
//        }
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("clicked_data", list.get(position).getItemID());
//                mOnProductEditListener.onProductEdit(list.get(position), position);

            }
        });

        viewHolder.branchlayout.setVisibility(View.VISIBLE);

        viewHolder.stockLayout.setVisibility(View.VISIBLE);



        try {
            if (mCategoryBean.getTotalPurchase().isEmpty()) {
                viewHolder.txtTotalPurchase.setText("0");

            } else {
                viewHolder.txtTotalPurchase.setText(mCategoryBean.getTotalPurchase());

            }
        } catch (Exception e) {
            viewHolder.txtTotalPurchase.setText("0");

        }
        try {
            if (mCategoryBean.getInQty().isEmpty()) {
                viewHolder.txtInQty.setText("0");

            } else {
                viewHolder.txtInQty.setText(mCategoryBean.getInQty());
            }
        } catch (Exception e) {
            viewHolder.txtInQty.setText("0");

        }
        try {
            if (mCategoryBean.getTotalSale().isEmpty()) {
                viewHolder.txtTotalSale.setText("0");

            } else {
                viewHolder.txtTotalSale.setText(mCategoryBean.getTotalSale());
            }
        } catch (Exception e) {
            viewHolder.txtTotalSale.setText("0");

        }
        try {
            if (mCategoryBean.getOutQty().isEmpty()) {
                viewHolder.txtOutQty.setText("0");

            } else {
                viewHolder.txtOutQty.setText(mCategoryBean.getOutQty());
            }
        } catch (Exception e) {
            viewHolder.txtOutQty.setText("0");

        }
        try {
            if (mCategoryBean.getBalance().isEmpty()) {
                viewHolder.txtBalance.setText("0");

            } else {
                viewHolder.txtBalance.setText(mCategoryBean.getBalance());
            }
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.txtBalance.setText("0");

        }
        viewHolder.btnDetail.setVisibility(View.GONE);
        viewHolder.btnUpdate.setVisibility(View.GONE);
//        if (prefss.getString("isIntent", "0").equalsIgnoreCase("1")) {
//
//
//        } else {
//            viewHolder.stockLayout.setVisibility(View.GONE);
//        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvQty)
        TextView tvQty;
        @BindView(R.id.checkedLayout)
        RelativeLayout checkedLayout;
        @BindView(R.id.btnDetail)
        TextView btnDetail;
        @BindView(R.id.btnUpdate)
        Button btnUpdate;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvOriginalPrice)
        TextView tvOriginalPrice;
        @BindView(R.id.tvOriginalPrice2)
        TextView tvOriginalPrice2;
        @BindView(R.id.ivProduct)
        ImageView ivProduct;
        @BindView(R.id.deletebtn)
        ImageView deletebtn;
        @BindView(R.id.cbIsNew)
        CheckBox cbIsNew;
        @BindView(R.id.cbIsDeal)
        CheckBox cbIsDeal;
        @BindView(R.id.cbName)
        CheckBox cbName;
        @BindView(R.id.cbImage)
        CheckBox cbImage;
        @BindView(R.id.llName)
        LinearLayout llName;
        @BindView(R.id.cbimagell)
        LinearLayout cbimagell;
        @BindView(R.id.cbnamell)
        LinearLayout cbnamell;
        int position;
        @BindView(R.id.tvOff)
        TextView tvOff;

        @BindView(R.id.tvSave)
        TextView tvSave;

        @BindView(R.id.tvOriginalPrice1)
        TextView tvOriginalPrice1;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @BindView(R.id.btnHistory)
        ImageView btnHistory;
        @BindView(R.id.stockLayout)
        LinearLayout stockLayout;
        @BindView(R.id.branchlayout)
        LinearLayout branchlayout;


        @BindView(R.id.txtTotalPurchase)
        TextView txtTotalPurchase;
        @BindView(R.id.txtBranchName)
        TextView txtBranchName;
        @BindView(R.id.txtOutQty)
        TextView txtOutQty;
        @BindView(R.id.txtBalance)
        TextView txtBalance;
        @BindView(R.id.txtTotalSale)
        TextView txtTotalSale;
        @BindView(R.id.txtInQty)
        TextView txtInQty;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnHistory.setOnClickListener(this);
            tvName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.btnHistory:
                    if (prefss.getString("isIntent", "0").equalsIgnoreCase("1")) {
                        mEditor.putString("branchname", list.get(getAdapterPosition()).getBranchName());
                        mEditor.commit();
                        context.startActivity(new Intent(context, ItemHistoryActivity.class).putExtra("mData", list.get(getAdapterPosition()).getItemID()).putExtra("image", list.get(getAdapterPosition()).getFileName() + "&filePath=" + list.get(getAdapterPosition()).getFilepath()).putExtra("branchData", "1").putExtra("mtype","2"));

                    } else {

                        context.startActivity(new Intent(context, ItemHistoryActivity.class).putExtra("mData", list.get(getAdapterPosition()).getItemID()).putExtra("image", list.get(getAdapterPosition()).getFileName() + "&filePath=" + list.get(getAdapterPosition()).getFilepath()).putExtra("mtype","2"));
                    }
                    break;
                case R.id.imageView2:
                    Log.e("listlist", list.size() + "");
                    Intent intent = new Intent(context, CategoryActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("list", (Serializable) list);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("mPosition", position);
                    context.startActivity(intent);

                    break;
                case R.id.tvName:
                    if (prefss.getString("isIntent", "0").equalsIgnoreCase("1")) {
                        mEditor.putString("branchname", list.get(getAdapterPosition()).getBranchName());
                        mEditor.commit();
                        context.startActivity(new Intent(context, ItemHistoryActivity.class).putExtra("mData", list.get(getAdapterPosition()).getItemID()).putExtra("image", list.get(getAdapterPosition()).getFileName() + "&filePath=" + list.get(getAdapterPosition()).getFilepath()).putExtra("branchData", "1").putExtra("mtype","2"));

                    } else {

                        context.startActivity(new Intent(context, ItemHistoryActivity.class).putExtra("mData", list.get(getAdapterPosition()).getItemID()).putExtra("image", list.get(getAdapterPosition()).getFileName() + "&filePath=" + list.get(getAdapterPosition()).getFilepath()).putExtra("mtype","2"));
                    }
                    break;
            }

        }
    }

    String strType;
    String ToShow1;

    public void updateProduct(Context ctx, String ItemID, String AvailableQty, String ToShow, String SaleRate, String Type) {

        ToShow1 = ToShow;
        strType = Type;
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                if (type.equalsIgnoreCase("4")) {
                                }
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
                    mSaleRate = (int) (Double.parseDouble(SaleRate)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mSaleRate = SaleRate;
                }
                String mQty = "";
                try {
                    mQty = (int) (Double.parseDouble(AvailableQty)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mQty = AvailableQty;
                }

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    int as = 0;

                    if (Type.equalsIgnoreCase("10A")) {
                        strType = Type.replace("A", "");
                        ToShow1 = "1";
                        as = 1;
                    } else if (Type.equalsIgnoreCase("10B")) {
                        strType = Type.replace("B", "");
                        ToShow1 = "0";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("9A")) {
                        strType = Type.replace("A", "");
                        ToShow1 = "1";
                        as = 1;
                    } else if (Type.equalsIgnoreCase("9B")) {
                        strType = Type.replace("B", "");
                        ToShow1 = "0";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("5A")) {
                        strType = Type.replace("A", "");
                        ToShow1 = "1";
                        as = 1;
                    } else if (Type.equalsIgnoreCase("5B")) {
                        strType = Type.replace("B", "");
                        ToShow1 = "0";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("6C")) {
                        strType = Type.replace("C", "");
                        ToShow1 = "1";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("6D")) {
                        strType = Type.replace("D", "");
                        ToShow1 = "0";
                        as = 1;

                    } else {
                        ToShow1 = ToShow;
                        strType = Type;
                        as = 0;
                    }

                    try {
                        if (mQty.isEmpty()) {
                            mQty = "0";
                        }
                    } catch (Exception e) {
                        mQty = "0";
                        e.printStackTrace();
                    }
                    if (as == 1) {
                        strType = strType + "&isdeal=" + ToShow1;
                    } else {
                        strType = strType;
                    }
                    String param = ctx.getString(R.string.CREATE_ORDER_ITEMS) + "&ItemID=" + ItemID + "&VendorID=" + prefs.getPrefsContactId() + "&Createdby=" + prefs.getPrefsContactId() + "&AvailableQty=" + mQty
                            + "&ToShow=" + ToShow1 + "&SaleRate=" + mSaleRate + "&Type=" + strType + "&SaleUOMID=" + "0" + "&SaleUOM=" + "0";
                    Log.d("Beforeencrptionadd", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrptionadd", finalparam);
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

    public void filterList(ArrayList<BranchwiseHelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public List<BranchwiseHelper> getChckedProducts() {
        List<BranchwiseHelper> mList = new ArrayList<>();
        for (int a = 0; a < list.size(); a++) {
            if (list.get(a).getChecked()) {
                mList.add(list.get(a));
            }
        }
        return mList;
    }

}
