package com.hilo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.hilo.base.BasePresenterActivity;
import com.hilo.base.BasePresenterFragment;
import com.hilo.others.Configuration;
import com.hilo.receiver.CrashHandler;

/**
 * Created by hilo on 16/2/24.
 */
public class UtilTool {

    public static void appLogout(Context context) {
        BasePresenterActivity.logoutReceiverRepleaseResources();
    }

    public static void setVariablesNull() {
        Configuration.setConfigNull();
        BasePresenterFragment.setVariablesNull();
    }

    /**
     * 判断当前应用是否处于debug状态
     *
     * @param context
     * @return
     */
    public static boolean isApkDebugable(Context context) {
        boolean flag = false;
        try {
            if (null != context) {
                ApplicationInfo info = context.getApplicationInfo();
                flag = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
