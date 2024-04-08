package com.maunc.mvvmhabit.http.download;

import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

public class DownTaskTools {

    public static final int LIMIT_AVAIL_SPACE = 500 * 1024 * 1024;//预留存储空间
    public static DownloadTask getDownloadTask(IDownTask downTask) {
        if (TextUtils.isEmpty(downTask.getTaskUrl())) {
            Log.e("DOWN", "资源路径不存在");
            return null;
        }

        DownloadTask task = OkDownload.getInstance().getTask(downTask.getTaskTag());
        if (task == null) {
            deleteFile(downTask);
            String tempFileName = downTask.getTaskTempName();
            //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
            GetRequest<File> request = OkGo.get(downTask.getTaskUrl());
            task = OkDownload.request(downTask.getTaskTag(), request)
                    .folder(downTask.getTaskFolder())
                    .fileName(tempFileName)//
                    .save()//
                    .register(new IDownloadListener(downTask));
        } else {
            if (task.listeners.get(downTask.getTaskTag()) == null)
                task.register(new IDownloadListener(downTask));
        }
        return task;
    }

    public static boolean delete(IDownTask downTask) {
        OkDownload.getInstance().removeTask(downTask.getTaskTag());
        DownloadManager.getInstance().delete(downTask.getTaskTag());
        return deleteFile(downTask);
    }

    private static boolean deleteFile(IDownTask downTask) {
        File file = new File(downTask.getTaskFolder() + "/" + downTask.getTaskName());
        File tempFile = new File(downTask.getTaskFolder() + "/" + downTask.getTaskTempName());
        if (file.exists()) {
            file.delete();
            Log.e("hjj-->", "删除成功");
            return true;
        }
        if(tempFile.exists()) {
            tempFile.delete();
            Log.e("hjj-->", "删除成功");
            return true;
        }
        return false;
    }
}
