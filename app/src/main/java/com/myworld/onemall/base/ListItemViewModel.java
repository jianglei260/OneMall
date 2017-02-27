package com.myworld.onemall.base;

import android.databinding.ObservableInt;

/**
 * Created by jianglei on 2016/11/25.
 */

public abstract class ListItemViewModel {
    public ObservableInt viewType = new ObservableInt();
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_LOAD_MORE = 1;
    public static final int VIEW_TYPE_LOAD_FINISH = 2;
    public static final int VIEW_TYPE_BANNER = 3;
    public static final int VIEW_TYPE_LINEAR= 4;
    public static final int VIEW_TYPE_ACTIVE= 5;
    public static final int VIEW_TYPE_GOODS_BANNER= 6;


    public abstract int getViewType();
}
