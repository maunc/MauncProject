package com.maunc.mvvmhabit.http.download;

import androidx.annotation.NonNull;

import com.maunc.mvvmhabit.message.bus.RxBus;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;

public class IDownloadListener extends DownloadListener {
    private IDownTask downTask;

    public IDownloadListener(@NonNull IDownTask downTask) {
        super(downTask.getTaskTag());
        this.downTask = downTask;
    }

    @Override
    public void onStart(Progress progress) {
        RxBus.getDefault().post(progress);
    }

    @Override
    public void onProgress(Progress progress) {
        RxBus.getDefault().post(progress);
    }

    @Override
    public void onError(Progress progress) {
        RxBus.getDefault().post(progress);
    }

    @Override
    public void onFinish(File file, Progress progress) {
        if (downTask != null) {
//            if (file.length() != downTask.getTaskFileSize()) {
//                DownTaskTools.delete(downTask);
//                progress.status = Progress.ERROR;
//            } else {
            file.renameTo(new File(downTask.getTaskFolder(), downTask.getTaskName()));
            downTask.onTaskFinish();
//            }
        }
        RxBus.getDefault().post(progress);
    }

    @Override
    public void onRemove(Progress progress) {
        RxBus.getDefault().post(progress);
    }
}
