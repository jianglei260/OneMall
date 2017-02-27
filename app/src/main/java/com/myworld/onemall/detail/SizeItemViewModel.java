package com.myworld.onemall.detail;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.data.entity.Goods;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/8.
 */

public class SizeItemViewModel {
    public ObservableField<String> sizeText = new ObservableField<>();
    private Goods goods;
    public ReplyCommand sizeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            activity.selectBinding.getSelectlViewModel().clearSelected();
            selected.set(true);
            activity.upateGoods(goods);
        }
    });
    public ObservableBoolean selected = new ObservableBoolean(false);
    private DetailActivity activity;

    public SizeItemViewModel(DetailActivity activity,Goods goods) {
        this.activity = activity;
        this.goods=goods;
        initSize();
    }

    public void initSize(){
        sizeText.set(goods.getGoodsSize());
    }
}
