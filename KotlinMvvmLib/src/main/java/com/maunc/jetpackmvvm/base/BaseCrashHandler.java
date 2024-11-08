package com.maunc.jetpackmvvm.base;

import android.annotation.SuppressLint;

import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = BaseCrashHandler.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static volatile BaseCrashHandler crashHandler;
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    //保证只有一个MyCrashHandler实例
    private BaseCrashHandler() {
    }

    // 获取CrashHandler实例 单例模式 - 双重校验锁
    public static BaseCrashHandler getInstance() {
        if (crashHandler == null) {
            synchronized (BaseCrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new BaseCrashHandler();
                }
            }
        }
        return crashHandler;
    }

    /**
     * 初始化
     */
    public void init() {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该MyCrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        //记录错误信息
        saveCrashInfoToFile(ex);
        //判断异常是否需要兜底
        if (needBandage(ex)) {
            bandage();
            return;
        }
        if (mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理 目的是判断异常是否已经被处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean needBandage(@NonNull Throwable ex) {
        String message = ex.getMessage();
        Log.d(TAG, "崩溃信息:" + message);
        //如果是没磁盘空间了，尝试清理一波缓存
        if (!TextUtils.isEmpty(message) && message.contains("No space left on device")) {
            return true;
        }
        //BadTokenException
        return ex instanceof WindowManager.BadTokenException;
    }

    /**
     * 让当前线程恢复运行
     */
    private void bandage() {
        try {
            //fix No Looper; Looper.prepare() wasn't called on this thread.
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            Looper.loop();
        } catch (Exception e) {
            uncaughtException(Thread.currentThread(), e);
        }
    }

    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfoToFile(Throwable ex) {
        //获取错误原因
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable exCause = ex.getCause();
        while (exCause != null) {
            exCause.printStackTrace(printWriter);
            exCause = exCause.getCause();
        }
        printWriter.close();

        String message = writer.toString();
        Log.d(TAG, "错误信息:" + message);
        //做上报操作
        //saveFile(message);
    }

    /**
     * 保存错误信息到本地
     */
    private static void saveFile(String message) {
        // 错误日志文件名称
        String fileName = "crash-" + getCurrentTimeDay() + ".log";
        // 判断sd卡可正常使用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件存储位置
            String path = Environment.getExternalStorageDirectory().getPath() + "/crashInfo/";
            File fl = new File(path);
            //创建文件夹
            if (!fl.exists()) {
                fl.mkdirs();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName, true);
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.d(TAG, "saveCrashInfoToFile: " + e1.getMessage());
            }
        }
    }

    @NonNull
    public static String getCurrentTimeDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
