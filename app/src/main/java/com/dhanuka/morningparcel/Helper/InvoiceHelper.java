package com.dhanuka.morningparcel.Helper;



public class InvoiceHelper  {


    private int mId;
    private int orderid;
    private String mImageName;
    private String mDescription;
    private String mImagePath;
    private String mImageType;
    private String invoicedate;
    private String invoiceamount;
    private int mImageUploadStatus;
    private String mServerId;
    private String invoiceno;
    public  InvoiceHelper()
    {

    }
    public  InvoiceHelper(int orderid,String mImageName, String mDescription,String mImagePath,String mImageType,String invoicedate,String invoiceamount,int mImageUploadStatus,String mServerId,String invoiceno)
    {
        this.orderid = orderid;
        this.mImageName = mImageName;
        this.mDescription = mDescription;
        this.mImagePath = mImagePath;
        this.mImageType = mImageType;
        this.invoiceamount = invoiceamount;
        this.invoicedate = invoicedate;
        this.mImageUploadStatus = mImageUploadStatus;
        this.mServerId = mServerId;
        this.invoiceno = invoiceno;
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getmImageName() {
        return mImageName;
    }

    public void setmImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getmImageType() {
        return mImageType;
    }

    public void setmImageType(String mImageType) {
        this.mImageType = mImageType;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public String getInvoiceamount() {
        return invoiceamount;
    }

    public void setInvoiceamount(String invoiceamount) {
        this.invoiceamount = invoiceamount;
    }

    public int getmImageUploadStatus() {
        return mImageUploadStatus;
    }

    public void setmImageUploadStatus(int mImageUploadStatus) {
        this.mImageUploadStatus = mImageUploadStatus;
    }

    public String getmServerId() {
        return mServerId;
    }

    public void setmServerId(String mServerId) {
        this.mServerId = mServerId;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }
}
