package com.myworld.onemall.base;

/**
 * Created by jianglei on 2016/12/10.
 */

public interface Refreshable {
    public void onLoadMore(OnComplete complete);

    public void onRefresh(OnComplete complete);

    public interface OnComplete{
        public void onComplete();
    }
}
