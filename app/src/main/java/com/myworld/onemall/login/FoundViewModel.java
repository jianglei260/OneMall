package com.myworld.onemall.login;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.base.OneMallApplication;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.SMSRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.EncryptUtil;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by jianglei on 2016/12/9.
 */

public class FoundViewModel extends LoaddingViewModel{
    public ObservableField<String> phoneNumber = new ObservableField<>();
    public ObservableField<String> passwd = new ObservableField<>("");
    public ObservableField<String> smsCode = new ObservableField<>("");
    private FounPdActivity activity;
    public ReplyCommand getCode = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (checkPhoneNumber()) {
                getSmsCode(phoneNumber.get());
                activity.showTimer();
            }
        }
    });
    public ReplyCommand regsite = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (checkPhoneNumber() && checkPasswd() && checkSMSCode()) {
                findPD();
            }

        }
    });

    public ObservableBoolean getCodeClickable = new ObservableBoolean(true);
    public void getSmsCode(final String phone) {
        loadding.set(true);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = manager.getDeviceId();
                return UserRepository.getRepository().getSmsCode(deviceId, phone);
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                loadding.set(false);
                if (success) {
                    Toast.makeText(activity, activity.getString(R.string.sended_sms_code), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void findPD() {
        loadding.set(true);
        Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                subscriber.onNext(UserRepository.getRepository().findPD(phoneNumber.get(), EncryptUtil.getInstance().encrypt(passwd.get()), smsCode.get(),
                        OneMallApplication.getInstance().getDeviceToken()));
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
                if (token != null && !TextUtils.isEmpty(token.getUserId())) {
                    CustomToast.showSuccess(activity,activity.getString(R.string.find_pd_success));
                    activity.onUserLogin();
                    activity.finish();
                } else {
                    CustomToast.showFailed(activity, activity.getString(R.string.find_pd_error));
                }
            }
        });
    }

    public FoundViewModel(FounPdActivity activity) {
        this.activity = activity;
    }

    public boolean checkPhoneNumber() {
        if (phoneNumber.get() != null && phoneNumber.get().length() == 11)
            return true;
        activity.showWrongPhoneNumber();
        return false;
    }

    public boolean checkPasswd() {
        if (passwd.get() != null && passwd.get().length() >= 8)
            return true;
        activity.showPasswdTooShort();
        return false;
    }

    public boolean checkSMSCode() {
        if (smsCode.get() != null && smsCode.get().length() >= 4)
            return true;
        Toast.makeText(activity, activity.getString(R.string.error_sms_code), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
