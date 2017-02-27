package com.myworld.onemall.detail;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.address.AddressListActivity;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.login.LoginActivity;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.utils.UserUtil;
import com.myworld.onemall.widget.CustomToast;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/11/25.
 */

public class DetailViewModel extends LoaddingViewModel {
    private DetailActivity activity;
    public ObservableList<BannerItem> images = new ObservableArrayList<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> size = new ObservableField<>();
    public ObservableBoolean haveGoods = new ObservableBoolean(true);
    private Goods goods;

    public ReplyCommand selectClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            activity.showView();
        }
    });

    public ReplyCommand addCartClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            addCart();
        }
    });

    public ReplyCommand addressClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            chooseAddress();
        }
    });

    public OnBannerClickListener listener = new OnBannerClickListener() {
        @Override
        public void OnBannerClick(int position) {
            Intent intent = new Intent(activity, ImageActivity.class);
            intent.putExtra(ImageActivity.GOODS_ID, goods.get_id());
            intent.putExtra(ImageActivity.IMG_INDEX, position - 1);
            activity.startActivity(intent);
        }
    };

    public void addCart() {
        if (!haveGoods.get()) {
            CustomToast.showFailed(activity, activity.getString(R.string.goods_empty));
            return;
        }
        loadding.set(true);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                if (!UserRepository.getRepository().isLogin(activity)) {
                    UserUtil.login(activity);
                    return false;
                }
                Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                return CartRepository.geRepository().add(token.getValue(), goods.get_id(), 1);
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                loadding.set(false);
                if (success) {
                    CustomToast.showSuccess(activity, activity.getString(R.string.add_cart_success));
                    activity.updateCartNum(CartRepository.geRepository().getGoodsNum());
                }
            }
        });
    }

    public void chooseAddress() {
        loadding.set(true);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                return UserRepository.getRepository().isLogin(activity);
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean login) {
                loadding.set(false);
                if (login) {
                    activity.chooseAddress();
                } else {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    public void onAddressSelect(Address addr) {
        if (addr != null)
            address.set(addr.getAddress());
    }


    public void initAddress() {
        loadding.set(true);
        RxUtil.execute(new IOTask<Address>() {
            @Override
            public Address run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    Address address = AddressRepository.getRepository().getDefaultAddress(token.getValue());
                    return address;
                }
                return null;
            }
        }, new UIAction<Address>() {
            @Override
            public void onComplete(Address addr) {
                loadding.set(false);
                if (addr != null) {
                    address.set(addr.getAddress());
                } else {
                    address.set(activity.getString(R.string.address_cuit));
                }
            }
        });
    }


    public DetailViewModel(String goodsId, DetailActivity activity) {
        this.activity = activity;
        initGoods(goodsId);
        initAddress();
    }

    public void initGoods(final String goodsId) {
        RxUtil.execute(new IOTask<Goods>() {
            @Override
            public Goods run() {
                return GoodsRepository.getRepository().goodsDetail(goodsId);
            }
        }, new UIAction<Goods>() {
            @Override
            public void onComplete(Goods goods) {
                if (goods == null) {
                    CustomToast.showFailed(activity, activity.getString(R.string.goods_not_find));
                    return;
                }
                DetailViewModel.this.goods = goods;
                activity.initSelectViewModel(goods);
                name.set(goods.getGoodsName());
                price.set(StringUtil.getRMBString(goods.getGoodsPrice()));
                List<Picture> pictures = GoodsRepository.getRepository().getGoodsPics(goods);
                for (Picture picture : pictures) {
                    BannerItem item = new BannerItem();
                    item.setPic(picture.getUrl());
                    images.add(item);
                }
                haveGoods.set(goods.getGoodsAmount() > 0);
                size.set(goods.getGoodsSize());
            }
        });

    }

    public void upateGoods(Goods goods) {
        if (this.goods.get_id().equals(goods.get_id()))
            return;
        this.goods = goods;
        name.set(goods.getGoodsName());
        size.set(goods.getGoodsSize());
        price.set(StringUtil.getRMBString(goods.getGoodsPrice()));
        List<Picture> pictures = GoodsRepository.getRepository().getGoodsPics(goods);
        haveGoods.set(goods.getGoodsAmount() > 0);
        images.clear();
        for (Picture picture : pictures) {
            BannerItem item = new BannerItem();
            item.setPic(picture.getUrl());
            images.add(item);
        }
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
