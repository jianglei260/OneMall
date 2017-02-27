package com.myworld.onemall.search;

import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseViewModel;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.main.GoodsItemViewModel;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/8.
 */

public class GoodsListViewModel extends LoaddingViewModel {
    private GoodsListActivity activity;
    private String keywords;
    public ObservableBoolean linearMode = new ObservableBoolean(true);
    public final ItemViewSelector<ListItemViewModel> itemView = new ItemViewSelector<ListItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ListItemViewModel item) {
            if (linearMode.get()) {
                itemView.set(BR.goodsViewModel, R.layout.list_goods_linear);
            } else {
                itemView.set(BR.goodsViewModel, R.layout.list_goods);
            }
        }

        @Override
        public int viewTypeCount() {
            return 2;
        }
    };
    public ObservableField<String> searchText = new ObservableField<>();
    public ObservableList<ListItemViewModel> viewModels = new ObservableArrayList<>();
    public ReplyCommand searchClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, SearchActivity.class);
            activity.startActivity(intent);
        }
    });


    public GoodsListViewModel(final GoodsListActivity activity, String keywords) {
        this.activity = activity;
        this.keywords = keywords;
        searchText.set(keywords);
        linearMode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                activity.changeLayout(linearMode.get());
            }
        });
        initSearchList();
    }

    public void updateSearch(String keywords) {
        this.keywords = keywords;
        searchText.set(keywords);
        initSearchList();
    }

    public void initSearchList() {
        loadding.set(true);
        emptyContent.set(false);
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                return GoodsRepository.getRepository().searchGoods(keywords, 20, 0);
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                loadding.set(false);
                activity.changeLayout(linearMode.get());
                viewModels.clear();
                for (Goods goodse : goodses) {
                    ListItemViewModel itemViewModel;
                    itemViewModel = new GoodsItemViewModel(activity, goodse);
                    viewModels.add(itemViewModel);
                }
                if (goodses.size() <= 0) {
                    emptyContent.set(true);
                }
            }
        });
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
