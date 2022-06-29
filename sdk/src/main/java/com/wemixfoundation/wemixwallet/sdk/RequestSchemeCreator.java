package com.wemixfoundation.wemixwallet.sdk;

import static com.wemixfoundation.wemixwallet.sdk.WemixWalletSDK.REQUEST_ID;

import android.net.Uri;

import androidx.annotation.NonNull;

public class RequestSchemeCreator{
    public static @NonNull Uri create(@NonNull ActivityWrapper activityWrapper, @NonNull String requestId) {
        // Create scheme
        Uri.Builder appSchemebuilder = new Uri.Builder().scheme("wemix")
                .authority("wallet")
                .appendQueryParameter(REQUEST_ID, requestId);

        return appSchemebuilder.build();
    }
}
