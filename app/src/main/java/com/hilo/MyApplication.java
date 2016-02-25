package com.hilo;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.hilo.others.Configuration;
import com.hilo.receiver.CrashHandler;
import com.hilo.utils.LogUtils;
import com.hilo.utils.UIUtils;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by hilo on 16/2/19.
 */
public class MyApplication extends Application {

    public static Context mContext;
    public static Typeface font;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Configuration cfg = Configuration.getConfig();
        Configuration.saveConfig();
        cfg.deviceType = 1;

//        boolean isTablet = UIUtils.isTablet(getResources());
        if (getResources().getBoolean(R.bool.isTablet)) cfg.deviceType = 2;
        cfg.deviceName = android.os.Build.BRAND + "_" + android.os.Build.PRODUCT;
        LogUtils.I("klog", "deviceName: " + cfg.deviceName);

        AssetManager mgr = this.getAssets();
        font = Typeface.createFromAsset(mgr, "fonts/PingFang Medium.ttf");

        // 检查内存泄露
        LeakCanary.install(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }
}
