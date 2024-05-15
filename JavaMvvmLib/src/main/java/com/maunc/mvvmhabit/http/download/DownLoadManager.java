package com.maunc.mvvmhabit.http.download;

import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;

import com.maunc.mvvmhabit.BaseApp;
import com.maunc.mvvmhabit.message.bus.RxBus;
import com.maunc.mvvmhabit.message.bus.RxSubscriptions;
import com.maunc.mvvmhabit.utils.DeviceUtils;
import com.maunc.mvvmhabit.utils.NetworkUtils;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

import io.reactivex.disposables.Disposable;

public class DownLoadManager {

    public int progressOI = 0;
    public int progressMaxOI = 100;
    public int statusOF = Progress.NONE;
    public String progressMsgOF; //状态msg
    public long currentSizeOI = 0;
    private IDownTask downTask;
    private String className;
    private OnStateChangeListener listener;
    private String mTag;
    private String progressSpeedOF;

    public DownLoadManager(IDownTask t, String className, String tag) {
        progressMsgOF = "下载";
        downTask = t;
        this.className = className;
        this.mTag = tag;
        Progress progress = DownTaskTools.getDownloadTask(t).progress;
        if (progress.status == Progress.FINISH && !getFile().exists()) {
            progress = new Progress();
        }
        upState(progress);
        registerRxBus();
    }

    public String getTag() {
        return mTag;
    }

    public void getChange() {
        Progress progress = DownTaskTools.getDownloadTask(downTask).progress;
        if (progress.status == Progress.FINISH && !getFile().exists()) {
            progress = new Progress();
        }
        upState(progress);
    }

    private void upState(@NonNull Progress progress) {
        if (progress.status == Progress.NONE && progress.currentSize != 0) {
            progress.status = Progress.PAUSE;
        }
        statusOF = progress.status;
        currentSizeOI = progress.currentSize;
        progressOI = ((int) (progress.currentSize * 100 / progress.totalSize));
        progressSpeedOF = Formatter.formatFileSize(BaseApp.getInstance(), progress.speed);
        if (progress.status == Progress.PAUSE) {
            progressMsgOF = "暂停";
        } else if (progress.status == Progress.NONE) {
            progressMsgOF = "下载";
        } else if (progress.status == Progress.WAITING) {
            progressMsgOF = "队列中";
        } else if (progress.status == Progress.LOADING) {
            progressMsgOF = "下载中";
        } else if (progress.status == Progress.ERROR) {
            progressMsgOF = "错误";
            if (progress.exception != null) {
                progress.exception.printStackTrace();
            }
//            DownloadTask task = DownTaskTools.getDownloadTask(downTask);
//            task.remove(true);
//            resetProgress();
            if (!progress.request.getUrl().equalsIgnoreCase(downTask.getTaskUrl())) {
                downFile();
            } else {
                progressMsgOF = "下载失败";
            }
        } else if (progress.status == Progress.FINISH) {
            progressMsgOF = "完成";
            if (listener != null) {
                listener.taskFinish();
            }
        }
        if (listener != null) {
            Log.e("hjj-->", "onStateChange :" + "progressOI=" + progressOI + ",statusOF=" + statusOF + ",progressMsgOF=" + progressMsgOF + "\n,progressSpeedOF=" + progressSpeedOF);
            listener.onStateChange(progressOI, statusOF, progressMsgOF, progressSpeedOF);
        } else {
            Log.e("hjj-->", "listener is null");
        }
    }

    public File getFile() {
        return new File(downTask.getTaskFolder() + "/" + downTask.getTaskName());
    }

    public void downPause() {
        DownloadTask task = DownTaskTools.getDownloadTask(downTask);
        task.pause();
    }

    public void downFile() {
        DownloadTask task = DownTaskTools.getDownloadTask(downTask);
        boolean finish = task.progress.status == Progress.FINISH && getFile().exists();
        Log.e("hjj-->", "finish=" + finish + ",task.progress=" + task.progress.status);
        if (!finish && downTask.checkPath()) {
            String taskFolder = downTask.getTaskFolder();
            if (!taskFolder.startsWith(DeviceUtils.getSDCardPath() + "download")) {
                reset(task);
                return;
            }
        }
        long availSpace = availSpace(downTask.getTaskFolder());
        if (availSpace == -1) {
            Log.e("hjj-->", "存储路径读取失败，请检查是否授权");
            return;
        }
        if (availSpace < DownTaskTools.LIMIT_AVAIL_SPACE && !finish) {
            Log.e("hjj-->", "下载失败,机身不足500mb");
            return;
        }
        if (availSpace < downTask.getTaskFileSize() && !finish) {
            Log.e("hjj-->", "内存已满，清理空间");
            return;
        }
        switch (task.progress.status) {
            case Progress.PAUSE:
            case Progress.NONE:
            case Progress.ERROR:
                if (NetworkUtils.isNetworkAvailable()) {
                    statusOF = Progress.WAITING;
                    progressMsgOF = "等待中";
                    removeRxBus();
                    registerRxBus();
                    task.start();
                }
                break;
            case Progress.LOADING:
                task.pause();
                break;
            case Progress.FINISH:
                if (getFile().exists()) {
                    if (listener != null) {
                        listener.taskFinish();
                    }
                } else {
                    reset(task);
                }
                break;
        }
    }

    /**
     *创建路径获取可用空间
     */
    public long availSpace(String path) {
        try {
            if (!new File(path).exists()) {
                Log.d("AppUtil", path + "#file not exist ");
                boolean mkd = new File(path).mkdirs();
                if (!mkd) {
                    Log.e("AppUtil", "创建目录失败！" + path);
                    return -1;
                }
            }
            //获取可用内存大小
            StatFs statfs = new StatFs(path);
            //获取可用区块的个数
            long count = statfs.getAvailableBlocksLong();
            //获取区块大小
            long size = statfs.getBlockSizeLong();
            //可用空间总大小
            return count * size;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void reset(DownloadTask task) {
        Log.e("hjj-->", "reset");
        task.remove(true);
        resetProgress();
        downFile();
    }

    private void resetProgress() {
        Log.e("hjj-->", "resetProgress");
        statusOF = Progress.NONE;
        DownTaskTools.delete(downTask);
    }

    //订阅者
    private Disposable mSubscription, removeSubscription;

    public void registerRxBus() {
        Log.e("hjj-->", "registerRxBus");
        mSubscription = RxBus.getDefault().toObservable(Progress.class).subscribe(s -> {
            if (s.tag.equalsIgnoreCase(downTask.getTaskTag())) {
                upState(s);
            }
        });
        removeSubscription = RxBus.getDefault().toObservable(_DownRemoveStatus.class).subscribe(s -> {
            if (s.className.equalsIgnoreCase(className)) {
                removeRxBus();
            }
        });
        //将订阅者加入管理站
        RxSubscriptions.add(mSubscription);
        RxSubscriptions.add(removeSubscription);
    }

    public void removeRxBus() {
        Log.e("hjj-->", "removeRxBus");
        //将订阅者从管理站中移除
        if (mSubscription != null) {
            RxSubscriptions.remove(mSubscription);
        }
        if (removeSubscription != null) {
            RxSubscriptions.remove(removeSubscription);
        }
    }

    public void setOnDownStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnStateChangeListener {
        void onStateChange(int progress, int status, String progressMsg, String progressSpeed);

        default void taskFinish() {
        }
    }
}
