package com.us.ziplib;

import android.annotation.SuppressLint;
import android.util.Log;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;


public class ZipUtils {

    public static final String TAG = "ZipUtils";

    /**
     * 解压文件
     */
    @SuppressLint("SdCardPath")
    public static void zipToFiles(String zipFilePath, String decompressDir, UpZipFileCallBack upZipFileCallBack) {
        new Thread(() -> {
            BufferedInputStream bi = null;
            ZipFile zf = null;// 支持中文
            try {
                zf = new ZipFile(zipFilePath, "GBK");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Enumeration e = zf.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = decompressDir + "/" + entryName;
                if (ze2.isDirectory()) {
                    //Log.d(TAG, "readByApacheZipFile:正在创建解压目录 - " + entryName);
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    //Log.d(TAG, "readByApacheZipFile:正在创建解压文件 - " + entryName);
                    upZipFileCallBack.onProgress();
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(
                                new FileOutputStream(decompressDir + "/" + entryName));
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    try {
                        bi = new BufferedInputStream(zf.getInputStream(ze2));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    byte[] readContent = new byte[1024];
                    int readCount = 0;
                    try {
                        readCount = bi.read(readContent);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    while (readCount != -1) {
                        try {
                            bos.write(readContent, 0, readCount);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        try {
                            readCount = bi.read(readContent);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    try {
                        bos.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        upZipFileCallBack.onFailed();
                    }
                }
            }
            try {
                zf.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                upZipFileCallBack.onFailed();
            }
            upZipFileCallBack.onSuccess();
        }).start();
    }

    /**
     * 判断文件是否已经解压
     */
    public static boolean fileExistsToZipSuccess(String filePath) {
        if ("".equals(filePath) || filePath == null) {
            return false;
        }
        File dckFile = new File(filePath);
        if (dckFile.exists()) {
            Log.e(TAG, "dckFileExists ->" + true);
            return true;
        }
        Log.e(TAG, "dckFileExists ->" + false);
        return false;
    }

    public interface UpZipFileCallBack {
        default void onProgress() {
        }
        default void onFailed() {
        }

        void onSuccess();
    }
}
