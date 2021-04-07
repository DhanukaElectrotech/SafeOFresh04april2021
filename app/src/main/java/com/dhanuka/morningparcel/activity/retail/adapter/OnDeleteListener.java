package com.dhanuka.morningparcel.activity.retail.adapter;


import com.dhanuka.morningparcel.activity.retail.beans.ItemMasterhelper;

public interface OnDeleteListener {
    void onItemDelete(int position);
    void onItemUpdate(int position, ItemMasterhelper itemMasterhelper);
}
