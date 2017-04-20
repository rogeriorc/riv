package com.roger.riv;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.WebSettings;

import com.totvs.smartclient.SmartClientActivity;
import com.totvs.smartclient.WebViewController;
import com.totvs.smartclient.WebViewHook;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RivActivity extends SmartClientActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.checkPermissions();
        }

        this.setCloudBridgeProgram("Riv");
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissions() {
        ArrayList<String> permissions = new ArrayList<>();

        Context context = this.getApplicationContext();
        String packageName = context.getPackageName();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (this.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED)
                        permissions.add(p);
                }
            }

            if (permissions.size() > 0) {
                this.requestPermissions(permissions.toArray(new String[0]), 999);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void webview(int id, String method, String param) {
        super.webview(id, method, param);

        final RivActivity _this = this;
        final int idwebview = id;

        Log.d("RIV", "Super WebView");

        if (method.toLowerCase().equals("paint")) {
            this.runOnUiThread(new Thread() {
                @Override
                public void run() {
                    try {
                        Log.d("RIV", "Super WebView try");
                        Field wvcf = _this.getClass().getSuperclass().getDeclaredField("webViewController");
                        wvcf.setAccessible(true);

                        WebViewController wvc = (WebViewController) wvcf.get(_this);

                        Field wlf = wvc.getClass().getDeclaredField("webList");
                        wlf.setAccessible(true);

                        SparseArray<WebViewHook> webList = (SparseArray<WebViewHook>) wlf.get(wvc);

                        WebViewHook wvh = webList.get(idwebview);
                        WebSettings settings = wvh.webview.getSettings();

                        settings.setSupportZoom(true);
                        settings.setBuiltInZoomControls(true);
                        settings.setDisplayZoomControls(false);

                        Log.d("RIV", "Super WebView Success");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
