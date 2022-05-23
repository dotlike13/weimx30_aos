package com.coinplug.wemixwallet.sdk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendToken extends SendData{
    @SerializedName("value")
    private String value;
    @SerializedName("contract")
    private String contract;

    public SendToken(String from, String to, String value, String contract){
        super(from, to);
        this.value = value;
        this.contract = contract;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public TransactionData getTransactionData(){
        return  new TransactionData(getFrom(),getTo(), value, contract,null,null,null);
    }
}
