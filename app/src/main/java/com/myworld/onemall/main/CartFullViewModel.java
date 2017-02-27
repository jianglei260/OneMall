package com.myworld.onemall.main;

import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.Refreshable;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 2016/11/30.
 */

public class CartFullViewModel implements Refreshable {
    public final ItemViewSelector<CartItemViewModel> itemView = new ItemViewSelector<CartItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, CartItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_cart);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ObservableList<CartItemViewModel> viewModels = new ObservableArrayList<>();
    private CartViewModel cartViewModel;
    private CartFragment fragment;
    public boolean fromUI = true;

    public CartFullViewModel(CartFragment fragment) {
        this.fragment = fragment;
        refresh = this;
        cartViewModel = fragment.cartBinding.getCartViewModel();
        cartViewModel.allCheck.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (cartViewModel.allCheck.get()) {
                    if (fromUI)
                        checkAll();
                } else {
                    if (fromUI)
                        unCheckAll();
                }
            }
        });
    }

    public void deleteChecked() {
        for (CartItemViewModel viewModel : viewModels) {
            if (viewModel.checked.get()) {
                deleteCart(viewModel);
            }
        }
    }

    public void deleteCart(final CartItemViewModel itemViewModel) {
        RxUtil.execute(new IOTask<Boolean>() {
                           @Override
                           public Boolean run() {
                               boolean login = UserRepository.getRepository().isLogin(fragment.getActivity());
                               if (login) {
                                   Token token = UserRepository.getRepository().getCurrentUserToken(fragment.getActivity());
                                   boolean result = CartRepository.geRepository().delete(token.getValue(), itemViewModel.cart.get_id());
                                   return result;
                               }
                               return false;
                           }
                       }
                , new UIAction<Boolean>() {
                    @Override
                    public void onComplete(Boolean aBoolean) {
                        if (!aBoolean) {
                            CustomToast.showSuccess(fragment.getActivity(), fragment.getString(R.string.cart_delete_failed));
                        } else {
                            viewModels.remove(itemViewModel);
                        }

                    }
                }

        );
    }

    public void updateChecked() {
        int totalNum = 0;
        float totalPrice = 0;
        int checkedNum = 0;
        for (CartItemViewModel viewModel : viewModels) {
            if (viewModel.checked.get()) {
                checkedNum++;
                try {
                    float price = Float.parseFloat(viewModel.cart.getGoods().getGoodsPrice());
                    int num = Integer.parseInt(viewModel.number.get());
                    totalNum += num;
                    totalPrice += price * num;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (checkedNum == viewModels.size())
            cartViewModel.allCheck.set(true);
        else
            cartViewModel.allCheck.set(false);
        cartViewModel.totalNum.set(totalNum);
        cartViewModel.totalPrice.set(totalPrice);
    }

    public void checkAll() {
        for (CartItemViewModel viewModel : viewModels) {
            viewModel.checked.set(true);
        }
    }

    public void unCheckAll() {
        for (CartItemViewModel viewModel : viewModels) {
            viewModel.checked.set(false);
        }
    }

    public Refreshable refresh;

    @Override
    public void onLoadMore(OnComplete complete) {
        fragment.cartBinding.getCartViewModel().onLoadMore(complete);
    }

    @Override
    public void onRefresh(OnComplete complete) {
        fragment.cartBinding.getCartViewModel().onRefresh(complete);
    }
}
