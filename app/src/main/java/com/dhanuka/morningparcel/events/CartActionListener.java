package com.dhanuka.morningparcel.events;

 import com.dhanuka.morningparcel.beans.CartProduct;

import java.util.ArrayList;

public interface CartActionListener {
    void onMinusClick(ArrayList<CartProduct> cartList, int pos);

    void onAddClick(ArrayList<CartProduct> cartList, int pos);

    void onDeleteClick(ArrayList<CartProduct> cartList, int pos);

}
