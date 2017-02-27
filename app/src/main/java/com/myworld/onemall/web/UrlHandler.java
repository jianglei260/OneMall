package com.myworld.onemall.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.detail.DetailActivity;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by jianglei on 2016/12/16.
 */

public class UrlHandler {
    public static HashMap<String, Class<? extends BaseActivity>> router = new HashMap<>();

    static {
        router.put("http://119.29.141.199/goods", DetailActivity.class);
    }

    public static boolean handled(Context context, String url) {
        Uri uri = Uri.parse(url);
        String parsedUrl=uri.getScheme()+"://"+uri.getHost()+uri.getPath();
        if (router.containsKey(parsedUrl)) {
            Class activity = router.get(parsedUrl);
            Set<String> params = uri.getQueryParameterNames();
            Intent intent = new Intent(context, activity);
            for (String param : params) {
                intent.putExtra(param, uri.getQueryParameter(param));
            }
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }

    }
}
