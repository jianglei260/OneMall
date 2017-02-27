package com.myworld.onemall.main;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.base.Refreshable;
import com.myworld.onemall.cart.CartActivity;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.login.LoginActivity;
import com.myworld.onemall.order.ConfirmOrderActivity;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/11/30.
 */

public class CartViewModel extends LoaddingViewModel implements Refreshable {
    private CartFragment fragment;
    public ObservableBoolean loadding = new ObservableBoolean(false);
    public ObservableBoolean isLogin = new ObservableBoolean(false);
    public ObservableBoolean allCheck = new ObservableBoolean(false);
    public ObservableBoolean isFull = new ObservableBoolean(false);
    private static final String TAG = "CartViewModel";
    public ObservableFloat totalPrice = new ObservableFloat();
    public ObservableInt totalNum = new ObservableInt(0);
    public Refreshable refresh;
    public ObservableBoolean editMode = new ObservableBoolean(false);
    public ObservableBoolean backVisible = new ObservableBoolean(false);

    public ReplyCommand editClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (editMode.get()) {
                editMode.set(false);
                initCart();
            } else {
                editMode.set(true);
            }
        }
    });

    public ReplyCommand deleteClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.fullLayoutBinding.getCartFullViewModel().deleteChecked();
        }
    });

    public ReplyCommand addOrderClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            int checkedNum = CartRepository.geRepository().getChecked().size();
            if (checkedNum > 0) {
                Intent intent = new Intent(fragment.getActivity(), ConfirmOrderActivity.class);
                fragment.startActivity(intent);
            } else {
                Toast.makeText(fragment.getActivity(), fragment.getString(R.string.not_check_goods), Toast.LENGTH_SHORT).show();
            }
        }
    });


    public ReplyCommand loginClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
            fragment.startActivity(intent);
        }
    });

    public CartViewModel(CartFragment fragment) {
        this.fragment = fragment;
        refresh = this;
        if (fragment.getActivity() instanceof CartActivity)
            backVisible.set(true);
        initCart();
    }

    public void onUserLogout() {
        initCart();
    }


    public void initCart() {
        loadding.set(true);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                return UserRepository.getRepository().isLogin(fragment.getActivity());
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean aBoolean) {
                isLogin.set(aBoolean);
            }
        });

        RxUtil.execute(new IOTask<List<Cart>>() {
            @Override
            public List<Cart> run() {
                boolean login = UserRepository.getRepository().isLogin(fragment.getActivity());
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(fragment.getActivity());
                    List<Cart> carts = CartRepository.geRepository().getCarts(token.getValue());
                    return carts;
                }
                return null;
            }
        }, new UIAction<List<Cart>>() {
            @Override
            public void onComplete(List<Cart> carts) {
                loadding.set(false);
                if (carts != null && carts.size() > 0) {
                    isFull.set(true);
                    fragment.showFullLayout();
                    showCarts(carts);
                } else {
                    isFull.set(false);
                    fragment.showEmptyLayout();
                    showEmpty();
                }
            }
        });
    }

    public void showEmpty() {
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                List<Goods> goodses = GoodsRepository.getRepository().getGoods(1, 20, 0, -1);
                return goodses;
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                CartEmptyViewModel viewModel = fragment.emptyLayoutBinding.getCartViewModel();
                viewModel.viewModels.clear();
                viewModel.viewModels.add(0, new CartEmptyItemViewModel());
                for (Goods goodse : goodses) {
                    ListItemViewModel itemViewModel = new GoodsItemViewModel(fragment.getActivity(), goodse);
                    viewModel.viewModels.add(itemViewModel);
                }
                viewModel.viewModels.add(new ListItemFinishViewModel());
            }
        });
    }

    public void showCarts(List<Cart> carts) {
        CartFullViewModel viewModel = fragment.fullLayoutBinding.getCartFullViewModel();
        viewModel.viewModels.clear();
        for (Cart cart : carts) {
            CartItemViewModel itemViewModel = new CartItemViewModel(cart, fragment);
            viewModel.viewModels.add(itemViewModel);
        }
        viewModel.updateChecked();
    }

    @Override
    public void onLoadMore(final OnComplete complete) {
        complete.onComplete();
    }

    @Override
    public void onRefresh(final OnComplete complete) {
        initCart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                complete.onComplete();
            }
        }, 1500);
    }

    @Override
    public void onBack() {
        if (fragment.getActivity() instanceof CartActivity) {
            fragment.getActivity().finish();
        }
    }
}
