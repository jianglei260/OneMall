package com.myworld.onemall.adapter;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by jianglei on 2016/12/9.
 */

public class TextViewAdapter {
    @SuppressWarnings("unchecked")
    @BindingAdapter({"android:paintFlags"})
    public static  void setStrike(TextView textView,int flag){
            textView.setPaintFlags(flag);

    }
}
