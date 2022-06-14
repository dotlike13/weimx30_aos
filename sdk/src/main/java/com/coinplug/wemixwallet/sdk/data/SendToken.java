package com.coinplug.wemixwallet.sdk.data;

import androidx.annotation.NonNull;

import com.coinplug.wemixwallet.sdk.WemixWalletSDK;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendToken extends SendData{
    @SerializedName("value")
    private String value;
    @SerializedName("contract")
    private String contract;

    public SendToken(@NonNull String from, @NonNull String to, @NonNull String value, @NonNull String contract){
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

    @Override
    public TransactionData getTransactionData(){
        return  new TransactionData(getFrom(),getTo(), value, contract,null,null,null);
    }

    @Override
    public String getRequestType(){
        return WemixWalletSDK.RequestType.send_token.toString();
    }

}
