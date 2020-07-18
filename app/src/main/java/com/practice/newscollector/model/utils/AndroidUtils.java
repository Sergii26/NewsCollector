package com.practice.newscollector.model.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidUtils implements Utils{
    private final Context context;
    public AndroidUtils(Context context) {
        this.context = context;
    }

    public boolean isConnectedToNetwork() {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivityManager != null) {
            final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        return isConnected;
    }
}
