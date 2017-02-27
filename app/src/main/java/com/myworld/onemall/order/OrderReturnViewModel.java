package com.myworld.onemall.order;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/6.
 */

public class OrderReturnViewModel extends LoaddingViewModel{
    private OrderReturnActivity activity;
    private String orderId;
    public ObservableField<String> reason = new ObservableField<>();
    public ReplyCommand applyClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(reason.get())) {
                CustomToast.showFailed(activity, activity.getString(R.string.order_return_reason));
            } else {
                back();
            }
        }
    });

    public void back() {
        loadding.set(true);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return OrderRepository.geRepository().orderReturn(token.getValue(), orderId, reason.get());
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                loadding.set(false);
                if (success) {
                    CustomToast.showSuccess(activity, activity.getString(R.string.order_return_success));
                    activity.finish();
                } else {
                    CustomToast.showSuccess(activity, activity.getString(R.string.order_return_failed));
                }
            }
        });
    }

    public OrderReturnViewModel(OrderReturnActivity activity, String orderId) {
        this.activity = activity;
        this.orderId = orderId;
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
