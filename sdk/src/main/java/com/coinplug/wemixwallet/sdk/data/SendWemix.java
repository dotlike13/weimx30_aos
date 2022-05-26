package com.coinplug.wemixwallet.sdk.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendWemix extends SendData{

    @SerializedName("value")
    private String value;

    public SendWemix(@NonNull String from, @NonNull String to, @NonNull String value){
        super(from, to);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TransactionData getTransactionData(){
        return  new TransactionData(getFrom(),getTo(), value, null,null,null,null);
    }

}
