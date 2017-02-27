package com.myworld.onemall.order;

import android.content.Intent;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.detail.DetailActivity;
import com.myworld.onemall.utils.StringUtil;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/6.
 */

public class DetailGoodsItemViewModel {
    public ObservableField<String> number = new ObservableField<>("1");
    public ObservableField<String> image = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> size = new ObservableField<>();
    private Goods goods;
    private OrderDetailActivity activity;

    public ReplyCommand itemClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra(DetailActivity.GOODS_ID, goods.get_id());
            activity.startActivity(intent);
        }
    });

    public DetailGoodsItemViewModel(Goods goods, OrderDetailActivity activity) {
        this.goods = goods;
        this.activity = activity;
        initGoods();
    }

    public void initGoods() {
        number.set(goods.getNumber() + "");
        image.set(GoodsRepository.getRepository().getFirstPicture(goods).getUrl());
        price.set(goods.getGoodsPrice());
        name.set(goods.getGoodsName());
        size.set(goods.getGoodsSize());
    }
}
