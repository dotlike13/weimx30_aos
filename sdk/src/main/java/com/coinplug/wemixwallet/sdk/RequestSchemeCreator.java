package com.coinplug.wemixwallet.sdk;

import android.net.Uri;

import androidx.annotation.NonNull;

public class RequestSchemeCreator{
    public static @NonNull Uri create(@NonNull ActivityWrapper activityWrapper, @NonNull String requestId) {
        // Create scheme
        Uri.Builder appSchemebuilder = new Uri.Builder().scheme("https")
                .authority("mykeepin.coinplug.com")
                .path("sign")
                .appendQueryParameter("requestId", requestId);

        return appSchemebuilder.build();
    }
}
