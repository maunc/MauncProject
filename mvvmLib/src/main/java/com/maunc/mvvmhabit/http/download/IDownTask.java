package com.maunc.mvvmhabit.http.download;

public interface IDownTask {
    String getTaskUrl();

    String getTaskTag();

    String getTaskFolder();

    String getTaskName();

    String getTaskTempName();

    void onTaskFinish();

    long getTaskFileSize();

    boolean checkPath();
}
