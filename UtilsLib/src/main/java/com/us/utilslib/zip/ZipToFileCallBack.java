package com.us.utilslib.zip;

public interface ZipToFileCallBack {
    default void onProgress() {
    }

    default void onFailed() {
    }

    void onSuccess();
}
