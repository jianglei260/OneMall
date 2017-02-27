package com.myworld.onemall.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.base.OneMallApplication;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.EncryptUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by jianglei on 2016/11/27.
 */

public class LoginViewModel extends LoaddingViewModel {
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> passWord = new ObservableField<>();
    public ObservableBoolean passwdVisible = new ObservableBoolean(false);
    public ReplyCommand nameClear = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            userName.set("");
        }
    });
    public ReplyCommand passwdClear = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            passWord.set("");
        }
    });
    public ReplyCommand resgist = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, RegistActivity.class);
            context.startActivity(intent);
        }
    });
    public ReplyCommand login = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            doLogin();
        }
    });
    public ReplyCommand findPdClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, FounPdActivity.class);
            context.startActivity(intent);
        }
    });

    public ObservableBoolean loginClickable = new ObservableBoolean(false);

    private LoginActivity context;

    public LoginViewModel(final LoginActivity context) {
        this.context = context;
        passwdVisible.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                context.setPasswdVisible(passwdVisible.get());
            }
        });
        passWord.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                if (TextUtils.isEmpty(passWord.get()))
                    return;
                if (TextUtils.isEmpty(userName.get()))
                    return;
                if (passWord.get().length() >= 8 && userName.get().length() == 11)
                    loginClickable.set(true);
            }
        });
    }

    public void doLogin() {
        loadding.set(true);
        Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                try {
                    Token token = UserRepository.getRepository().login(userName.get(), EncryptUtil.getInstance().encrypt(passWord.get()), OneMallApplication.getInstance().getDeviceToken());
                    subscriber.onNext(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Token>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Token token) {
                loadding.set(false);
                if (token != null && !TextUtils.isEmpty(token.getValue())) {
                    context.onUserLogin();
                }
            }
        });
    }

    @Override
    public void onBack() {
        context.finish();
    }
}
