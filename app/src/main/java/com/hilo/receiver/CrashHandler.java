package com.hilo.receiver;

import android.content.Context;

import com.hilo.utils.LogUtils;
import com.hilo.utils.UtilTool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hilo on 16/2/24.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private static CrashHandler INSTANCE;
    // 用户存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    // 保证只有一个CrashHandler实例
    private CrashHandler() {}

    public static CrashHandler getInstance() {
        if(INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean handlerException(final Throwable ex) {
        LogUtils.E("错误日志信息<< " + ex + " >>");
        if(ex == null) return false;
        // 注释后，则开启后台DEBUG信息
        if(UtilTool.isApkDebugable(mContext)) {
            return false;
        }
        // 下面写公司后台debugApi, 将用户的信息以及错误日志，以Post形式发送给服务端展示到后台界面
        // ...
        return false;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            // 如果用户没有主动处理则让系统默认的异常处理器去处理
            if(!handlerException(ex) && uncaughtExceptionHandler != null) {
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            } else {
                // 如果自己处理了异常，则不会弹出错误对话框，需要手动退出app
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
