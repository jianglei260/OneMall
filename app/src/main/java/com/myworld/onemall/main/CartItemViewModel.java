package com.myworld.onemall.main;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/1.
 */

public class CartItemViewModel {
    public ObservableField<String> number = new ObservableField<>("1");
    public ObservableField<String> image = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> size = new ObservableField<>();
    public ObservableField<String> goodNumNotify=new ObservableField<>();
    public ObservableBoolean checked = new ObservableBoolean();
    private String oldNumber;
    public Cart cart;
    private CartFragment fragment;
    public ReplyCommand addClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                oldNumber = number.get();
                int num = Integer.parseInt(number.get());
                if (num < 200) {
                    number.set(num + 1 + "");
                    fragment.fullLayoutBinding.getCartFullViewModel().updateChecked();
                    changeNumber(1);
                }
            } catch (NumberFormatException e) {
                number.set("1");
            }

        }
    });

    public ReplyCommand decClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                oldNumber = number.get();
                int num = Integer.parseInt(number.get());
                if (num > 1) {
                    number.set(num - 1 + "");
                    fragment.fullLayoutBinding.getCartFullViewModel().updateChecked();
                    changeNumber(-1);
                }
            } catch (NumberFormatException e) {
                number.set("1");
            }
        }
    });

    public ReplyCommand numberClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            try {
                fragment.showNumberPicker(Integer.parseInt(number.get()));
            } catch (NumberFormatException e) {
                fragment.showNumberPicker(1);
            }
            if (fragment.pickBinding != null)
                fragment.pickBinding.getPickViewModel().setListenner(new NumberPickerViewModel.SelectedListenner() {
                    @Override
                    public void selected(boolean positive) {
                        if (positive) {
                            String numText = fragment.pickBinding.getPickViewModel().number.get();
                            number.set(numText);
                            fragment.fullLayoutBinding.getCartFullViewModel().updateChecked();
                            try {
                                int num = Integer.parseInt(numText);
                                editNumber(num);
                            } catch (NumberFormatException e) {

                            }
                        }

                    }
                });
        }
    });

    public void changeNumber(final int num) {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(fragment.getActivity());
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(fragment.getActivity());
                    boolean result = CartRepository.geRepository().change(token.getValue(), cart.get_id(), num);
                    return result;
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (!success) {
                    CustomToast.showSuccess(fragment.getActivity(), fragment.getString(R.string.cart_add_failed));
                    number.set(oldNumber);
                }
            }
        });
    }

    public void editNumber(final int num) {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(fragment.getActivity());
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(fragment.getActivity());
                    boolean result = CartRepository.geRepository().edit(token.getValue(), cart.get_id(), num);
                    return result;
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (!success) {
                    CustomToast.showSuccess(fragment.getActivity(), fragment.getString(R.string.cart_add_failed));
                    number.set(oldNumber);
                }
            }
        });
    }

    public CartItemViewModel(Cart cart, final CartFragment fragment) {
        this.cart = cart;
        this.fragment = fragment;

        number.set(Integer.toString(cart.getGoodsNum()));
        Picture picture = GoodsRepository.getRepository().getFirstPicture(cart.getGoods());
        image.set(picture.getUrl());
        price.set(StringUtil.getRMBString(cart.getGoods().getGoodsPrice()));
        name.set(cart.getGoods().getGoodsName());
        size.set(cart.getGoods().getGoodsSize());
        checked.set(cart.getStatus() == 1 ? true : false);

        checked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                fragment.fullLayoutBinding.getCartFullViewModel().fromUI = false;
                fragment.fullLayoutBinding.getCartFullViewModel().updateChecked();
                fragment.fullLayoutBinding.getCartFullViewModel().fromUI = true;
                checkItem(checked.get() ? 1 : 0);
            }
        });
        updateNumNotify();
        number.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                updateNumNotify();
            }
        });
    }
    public void updateNumNotify(){
        int num=cart.getGoods().getGoodsAmount();
        try {
            int cartNum=Integer.parseInt(number.get());
            if (num<=cartNum){
                goodNumNotify.set(fragment.getString(R.string.goods_only_have_prefix)+num+fragment.getString(R.string.goods_only_have_tail));
            }else {
                goodNumNotify.set("");
            }
            if (num<=0){
                goodNumNotify.set(fragment.getString(R.string.goods_empty));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkItem(final int status) {
        fragment.showLoadding();
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(fragment.getActivity());
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(fragment.getActivity());
                    return CartRepository.geRepository().check(token.getValue(), cart.get_id(), status);
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                fragment.hideLoadding();
                if (!success) {

                }
            }
        });
    }
}
