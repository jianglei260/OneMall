package com.myworld.onemall.data.repository;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.myworld.onemall.base.OneMallApplication;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.source.UserDataSource;
import com.myworld.onemall.data.source.remote.GoodsRemoteDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

/**
 * Created by jianglei on 2016/11/30.
 */

public class UserRepository implements UserDataSource {
    private static UserRepository repository;
    private GoodsRepository remote;
    private Token token;

    public static UserRepository getRepository() {
        if (repository == null)
            repository = new UserRepository();
        return repository;
    }

    UserRepository() {
        remote = GoodsRepository.getRepository();
    }

    @Override
    public Token login(String userName, String passwd, String deviceToken) {
        Token token = remote.login(userName, passwd, deviceToken);
        this.token = token;
        if (token != null) {
            token.setPhone(userName);
            saveToLocal(OneMallApplication.getInstance(), token);
        }
        return token;
    }

    @Override
    public Token signUp(String userName, String passwd, String code, String deviceToken) {
        Token token = remote.signUp(userName, passwd, code, deviceToken);
        this.token = token;
        if (token != null) {
            token.setPhone(userName);
            saveToLocal(OneMallApplication.getInstance(), token);
        }
        return token;
    }

    @Override
    public boolean isLogin(Context context) {
        Token token = getCurrentUserToken(context);
        if (token == null)
            return false;
        if (token != null) {
            long epire = token.getExpire();
            if (epire <= System.currentTimeMillis() - 10 * 1000)
                return false;
            return !TextUtils.isEmpty(token.getValue());
        }
        return false;
    }

    @Override
    public Token getCurrentUserToken(Context context) {
        if (this.token == null)
            this.token = getLocalToken(context);
        return this.token;
    }

    @Override
    public boolean logout(Context context) {
        boolean result = deleteToken(context);
        if (result) {
            CartRepository.geRepository().clearCache();
            OrderRepository.geRepository().clearCache();
            this.token = null;
        }
        return result;
    }

    @Override
    public Token findPD(String userName, String passwd, String code, String deviceToken) {
        Token token = remote.findPD(userName, passwd, code, deviceToken);
        this.token = token;
        if (token != null) {
            token.setPhone(userName);
            saveToLocal(OneMallApplication.getInstance(), token);
        }
        return token;
    }

    public boolean getSmsCode(String deviceId, String phoneNumber) {
        return remote.getSmsCode(deviceId, phoneNumber);
    }

    public boolean deleteToken(Context context) {
        this.token = null;
        String authDataPath = context.getFilesDir().getAbsolutePath() + File.separator + "user.json";
        File authData = new File(authDataPath);
        if (!authData.exists())
            return false;
        else
            return authData.delete();
    }

    public Token getLocalToken(Context context) {
        Gson gson = new Gson();
        String authDataPath = context.getFilesDir().getAbsolutePath() + File.separator + "user.json";
        File authData = new File(authDataPath);
        if (!authData.exists())
            return null;
        try {
            Reader reader = new FileReader(authData);
            return gson.fromJson(reader, Token.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveToLocal(Context context, Token token) {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "user.json";
        Gson gson = new Gson();
        String json = gson.toJson(token);
        File authData = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(authData, false);
            byte[] data = json.getBytes();
            fos.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
