package com.myworld.onemall.detail;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.utils.UserUtil;
import com.myworld.onemall.widget.CustomToast;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by jianglei on 2016/11/26.
 */

public class SelectViewModel {
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> image = new ObservableField<>();
    public ObservableField<String> number = new ObservableField<>("1");
    private DetailActivity activity;
    private int MAX_NUM = 200;
    private Goods goods;
    public ReplyCommand closeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            activity.hideView();
        }
    });
    public ReplyCommand addClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                int num = Integer.parseInt(number.get());
                number.set(num + 1 + "");
            } catch (NumberFormatException e) {
                number.set("1");
            }

        }
    });

    public ReplyCommand imageClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, ImageActivity.class);
            intent.putExtra(ImageActivity.GOODS_ID, goods.get_id());
            intent.putExtra(ImageActivity.IMG_INDEX, 0);
            activity.startActivity(intent);
        }
    });
    public ObservableBoolean haveSize = new ObservableBoolean(false);
    public ObservableList<SizeItemViewModel> viewModels = new ObservableArrayList<>();
    public final ItemViewSelector<SizeItemViewModel> itemView = new ItemViewSelector<SizeItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, SizeItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_size);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public ReplyCommand decClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                int num = Integer.parseInt(number.get());
                if (num > 1)
                    number.set(num - 1 + "");
            } catch (NumberFormatException e) {
                number.set("1");
            }
        }
    });

    public ReplyCommand addCartClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            addCart();
        }
    });

    public void addCart() {
        try {
            int num = Integer.parseInt(number.get());
            if (num > goods.getGoodsAmount()) {
                CustomToast.showFailed(activity, activity.getString(R.string.goods_not_enough));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                if (!UserRepository.getRepository().isLogin(activity)) {
                    UserUtil.login(activity);
                    return false;
                }
                int num = 1;
                try {
                    num = Integer.parseInt(number.get());
                } catch (Exception e) {
                    e.printStackTrace();
                    num = 1;
                }
                Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                boolean result = CartRepository.geRepository().add(token.getValue(), goods.get_id(), num);
                return result;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (success) {
                    CustomToast.showSuccess(activity, activity.getString(R.string.add_cart_success));
                    activity.updateCartNum(CartRepository.geRepository().getGoodsNum());
                    activity.hideView();
                }
            }
        });
    }

    public SelectViewModel(Goods goods,DetailActivity activity) {
        this.activity = activity;
        this.goods=goods;
        number.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                try {
                    int num = Integer.parseInt(number.get());
                    if (num > 200)
                        number.set("200");
                    if (num <= 0)
                        number.set("1");
                } catch (NumberFormatException e) {
                    number.set("1");
                }
            }
        });
        initGoods();
        initSizes();
    }

    public void initGoods() {
        image.set(GoodsRepository.getRepository().getFirstPicture(goods).getUrl());
        name.set(goods.getGoodsName());
        price.set(StringUtil.getRMBString(goods.getGoodsPrice()));
    }

    public void updateGoods(Goods goods) {
        if (this.goods != null && this.goods.get_id().equals(goods.get_id()))
            return;
        this.goods = goods;
        image.set(GoodsRepository.getRepository().getFirstPicture(goods).getUrl());
        price.set(StringUtil.getRMBString(goods.getGoodsPrice()));
    }

    public void clearSelected() {
        for (SizeItemViewModel viewModel : viewModels) {
            viewModel.selected.set(false);
        }
    }

    public void initSizes() {
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                return GoodsRepository.getRepository().getSize(goods.getGoodsName());
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                if (goodses.size() <= 0) {
                    haveSize.set(false);
                } else {
                    haveSize.set(true);
                }
                for (Goods goodse : goodses) {
                    SizeItemViewModel itemViewModel = new SizeItemViewModel(activity, goodse);
                    if (goodse.equals(SelectViewModel.this.goods))
                        itemViewModel.selected.set(true);
                    viewModels.add(itemViewModel);
                }
            }
        });
    }

}
