package com.myworld.onemall.data.repository;

import android.os.Environment;

import com.myworld.onemall.base.OneMallApplication;

import dalvik.system.DexClassLoader;

/**
 * Created by jianglei on 2016/12/20.
 */

public class DynamicLoadRepository {
    private static DynamicLoadRepository instance;
    public static String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/active.apk";;

    public static DynamicLoadRepository getInstance() {
        if (instance == null) {
            instance = new DynamicLoadRepository();
        }
        return instance;
    }



    public Class loadClass(String classPath) {
        DexClassLoader classLoader = new DexClassLoader(path, OneMallApplication.getInstance().getCacheDir().getAbsolutePath(), null, getClass().getClassLoader());
        try {
            return classLoader.loadClass(classPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
