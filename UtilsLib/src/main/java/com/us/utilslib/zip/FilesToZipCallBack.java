package com.us.utilslib.zip;

public interface FilesToZipCallBack {
    default void onFailed() {
    }

    void onSuccess();
}
