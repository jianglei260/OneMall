package com.myworld.onemall.search;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.SearchRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/7.
 */

public class SearchClearItemViewModel extends SearchItemViewModel {
    private SearchActivity activity;
    public ReplyCommand clearClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            clearHistory();
        }
    });

    public void clearHistory() {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                String userId = "";
                if (token != null)
                    userId = token.getUserId();
                SearchRepository.getRepository().clearSearch(userId);
                return true;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean bool) {
                activity.binding.getSearchViewModel().viewModels.clear();
            }
        });
    }

    public SearchClearItemViewModel(SearchActivity activity) {
        this.activity = activity;
    }
}
