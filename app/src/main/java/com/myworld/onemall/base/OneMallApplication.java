package com.myworld.onemall.base;

import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.myworld.onemall.R;
import com.myworld.onemall.data.repository.DynamicLoadRepository;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;


/**
 * Created by jianglei on 2016/11/25.
 */

public class OneMallApplication extends Application {
    private static OneMallApplication instance;
    public static final String ACTION_ERROR_MESSAGE = "com.myworld.onemall.action.error";
    public static final String MESSAGE = "message";
    private String deviceToken;
    private static final String TAG = "OneMallApplication";
    private AssetManager mAssetManager;
    private Resources mResources;
    private String mClassLoaderName = OneMallApplication.class.getClassLoader().toString();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getStringExtra(MESSAGE), Toast.LENGTH_SHORT).show();
        }
    };
    private UmengMessageHandler messageHandler = new UmengMessageHandler() {
        @Override
        public void dealWithCustomMessage(Context context, UMessage uMessage) {
            super.dealWithCustomMessage(context, uMessage);
            Log.d(uMessage.custom, uMessage.custom);
            Toast.makeText(context, uMessage.custom, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(this);
        IntentFilter filter = new IntentFilter(ACTION_ERROR_MESSAGE);
        registerReceiver(receiver, filter);
        try {
            PushAgent mPushAgent = PushAgent.getInstance(this);
            //注册推送服务，每次调用register方法都会回调该接口
            mPushAgent.register(new IUmengRegisterCallback() {

                @Override
                public void onSuccess(String deviceToken) {
                    //注册成功会返回device token
                    Log.d(TAG, "onSuccess: " + deviceToken);
                    OneMallApplication.this.deviceToken = deviceToken;
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.d(TAG, "onFailure: " + s);
                }
            });
            mPushAgent.setMessageHandler(messageHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(receiver);
    }

    @Override
    public AssetManager getAssets() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().contains("com.myworld.active")){
                if (mAssetManager == null) {
                    loadResources(DynamicLoadRepository.path);
                }
                return mAssetManager;
            }
        }
        return getBaseContext().getAssets();
    }

    public void loadResources(String appPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, appPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = getBaseContext().getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
    }

    @Override
    public Resources getResources() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().contains("com.myworld.active")){
                if (mResources == null) {
                    loadResources(DynamicLoadRepository.path);
                }
                return mResources;
            }
        }
        return getBaseContext().getResources();
    }

    public static OneMallApplication getInstance() {
        return instance;
    }
}
