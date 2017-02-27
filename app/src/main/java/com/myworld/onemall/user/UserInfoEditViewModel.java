package com.myworld.onemall.user;

import android.databinding.ObservableField;

import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

/**
 * Created by jianglei on 2016/12/4.
 */

public class UserInfoEditViewModel extends LoaddingViewModel {
    public ObservableField<String> userImage = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> userNick = new ObservableField<>();
    private UserInfoEditActivity activity;

    public UserInfoEditViewModel(UserInfoEditActivity activity) {
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
