package com.dhanuka.morningparcel.events;


import com.dhanuka.morningparcel.beans.OrderBean;

public interface OrderClicklistner {
    void onCameraClick(String type, int orderid);
    void onRepeatOrder(String orderId, OrderBean mBeanOrder);
    void onPopOpen(int mPosition, OrderBean mBeanOrder);
    void onAssignOrder(int mPosition, OrderBean mBeanOrder);

}
