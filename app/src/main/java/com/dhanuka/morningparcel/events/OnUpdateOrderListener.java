package com.dhanuka.morningparcel.events;

import com.dhanuka.morningparcel.beans.OrderBean;

import java.util.List;

public interface OnUpdateOrderListener {

    void onCancelItem(OrderBean.OrderItemsBean beanData);
    void onUpdateOrder(List<OrderBean.OrderItemsBean> filteredList);
    void onPopupListener(int mPosition,OrderBean.OrderItemsBean mBeanItems);
    void Cancelclk(boolean cancelclk, OrderBean.OrderItemsBean orderItemsBean,int itemposition);
}
