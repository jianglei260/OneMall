package com.myworld.onemall.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.detail.DetailActivity;
import com.myworld.onemall.utils.StringUtil;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/11/25.
 */

public class GoodsItemViewModel extends ListItemViewModel {
    private Context context;
    private Goods goods;
    private static int MIN_NUM_THRSEHOLD = 3;
    public ObservableField<String> image = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ReplyCommand itemClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.GOODS_ID, goods.get_id());
            context.startActivity(intent);
        }
    });
    public ObservableField<String> oriPrice = new ObservableField<>();
    public ObservableField<String> numNotify = new ObservableField();
    public ObservableInt buyNumber = new ObservableInt();
    public ObservableBoolean strike = new ObservableBoolean(true);

    @Override
    public int getViewType() {
        return VIEW_TYPE_NORMAL;
    }

    public GoodsItemViewModel(Context context, Goods goods) {
        this.context = context;
        this.goods = goods;
        List<Picture> pictures = GoodsRepository.getRepository().getGoodsPics(goods);
        if (pictures.size() > 0)
            image.set(pictures.get(0).getUrl());
        name.set(goods.getGoodsName());
        price.set("¥" + goods.getGoodsPrice());
        buyNumber.set(goods.getBuyNum());
        oriPrice.set(StringUtil.getRMBString(goods.getOriPrice()));
        if (goods.getGoodsAmount() <= MIN_NUM_THRSEHOLD) {
            if (goods.getGoodsAmount() > 0) {
                numNotify.set(context.getString(R.string.goods_only_have_prefix) + goods.getGoodsAmount() + context.getString(R.string.goods_only_have_tail));
            } else {
                numNotify.set(context.getString(R.string.goods_empty));
            }
        }
    }
}
