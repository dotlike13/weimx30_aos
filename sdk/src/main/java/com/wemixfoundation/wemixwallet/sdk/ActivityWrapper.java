package com.wemixfoundation.wemixwallet.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 * Activity 를 참조하기 위한 wrapper class
 *
 * @author ybjeon
 */
@SuppressWarnings({"unused", "SameParameterValue"})
class ActivityWrapper {
    private Activity activity;
    private Fragment fragment;

    ActivityWrapper(Activity activity) {
        this.activity = activity;
    }

    ActivityWrapper(Fragment fragment) {
        this.fragment = fragment;
    }

    public void startActivity(Intent intent) {
        if (activity != null) {
            activity.startActivity(intent);
        }
        else if (fragment != null) {
            fragment.startActivity(intent);
        }
        else {
            throw new IllegalStateException("ActivityWrapper does not contains activity or fragment");
        }
    }

    void startActivityForResult(Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        }
        else if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
        else {
            throw new IllegalStateException("ActivityWrapper does not contains activity or fragment");
        }
    }

    Context getContext() {
        if (activity != null) {
            return activity;
        }
        else if (fragment != null) {
            return fragment.getActivity();
        }
        else {
            throw new IllegalStateException("ActivityWrapper does not contains activity or fragment");
        }
    }
}
