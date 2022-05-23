package com.coinplug.wemixwallet.sdk;

import androidx.annotation.NonNull;

import com.coinplug.wemixwallet.sdk.data.Metadata;
import com.coinplug.wemixwallet.sdk.data.TransactionData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A2A proposal request data
 */
class A2AProposalRequest{
    @SerializedName("metadata")
    private Metadata metadata;
    @SerializedName("type")
    private String type;
    @SerializedName("transaction")
    @Expose
    private TransactionData transaction;

    public A2AProposalRequest(@NonNull Metadata metadata, @NonNull String type, TransactionData transaction){
        this.metadata = metadata;
        this.type = type;
        this.transaction = transaction;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TransactionData getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionData transaction) {
        this.transaction = transaction;
    }
}
