package com.wemixfoundation.wemixwallet.sdk.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("successCallback")
    @Expose
    private String successCallback;
    @SerializedName("failureCallback")
    @Expose
    private String failureCallback;

    public Metadata(@NonNull String name, String description, String url, String icon, String successCallback, String failureCallback){
        this.name = name;
        this.description = description;
        this.url = url;
        this.icon = icon;
        this.successCallback = successCallback;
        this.failureCallback = failureCallback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSuccessCallback() {
        return successCallback;
    }

    public void setSuccessCallback(String successCallback) {
        this.successCallback = successCallback;
    }

    public String getFailureCallback() {
        return failureCallback;
    }

    public void setFailureCallback(String failureCallback) {
        this.failureCallback = failureCallback;
    }

}