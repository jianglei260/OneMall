package com.myworld.onemall.main;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.myworld.onemall.R;
import com.myworld.onemall.base.ListItemViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

import com.myworld.onemall.BR;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

/**
 * Created by jianglei on 2016/12/20.
 */

public class ActiveItemViewModel extends ListItemViewModel {
    private MainFragment fragment;
    private String keywords;

    public ItemViewSelector<GoodsItemViewModel> itemView = new ItemViewSelector<GoodsItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, GoodsItemViewModel item) {
            itemView.set(BR.goodsViewModel, R.layout.list_active_item);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public ObservableList<GoodsItemViewModel> viewModels = new ObservableArrayList<>();
    public ObservableField<String> title = new ObservableField<>();

    @Override
    public int getViewType() {
        return VIEW_TYPE_ACTIVE;
    }

    public ActiveItemViewModel(MainFragment fragment) {
        this.fragment = fragment;
        keywords = fragment.getString(R.string.apple);
        title.set(fragment.getString(R.string.active_title));
        initGoodsList();
    }

    public void initGoodsList() {
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                return GoodsRepository.getRepository().searchGoods(keywords,10,0);
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                viewModels.clear();
                if (goodses!=null&&goodses.size()>0){
                    for (Goods goodse : goodses) {
                        GoodsItemViewModel itemViewModel=new GoodsItemViewModel(fragment.getActivity(),goodse);
                        viewModels.add(itemViewModel);
                    }
                }else {
                    fragment.binding.getMainViewModel().viewModels.remove(ActiveItemViewModel.this);
                }
            }
        });
    }
}
