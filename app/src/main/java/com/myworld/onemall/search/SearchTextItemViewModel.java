package com.myworld.onemall.search;

import android.content.Intent;
import android.databinding.ObservableField;

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

public class SearchTextItemViewModel extends SearchItemViewModel {
    public ObservableField<String> text = new ObservableField<>();
    private SearchActivity activity;
    public ReplyCommand itemClcik = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, GoodsListActivity.class);
            intent.putExtra(GoodsListActivity.KEY_WORDS, text.get());
            activity.startActivity(intent);
            RxUtil.execute(new IOTask<Boolean>() {
                @Override
                public Boolean run() {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    String userId = "";
                    if (token != null)
                        userId = token.getUserId();
                    try {
                        SearchRepository.getRepository().saveSearchWords(userId, text.get());
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
    });

    public SearchTextItemViewModel(SearchActivity activity) {
        this.activity = activity;
    }
}
