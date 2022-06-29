package com.wemixfoundation.wemixwallet.sdk.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionData{
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("contract")
    @Expose
    private String contract;
    @SerializedName("tokenId")
    @Expose
    private String tokenId;
    @SerializedName("abi")
    @Expose
    private String abi;
    @SerializedName("params")
    @Expose
    private String params;

    public TransactionData(@NonNull String from, @NonNull String to, String value, String contract, String tokenId, String abi, String params){
        this.from = from;
        this.to = to;
        this.value = value;
        this.contract = contract;
        this.tokenId = tokenId;
        this.abi = abi;
        this.params = params;
    }

    public String getFrom(){
        return from;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public String getTo(){
        return to;
    }

    public void setTo(String to){
        this.to = to;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getContract(){
        return contract;
    }

    public void setContract(String contract){
        this.contract = contract;
    }

    public String getTokenId(){
        return tokenId;
    }

    public void setTokenId(String tokenId){
        this.tokenId = tokenId;
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
}
