package com.myworld.onemall.search;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseViewModel;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Search;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.SearchRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/7.
 */

public class SearchViewModel extends LoaddingViewModel {
    public ItemViewSelector<SearchItemViewModel> itemView = new ItemViewSelector<SearchItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, SearchItemViewModel item) {
            if (item instanceof SearchClearItemViewModel)
                itemView.set(BR.itemViewModel, R.layout.list_search_clear);
            else if (item instanceof SearchTextItemViewModel)
                itemView.set(BR.itemViewModel, R.layout.list_search_item);
        }

        @Override
        public int viewTypeCount() {
            return 2;
        }
    };
    public ObservableField<String> searchText = new ObservableField<>();
    public ObservableList<SearchItemViewModel> viewModels = new ObservableArrayList<>();
    public ReplyCommand clearClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            searchText.set("");
        }
    });
    private SearchActivity activity;
    public ReplyCommand searchClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!TextUtils.isEmpty(searchText.get()))
                search();
        }
    });

    public void search() {
        Intent intent = new Intent(activity, GoodsListActivity.class);
        intent.putExtra(GoodsListActivity.KEY_WORDS, searchText.get());
        activity.startActivity(intent);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                String userId = "";
                if (token != null)
                    userId = token.getUserId();
                try {
                    SearchRepository.getRepository().saveSearchWords(userId, searchText.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean aBoolean) {
                activity.finish();
            }
        });

    }

    public SearchViewModel(SearchActivity activity) {
        this.activity = activity;
        initSearchList();
    }

    public void initSearchList() {
        RxUtil.execute(new IOTask<List<Search>>() {
            @Override
            public List<Search> run() {
                Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                String userId = "";
                if (token != null)
                    userId = token.getUserId();
                return SearchRepository.getRepository().searchHistory(userId);
            }
        }, new UIAction<List<Search>>() {
            @Override
            public void onComplete(List<Search> searches) {
                viewModels.clear();
                for (Search search : searches) {
                    SearchTextItemViewModel searchTextItemViewModel = new SearchTextItemViewModel(activity);
                    searchTextItemViewModel.text.set(search.getWords());
                    viewModels.add(searchTextItemViewModel);
                }
                if (searches.size() > 0) {
                    SearchClearItemViewModel clearItemViewModel = new SearchClearItemViewModel(activity);
                    viewModels.add(clearItemViewModel);
                }
            }
        });
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
