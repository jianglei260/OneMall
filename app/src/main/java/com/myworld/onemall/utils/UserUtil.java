package com.myworld.onemall.utils;

import android.content.Context;
import android.content.Intent;

import com.myworld.onemall.login.LoginActivity;

/**
 * Created by jianglei on 2016/12/2.
 */

public class UserUtil {
    public static void login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
