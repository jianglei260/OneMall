package com.myworld.onemall.data.source;

import android.content.Context;

import com.myworld.onemall.data.entity.Token;

/**
 * Created by jianglei on 2016/11/30.
 */

public interface UserDataSource {
    public Token login(String userName, String passwd,String deviceToken);

    public Token signUp(String userName, String passwd, String code,String deviceToken);

    public boolean isLogin(Context context);

    public Token getCurrentUserToken(Context context);

    public boolean logout(Context context);

    public Token findPD(String userName, String passwd, String code,String deviceToken);
}
