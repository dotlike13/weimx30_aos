package com.coinplug.wemixwallet.sdk.data;

import com.google.gson.annotations.SerializedName;

public abstract class SendData{
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;

    public SendData(String from, String to){
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public abstract String getRequestType();
    public abstract TransactionData getTransactionData();
}
