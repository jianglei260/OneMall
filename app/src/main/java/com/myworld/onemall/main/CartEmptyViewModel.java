package com.myworld.onemall.main;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.base.Refreshable;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 2016/11/30.
 */

public class CartEmptyViewModel implements Refreshable {
    public final ItemViewSelector<ListItemViewModel> itemView = new ItemViewSelector<ListItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ListItemViewModel item) {
            switch (item.getViewType()) {
                case ListItemViewModel.VIEW_TYPE_NORMAL:
                    itemView.set(BR.goodsViewModel, R.layout.list_goods);
                    break;
                case ListItemViewModel.VIEW_TYPE_LINEAR:
                    itemView.set(BR.goodsViewModel, R.layout.cart_empty_suggestion);
                    break;
                case ListItemViewModel.VIEW_TYPE_LOAD_FINISH:
                    itemView.set(BR.itemViewModel, R.layout.list_no_more);
                    break;
            }

        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ObservableList<ListItemViewModel> viewModels = new ObservableArrayList<>();
    public Refreshable refresh;
    private CartFragment fragment;

    public CartEmptyViewModel(CartFragment fragment) {
        refresh = this;
        this.fragment = fragment;
    }

    @Override
    public void onLoadMore(OnComplete complete) {
        fragment.cartBinding.getCartViewModel().onLoadMore(complete);
    }

    @Override
    public void onRefresh(OnComplete complete) {
        fragment.cartBinding.getCartViewModel().onRefresh(complete);
    }
}
