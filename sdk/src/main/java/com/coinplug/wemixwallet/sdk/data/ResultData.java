package com.coinplug.wemixwallet.sdk.data;

import com.google.gson.annotations.SerializedName;

public class ResultData{
    @SerializedName("address")
    private String address;
    @SerializedName("transactionHash")
    private String transactionHash;

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getTransactionHash(){
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash){
        this.transactionHash = transactionHash;
    }
}
