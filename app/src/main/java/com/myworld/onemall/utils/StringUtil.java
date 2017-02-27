package com.myworld.onemall.utils;

import com.myworld.onemall.R;
import com.myworld.onemall.base.OneMallApplication;

/**
 * Created by jianglei on 2016/12/1.
 */

public class StringUtil {
    public static String getRMBString(float value) {
        return OneMallApplication.getInstance().getString(R.string.rmb_prefix) + value;
    }

    public static String getRMBString(String value) {
        return OneMallApplication.getInstance().getString(R.string.rmb_prefix) + value;
    }

    public static String playMosaic(String src, int start, int num) {
        if (start <= 0 || start + num > src.length())
            throw new IllegalArgumentException("range is not in " + src + "\'s length");
        char[] chars = new char[num];
        for (int i = 0; i < num; i++) {
            chars[i] = '*';
        }
        String mosaic = new String(chars);
        return src.replace(src.substring(start, start + num), mosaic);
    }
}
