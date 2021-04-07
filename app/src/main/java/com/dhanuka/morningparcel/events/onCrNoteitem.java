package com.dhanuka.morningparcel.events;

import com.dhanuka.morningparcel.beans.OrderBean;

public interface onCrNoteitem {
    void onAddCart(OrderBean.OrderItemsBean mItemMasterhelper, int type);
}
