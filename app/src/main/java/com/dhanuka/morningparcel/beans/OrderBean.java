package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderBean implements Serializable {
    @SerializedName("RItemCOunt")
    String RItemCOunt;

    @SerializedName("returnds")
    ArrayList<OrderBean> returnds;




    @SerializedName("TrackHistory")
    ArrayList<Trackhistoryclass> trackhistoryclasses;



    @SerializedName("OrderType")
    String OrderType;
    @SerializedName("DeliveryBoyId")
    String DeliveryBoyId;
    @SerializedName("DeliveryBoyName")
    String DeliveryBoyName;
    @SerializedName("SupplierRating")
    String SupplierRating;
    @SerializedName("Comment")
    String Comment;
    @SerializedName("CustomerRating")
    String CustomerRating;
    @SerializedName("RAmount")
    String RAmount;
    @SerializedName("OrderID")
    String OrderID;
    @SerializedName("OrderDate")
    String OrderDate;
    @SerializedName("Status")
    String status;
    @SerializedName("CustomerID")
    String CustomerID;
    @SerializedName("CustomerName")
    String CustomerName;
    @SerializedName("FlatNo")
    String FlatNo;
    @SerializedName("Building")
    String Building;
    @SerializedName("society")
    String society;
    @SerializedName("CompanyID")
    String CompanyID;
    @SerializedName("SupplierName")
    String SupplierName;
    @SerializedName("CustomerPhone")
    String CustomerPhone;
    @SerializedName("CustomerEmail")
    String CustomerEmail;
    @SerializedName("CustomerLat")
    String CustomerLat;
    @SerializedName("CustomerLong")
    String CustomerLong;
    @SerializedName("SupplierPhone")
    String SupplierPhone;
    @SerializedName("SupplierLat")
    String SupplierLat;
  @SerializedName("PreviousAdjustedAmount")
    String PreviousAdjustedAmount;

    @SerializedName("ServiceCharge")
    String ServiceCharge;

    @SerializedName("Tax")
    String Tax;
    @SerializedName("DeliveryAddLong")
    String DeliveryAddLong;



    @SerializedName("DeliveryAddLat")
    String DeliveryAddLat;

    public String getPreviousAdjustedAmount() {
        return PreviousAdjustedAmount;
    }

    public void setPreviousAdjustedAmount(String previousAdjustedAmount) {
        PreviousAdjustedAmount = previousAdjustedAmount;
    }

    public String getDeliveryBoyId() {
        return DeliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        DeliveryBoyId = deliveryBoyId;
    }

    public String getDeliveryBoyName() {
        return DeliveryBoyName;
    }

    public void setDeliveryBoyName(String deliveryBoyName) {
        DeliveryBoyName = deliveryBoyName;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    @SerializedName("DeliveryCharge")
    String DeliveryCharge;


    public String getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    @SerializedName("SupplierLong")
    String SupplierLong;
    @SerializedName("DeliveryMode")
    String DeliveryMode;
    @SerializedName("DevliveryDate")
    String DevliveryDate;
    @SerializedName("DeliveryTime")
    String DeliveryTime;
    @SerializedName("PaymentMode")
    String PaymentMode;
    @SerializedName("PaymentTxnId")
    String PaymentTxnId;
    @SerializedName("Currency")
    String Currency;

    @SerializedName("CancelBy")
    String CancelBy;

    @SerializedName("CancelReason")
    String CancelReason;
    @SerializedName("Items")
    ArrayList<OrderItemsBean> mListItems;




    public String getDeliveryMode() {
        return DeliveryMode;
    }

    public String getPaymentTxnId() {
        return PaymentTxnId;
    }

    public void setPaymentTxnId(String paymentTxnId) {
        PaymentTxnId = paymentTxnId;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        DeliveryMode = deliveryMode;
    }

    public String getDevliveryDate() {
        return DevliveryDate;
    }

    public void setDevliveryDate(String devliveryDate) {
        DevliveryDate = devliveryDate;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }



    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getSupplierRating() {
        return SupplierRating;
    }

    public void setSupplierRating(String supplierRating) {
        SupplierRating = supplierRating;
    }

    public String getCustomerRating() {
        return CustomerRating;
    }

    public void setCustomerRating(String customerRating) {
        CustomerRating = customerRating;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getFlatNo() {
        return FlatNo;
    }

    public void setFlatNo(String flatNo) {
        FlatNo = flatNo;
    }

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getCustomerLat() {
        return CustomerLat;
    }

    public void setCustomerLat(String customerLat) {
        CustomerLat = customerLat;
    }

    public String getCustomerLong() {
        return CustomerLong;
    }

    public void setCustomerLong(String customerLong) {
        CustomerLong = customerLong;
    }

    public String getSupplierPhone() {
        return SupplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        SupplierPhone = supplierPhone;
    }

    public String getSupplierLat() {
        return SupplierLat;
    }

    public void setSupplierLat(String supplierLat) {
        SupplierLat = supplierLat;
    }

    public String getSupplierLong() {
        return SupplierLong;
    }

    public void setSupplierLong(String supplierLong) {
        SupplierLong = supplierLong;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRItemCOunt() {
        return RItemCOunt;
    }

    public void setRItemCOunt(String RItemCOunt) {
        this.RItemCOunt = RItemCOunt;
    }

    public String getRAmount() {
        return RAmount;
    }

    public void setRAmount(String RAmount) {
        this.RAmount = RAmount;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public ArrayList<OrderItemsBean> getmListItems() {
        return mListItems;
    }

    public void setmListItems(ArrayList<OrderItemsBean> mListItems) {
        this.mListItems = mListItems;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getDeliveryAddLong() {
        return DeliveryAddLong;
    }

    public void setDeliveryAddLong(String deliveryAddLong) {
        DeliveryAddLong = deliveryAddLong;
    }

    public String getDeliveryAddLat() {
        return DeliveryAddLat;
    }

    public void setDeliveryAddLat(String deliveryAddLat) {
        DeliveryAddLat = deliveryAddLat;
    }

    public ArrayList<OrderBean> getReturnds() {
        return returnds;
    }

    public void setReturnds(ArrayList<OrderBean> returnds) {
        this.returnds = returnds;
    }

    public String getCancelBy() {
        return CancelBy;
    }

    public void setCancelBy(String cancelBy) {
        CancelBy = cancelBy;
    }

    public String getCancelReason() {
        return CancelReason;
    }

    public void setCancelReason(String cancelReason) {
        CancelReason = cancelReason;
    }

    public ArrayList<Trackhistoryclass> getTrackhistoryclasses() {
        return trackhistoryclasses;
    }

    public void setTrackhistoryclasses(ArrayList<Trackhistoryclass> trackhistoryclasses) {
        this.trackhistoryclasses = trackhistoryclasses;
    }

    //Items
    public class OrderItemsBean implements Serializable {

        @SerializedName("OrderID")
        String OrderID;
        @SerializedName("OrderType")
        String OrderType;
        @SerializedName("OrderDate")
        String OrderDate;
        @SerializedName("InvoiceNumber")
        String InvoiceNumber;
        @SerializedName("CompanyID")
        String CompanyID;
        @SerializedName("CustomerID")
        String CustomerID;
        @SerializedName("RItemCOunt")
        String RItemCOunt;
        @SerializedName("OrderStatus")
        String OrderStatus;
        @SerializedName("OrderRequestedAmount")
        String OrderRequestedAmount;
        @SerializedName("OrderDItemCount")
        String OrderDItemCount;
        @SerializedName("OrderDAmount")
        String OrderDAmount;
        @SerializedName("OrderdetailID")
        String OrderdetailID;
        @SerializedName("ItemID")
        String ItemID;
        @SerializedName("ItemDescription")
        String ItemDescription;
        @SerializedName("RequestedQty")
        String RequestedQty;
       @SerializedName("Quantity")
        int Quantity;
        @SerializedName("UOM")
        String UOM;
        @SerializedName("Rate")
        String Rate;

        @SerializedName("ItemBarcode")
        String ItemBarcode;

        @SerializedName("BarCode")
        String BarCode;


        @SerializedName("RAmount")
        String RAmount;
        @SerializedName("DQty")
        String DQty;
        @SerializedName("DetailDAmount")
        String DetailDAmount;
        @SerializedName("Discount")
        String Discount;
        @SerializedName("Status")
        String Status;
        @SerializedName("FileName")
        String FileName;
        @SerializedName("filepath")
        String filepath;
        @SerializedName("MRP")
        String MRP;
        @SerializedName("val1")
        String val1;

        @SerializedName("val2")
        String val2;

        @SerializedName("val3")
        String val3;




        Boolean isChecked=false,isreplace=false;

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String barCode) {
            BarCode = barCode;
        }

        public String getMRP() {
            return MRP;
        }

        public void setMRP(String MRP) {
            this.MRP = MRP;
        }

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String orderID) {
            OrderID = orderID;
        }

        public String getOrderType() {
            return OrderType;
        }

        public void setOrderType(String orderType) {
            OrderType = orderType;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String orderDate) {
            OrderDate = orderDate;
        }

        public String getInvoiceNumber() {
            return InvoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            InvoiceNumber = invoiceNumber;
        }

        public String getCompanyID() {
            return CompanyID;
        }

        public void setCompanyID(String companyID) {
            CompanyID = companyID;
        }

        public String getCustomerID() {
            return CustomerID;
        }

        public void setCustomerID(String customerID) {
            CustomerID = customerID;
        }

        public String getRItemCOunt() {
            return RItemCOunt;
        }

        public void setRItemCOunt(String RItemCOunt) {
            this.RItemCOunt = RItemCOunt;
        }

        public String getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            OrderStatus = orderStatus;
        }

        public String getOrderRequestedAmount() {
            return OrderRequestedAmount;
        }

        public void setOrderRequestedAmount(String orderRequestedAmount) {
            OrderRequestedAmount = orderRequestedAmount;
        }

        public String getOrderDItemCount() {
            return OrderDItemCount;
        }

        public void setOrderDItemCount(String orderDItemCount) {
            OrderDItemCount = orderDItemCount;
        }

        public String getOrderDAmount() {
            return OrderDAmount;
        }

        public void setOrderDAmount(String orderDAmount) {
            OrderDAmount = orderDAmount;
        }

        public String getOrderdetailID() {
            return OrderdetailID;
        }

        public void setOrderdetailID(String orderdetailID) {
            OrderdetailID = orderdetailID;
        }

        public String getItemID() {
            return ItemID;
        }

        public void setItemID(String itemID) {
            ItemID = itemID;
        }

        public String getItemDescription() {
            return ItemDescription;
        }

        public void setItemDescription(String itemDescription) {
            ItemDescription = itemDescription;
        }

        public String getRequestedQty() {
            return RequestedQty;
        }

        public void setRequestedQty(String requestedQty) {
            RequestedQty = requestedQty;
        }

        public String getUOM() {
            return UOM;
        }

        public void setUOM(String UOM) {
            this.UOM = UOM;
        }

        public String getRate() {
            return Rate;
        }

        public void setRate(String rate) {
            Rate = rate;
        }

        public String getRAmount() {
            return RAmount;
        }

        public void setRAmount(String RAmount) {
            this.RAmount = RAmount;
        }

        public String getDQty() {
            return DQty;
        }

        public void setDQty(String DQty) {
            this.DQty = DQty;
        }

        public String getDetailDAmount() {
            return DetailDAmount;
        }

        public void setDetailDAmount(String detailDAmount) {
            DetailDAmount = detailDAmount;
        }

        public String getDiscount() {
            return Discount;
        }

        public void setDiscount(String discount) {
            Discount = discount;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public String getItemBarcode() {
            return ItemBarcode;
        }

        public void setItemBarcode(String itemBarcode) {
            ItemBarcode = itemBarcode;
        }

        public String getVal1() {
            return val1;
        }

        public void setVal1(String val1) {
            this.val1 = val1;
        }

        public String getVal2() {
            return val2;
        }

        public void setVal2(String val2) {
            this.val2 = val2;
        }

        public String getVal3() {
            return val3;
        }

        public void setVal3(String val3) {
            this.val3 = val3;
        }

        public Boolean getIsreplace() {
            return isreplace;
        }

        public void setIsreplace(Boolean isreplace) {
            this.isreplace = isreplace;
        }

    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }


    public class Trackhistoryclass implements Serializable
    {
        @SerializedName("StatusID")
        String StatusID;

        @SerializedName("OrderId")
        String OrderId;
        @SerializedName("CreatedDate")
        String CreatedDate;


        @SerializedName("CreatedBy")
        String CreatedBy;


        @SerializedName("OrderStatus")
        String OrderStatus;


        public String getStatusID() {
            return StatusID;
        }

        public void setStatusID(String statusID) {
            StatusID = statusID;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(String createdBy) {
            CreatedBy = createdBy;
        }

        public String getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            OrderStatus = orderStatus;
        }
    }

}
