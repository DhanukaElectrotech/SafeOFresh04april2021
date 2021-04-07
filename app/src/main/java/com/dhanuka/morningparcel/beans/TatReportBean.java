package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TatReportBean implements Serializable {
    @SerializedName("success")
    String success;
    @SerializedName("returnds")
    ArrayList<tatinnerbean> branchdatalist;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<tatinnerbean> getBranchdatalist() {
        return branchdatalist;
    }

    public void setBranchdatalist(ArrayList<tatinnerbean> branchdatalist) {
        this.branchdatalist = branchdatalist;
    }


    public static class tatinnerbean implements Serializable {

        @SerializedName("RItemCOunt")
        String RItemCOunt;

        @SerializedName("RAmount")
        String RAmount;
        @SerializedName("TotalOrder")
        String TotalOrder;
        @SerializedName("OrderID")
        String OrderID;
        // @SerializedName("BranchID")
        @SerializedName("Status")
        String Status;
        @SerializedName("OrderDate")
        String OrderDate;


        @SerializedName("CustomerID")
        String CustomerID;

        @SerializedName("CustomerName")
        String CustomerName;
        @SerializedName("DelayReason")
        String DelayReason;

        @SerializedName("TrackHistory")
        ArrayList<Trackhistoryclass>  trackhistory;

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

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String orderDate) {
            OrderDate = orderDate;
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

        public String getDelayReason() {
            return DelayReason;
        }

        public void setDelayReason(String delayReason) {
            DelayReason = delayReason;
        }

        public ArrayList<Trackhistoryclass> getTrackhistory() {
            return trackhistory;
        }

        public void setTrackhistory(ArrayList<Trackhistoryclass> trackhistory) {
            this.trackhistory = trackhistory;
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
}
