package com.myworld.onemall.base;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.myworld.onemall.BR;
import com.myworld.onemall.R;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 2016/12/10.
 */

public class RefreshListViewModel extends BaseViewModel implements Refreshable {
    public Refreshable refresh;
    private int loadNum = 0;
    public final ItemViewSelector<ListItemViewModel> itemView = new ItemViewSelector<ListItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ListItemViewModel item) {
            switch (item.getViewType()) {
                case ListItemViewModel.VIEW_TYPE_NORMAL:
                    itemView.set(BR.goodsViewModel, R.layout.list_goods);
                    break;
                case ListItemViewModel.VIEW_TYPE_BANNER:
                    itemView.set(BR.itemViewModel, R.layout.list_banner);
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

    @Override
    public void onLoadMore(OnComplete complete) {

    }

    @Override
    public void onRefresh(OnComplete complete) {

    }
}
