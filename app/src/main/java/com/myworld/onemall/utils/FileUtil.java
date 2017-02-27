package com.myworld.onemall.utils;

import java.io.File;

/**
 * Created by jianglei on 16/9/9.
 */

public class FileUtil {
    public static long getFileSize(long size, File rootFile) {
        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                size += getFileSize(0, file);
            }
            return size;
        } else
            return size + rootFile.length();
    }

    public static void deleteFile(File rootFile) {
        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
            rootFile.delete();
        } else {
            rootFile.delete();
        }
    }
}
