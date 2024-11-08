package com.us.utilslib;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class ZipUtils {

    public static final String TAG = "ZipUtils";

    /**
     * 解压文件
     */
    @SuppressLint("SdCardPath")
    public static void zipToFiles(String zipFilePath, String decompressDir, ZipToFileCallBack callBack) {
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
                    callBack.onProgress();
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
                        callBack.onFailed();
                    }
                }
            }
            try {
                zf.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                callBack.onFailed();
            }
            callBack.onSuccess();
        }).start();
    }

    @SuppressLint("SdCardPath")
    public static void filesToZip(String sourceFolder, String zipFilePath,FilesToZipCallBack callBack) {
        new Thread(() -> {
            OutputStream os = null;
            BufferedOutputStream bos = null;
            ZipOutputStream zos = null;
            try {
                os = new FileOutputStream(zipFilePath);
                bos = new BufferedOutputStream(os);
                zos = new ZipOutputStream(bos);
                // 解决中文文件名乱码
                zos.setEncoding("GBK");
                File file = new File(sourceFolder);
                String basePath = null;
                if (file.isDirectory()) {//压缩文件夹
                    basePath = file.getPath();
                } else {
                    basePath = file.getParent();
                }
                zipFile(file, basePath, zos);
            } catch (Exception e) {
                e.printStackTrace();
                callBack.onFailed();
            } finally {
                try {
                    if (zos != null) {
                        zos.closeEntry();
                        zos.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }).start();
    }

    /**
     * 递归压缩文件
     */
    private static void zipFile(@NonNull File parentFile, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is;
        BufferedInputStream bis;
        byte[] cache = new byte[1024];
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + File.separator;
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                int nRead = 0;
                while ((nRead = bis.read(cache, 0, 1024)) != -1) {
                    zos.write(cache, 0, nRead);
                }
                bis.close();
                is.close();
            }
        }
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

    public interface ZipToFileCallBack {
        default void onProgress() {
        }

        default void onFailed() {
        }

        void onSuccess();
    }

    public interface FilesToZipCallBack {
        default void onFailed() {
        }

        void onSuccess();
    }
}
