package com.coinplug.wemixwallet.sdk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExecuteContract extends SendData{
    @SerializedName("abi")
    @Expose
    private String abi;
    @SerializedName("params")
    @Expose
    private String params;

    public ExecuteContract(String from, String to, String abi, String params){
        super(from, to);
        this.abi = abi;
        this.params = params;
    }

    public String getAbi(){
        return abi;
    }

    public void setAbi(String abi){
        this.abi = abi;
    }

    public String getParams(){
        return params;
    }

    public void setParams(String params){
        this.params = params;
    }

    public TransactionData getTransactionData(){
        return  new TransactionData(getFrom(),getTo(), null, null,null,abi,params);
    }
}
