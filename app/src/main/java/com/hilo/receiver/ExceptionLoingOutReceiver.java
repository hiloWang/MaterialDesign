package com.hilo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hilo.utils.UtilTool;

/**
 * Created by hilo on 16/2/24.
 */
public class ExceptionLoingOutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UtilTool.appLogout(context);
    }
}
