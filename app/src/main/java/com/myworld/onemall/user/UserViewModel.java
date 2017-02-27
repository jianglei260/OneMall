package com.myworld.onemall.user;

import android.content.Intent;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.address.AddressListActivity;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/4.
 */

public class UserViewModel extends LoaddingViewModel {
    public ObservableField<String> userImage = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> userNick = new ObservableField<>();
    private UserActivity activity;

    public ReplyCommand userClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, UserInfoEditActivity.class);
            activity.startActivity(intent);
        }
    });

    public ReplyCommand addressClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, AddressListActivity.class);
            activity.startActivity(intent);
        }
    });

    public UserViewModel(UserActivity activity) {
        this.activity = activity;
        initUser();
    }

    public void initUser() {
        RxUtil.execute(new IOTask<Token>() {
            @Override
            public Token run() {
                boolean isLogin = UserRepository.getRepository().isLogin(activity);
                if (isLogin) {
                    return UserRepository.getRepository().getCurrentUserToken(activity);
                }
                return null;
            }
        }, new UIAction<Token>() {
            @Override
            public void onComplete(Token token) {
                if (token != null) {
                    userName.set("om_" + token.getUserId().substring(0, 16));
                    userNick.set(token.getPhone());
                    userImage.set("res:///"+ R.drawable.ic_onemall_splash);
                }
            }
        });
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
