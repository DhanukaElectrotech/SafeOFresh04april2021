package com.dhanuka.morningparcel.InterfacePackage;

import com.dhanuka.morningparcel.adapter.DeliveryHelper;
import com.dhanuka.morningparcel.adapter.Payhelper;

public interface CartItemsadd {
    public void  sendtimeslotval(String res,String delverrate);
    public void  sendpayval(Payhelper res);
    public void  senddeliveryval(DeliveryHelper res);
}
