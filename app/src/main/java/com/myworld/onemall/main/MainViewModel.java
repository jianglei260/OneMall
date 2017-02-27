package com.myworld.onemall.main;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseViewModel;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.base.Refreshable;
import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.search.SearchActivity;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/11/25.
 */

public class MainViewModel extends BaseViewModel implements Refreshable {
    private MainFragment fragment;
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
                case ListItemViewModel.VIEW_TYPE_ACTIVE:
                    itemView.set(BR.itemViewModel, R.layout.list_active);
                    break;
                case ListItemViewModel.VIEW_TYPE_GOODS_BANNER:
                    itemView.set(BR.itemViewModel, R.layout.list_goods_baner);
                    break;
            }

        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ObservableList<ListItemViewModel> viewModels = new ObservableArrayList<>();
    public ObservableField<String> hotText = new ObservableField<>();
    public ObservableBoolean white = new ObservableBoolean(true);
    public Refreshable refresh;
    public ObservableInt sortType = new ObservableInt(-1);
    public ObservableField<String> sortName = new ObservableField<>();
    private int loadNum = 0;
    private BannerItemViewModel bannerItemViewModel;

    public MainViewModel(MainFragment fragment) {
        this.fragment = fragment;
        refresh = this;
        initGoods();

    }

    public ReplyCommand searchClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(fragment.getActivity(), SearchActivity.class);
            fragment.startActivity(intent);
        }
    });
    public ReplyCommand sortClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            int type = sortType.get();
            switch (type) {
                case GoodsRepository.SORT_TYPE_PRICE_DOWN:
                    sortType.set(GoodsRepository.SORT_TYPE_SOLD_DOWN);
                    break;
                case GoodsRepository.SORT_TYPE_SOLD_DOWN:
                    sortType.set(GoodsRepository.SORT_TYPE_TIME_DOWN);
                    break;
                case GoodsRepository.SORT_TYPE_TIME_DOWN:
                    sortType.set(GoodsRepository.SORT_TYPE_PRICE_DOWN);
                    break;
            }
            updateSortName(sortType.get());
            initGoods();
        }
    });

    public void updateSortName(int type) {
        switch (type) {
            case GoodsRepository.SORT_TYPE_PRICE_DOWN:
                sortName.set(fragment.getString(R.string.goods_sort_price));
                break;
            case GoodsRepository.SORT_TYPE_SOLD_DOWN:
                sortName.set(fragment.getString(R.string.goods_sort_sold));
                break;
            case GoodsRepository.SORT_TYPE_TIME_DOWN:
                sortName.set(fragment.getString(R.string.goods_sort_time));
                break;
        }
    }

    public void initGoods() {
        loadding.set(true);
        loadNum = 0;
        updateSortName(sortType.get());
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                return GoodsRepository.getRepository().getGoods(1, 20, loadNum, sortType.get());
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                loadding.set(false);
                viewModels.clear();
                viewModels.add(new GoodsBannerViewModel());
                for (Goods goodse : goodses) {
                    ListItemViewModel itemViewModel = new GoodsItemViewModel(fragment.getActivity(), goodse);
                    viewModels.add(itemViewModel);
                }
                loadNum += goodses.size();
                if (goodses.size() < 20)
                    viewModels.add(new ListItemFinishViewModel());
                initBanner();
            }
        });
    }

    public void initBanner() {
        if (bannerItemViewModel != null && bannerItemViewModel.items.size() > 0) {
            viewModels.add(0, bannerItemViewModel);
            addActive();
            fragment.scrollToStart();
        } else {
            RxUtil.execute(new IOTask<List<BannerItem>>() {
                @Override
                public List<BannerItem> run() {
                    List<BannerItem> items = GoodsRepository.getRepository().listBanner();
                    return items;
                }
            }, new UIAction<List<BannerItem>>() {
                @Override
                public void onComplete(List<BannerItem> bannerItems) {
                    BannerItemViewModel bannerItemViewModel = new BannerItemViewModel(fragment.getActivity());
                    bannerItemViewModel.items.addAll(bannerItems);
                    MainViewModel.this.bannerItemViewModel = bannerItemViewModel;
                    viewModels.add(0, bannerItemViewModel);
                    addActive();
                    fragment.scrollToStart();
                }
            });
        }
    }

    public void addActive() {
        ActiveItemViewModel activeItemViewModel = new ActiveItemViewModel(fragment);
        viewModels.add(1, activeItemViewModel);
    }

    @Override
    public void onLoadMore(final OnComplete complete) {
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                return GoodsRepository.getRepository().getGoods(1, 20, loadNum, sortType.get());
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                for (Goods goodse : goodses) {
                    ListItemViewModel itemViewModel = new GoodsItemViewModel(fragment.getActivity(), goodse);
                    viewModels.add(itemViewModel);
                }
                loadNum += goodses.size();
                if (goodses.size() < 20) {
                    if (!(viewModels.get(viewModels.size() - 1) instanceof ListItemFinishViewModel))
                        viewModels.add(new ListItemFinishViewModel());
                }
                complete.onComplete();
            }
        });
    }

    @Override
    public void onRefresh(final OnComplete complete) {
        loadding.set(true);
        loadNum = 0;
        fragment.hideSearchBar();
        RxUtil.execute(new IOTask<List<Goods>>() {
            @Override
            public List<Goods> run() {
                return GoodsRepository.getRepository().getGoods(1, 20, loadNum, sortType.get());
            }
        }, new UIAction<List<Goods>>() {
            @Override
            public void onComplete(List<Goods> goodses) {
                loadding.set(false);
                viewModels.clear();
                viewModels.add(new GoodsBannerViewModel());
                for (Goods goodse : goodses) {
                    ListItemViewModel itemViewModel = new GoodsItemViewModel(fragment.getActivity(), goodse);
                    viewModels.add(itemViewModel);
                }
                initBanner();
                loadNum += goodses.size();
                complete.onComplete();
                fragment.showSearchBar();
            }
        });
    }
}
