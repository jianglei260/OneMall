package com.myworld.onemall.order;

import android.databinding.ObservableField;

import com.myworld.onemall.R;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.repository.GoodsRepository;

/**
 * Created by jianglei on 2016/12/5.
 */

public class OrderGoodsItemViewModel {
    public ObservableField<String> number = new ObservableField<>("1");
    public ObservableField<String> image = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> size = new ObservableField<>();
    public ObservableField<String> goodNumNotify=new ObservableField<>();
    private ConfirmOrderActivity activity;
    public Cart cart;


    public OrderGoodsItemViewModel(ConfirmOrderActivity activity, Cart cart) {
        this.activity = activity;
        this.cart = cart;
        init();
    }

    public void init() {
        number.set(cart.getGoodsNum() + "");
        Picture picture = GoodsRepository.getRepository().getFirstPicture(cart.getGoods());
        image.set(picture.getUrl());
        price.set(cart.getGoods().getGoodsPrice());
        name.set(cart.getGoods().getGoodsName());
        size.set(cart.getGoods().getGoodsSize());
        updateNumNotify();
    }
    public void updateNumNotify(){
        int num=cart.getGoods().getGoodsAmount();
        try {
            int cartNum=Integer.parseInt(number.get());
            if (num<=cartNum){
                goodNumNotify.set(activity.getString(R.string.goods_only_have_prefix)+num+activity.getString(R.string.goods_only_have_tail));
            }else {
                goodNumNotify.set("");
            }
            if (num<=0){
                goodNumNotify.set(activity.getString(R.string.goods_empty));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

