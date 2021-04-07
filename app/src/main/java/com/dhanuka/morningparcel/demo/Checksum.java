package com.dhanuka.morningparcel.demo;
import com.google.gson.annotations.SerializedName;

public class Checksum {


    @SerializedName("CHECKSUMHASH")
    private String checksumHash;

    @SerializedName("ORDER_ID")
    private String orderId;

    @SerializedName("payt_STATUS")
    private String paytStatus;
   @SerializedName("PAYTM_MERCHANT_KEYE")
    private String PAYTM_MERCHANT_KEYE;

    public Checksum(String checksumHash, String orderId, String paytStatus) {
        this.checksumHash = checksumHash;
        this.orderId = orderId;
        this.paytStatus = paytStatus;
    }

    public void setChecksumHash(String checksumHash) {
        this.checksumHash = checksumHash;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setPaytStatus(String paytStatus) {
        this.paytStatus = paytStatus;
    }

    public String getPAYTM_MERCHANT_KEYE() {
        return PAYTM_MERCHANT_KEYE;
    }

    public void setPAYTM_MERCHANT_KEYE(String PAYTM_MERCHANT_KEYE) {
        this.PAYTM_MERCHANT_KEYE = PAYTM_MERCHANT_KEYE;
    }

    public String getChecksumHash() {
        return checksumHash;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaytStatus() {
        return paytStatus;
    }
}
