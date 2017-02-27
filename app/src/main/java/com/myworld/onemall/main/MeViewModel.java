package com.myworld.onemall.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.back.BackListActivity;
import com.myworld.onemall.base.Config;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.login.LoginActivity;
import com.myworld.onemall.order.OrderListActivity;
import com.myworld.onemall.settings.SettingActivity;
import com.myworld.onemall.user.UserActivity;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.web.WebActivity;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/11/27.
 */

public class MeViewModel extends LoaddingViewModel {
    private Context context;
    public ObservableBoolean login = new ObservableBoolean();
    public ObservableField<String> userImage = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> recevingNum = new ObservableField<>();
    public ObservableField<String> payingNum = new ObservableField<>();
    public ObservableField<String> defaultHead = new ObservableField<>();
    public ObservableField<String> loginHead = new ObservableField<>();

    public ReplyCommand userHeadClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!login.get()) {
                gotoLogin();
            } else {
                gotoUser();
            }
        }
    });

    public ReplyCommand settingClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, SettingActivity.class);
            context.startActivity(intent);
        }
    });

    public ReplyCommand receiveClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!login.get()) {
                gotoLogin();
                return;
            }
            Intent intent = new Intent(context, OrderListActivity.class);
            intent.putExtra(OrderListActivity.ORDER_STATUS, OrderRepository.ORDER_STATUS_RECEIVEING);
            context.startActivity(intent);
        }
    });

    public ReplyCommand orderClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!login.get()) {
                gotoLogin();
                return;
            }
            Intent intent = new Intent(context, OrderListActivity.class);
            context.startActivity(intent);
        }
    });

    public ReplyCommand payClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!login.get()) {
                gotoLogin();
                return;
            }
            Intent intent = new Intent(context, OrderListActivity.class);
            intent.putExtra(OrderListActivity.ORDER_STATUS, OrderRepository.ORDER_STATUS_PAYING);
            context.startActivity(intent);
        }
    });
    public ReplyCommand returnClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!login.get()) {
                gotoLogin();
                return;
            }
            Intent intent = new Intent(context, BackListActivity.class);
            context.startActivity(intent);
        }
    });

    public ReplyCommand feedBackClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            WebActivity.startWebActivity(context, Config.FEEDBACK_URL, context.getString(R.string.feedback));
        }
    });
    public ReplyCommand shareClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            ((MainActivity) context).showShare();
        }
    });

    private void gotoUser() {
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
    }

    private void gotoLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public MeViewModel(Context context) {
        this.context = context;
        initUser();
        defaultHead.set("res:///" + R.drawable.ic_user_head_default);
        loginHead.set("res:///" + R.drawable.ic_onemall_splash);
    }

    public void onUserLogin() {
        initUser();
    }

    public void onUserLogout() {
        login.set(false);
        userName.set(context.getString(R.string.activity_login_login));
        recevingNum.set("");
        payingNum.set("");
    }

    public void onReceiveChanged() {
        updateReceive();
    }

    public void initUser() {
        RxUtil.execute(new IOTask<Token>() {
            @Override
            public Token run() {
                boolean isLogin = UserRepository.getRepository().isLogin(context);
                if (isLogin) {
                    return UserRepository.getRepository().getCurrentUserToken(context);
                }
                return null;
            }
        }, new UIAction<Token>() {
            @Override
            public void onComplete(Token token) {
                if (token != null) {
                    login.set(true);
                    userName.set(token.getPhone());
                    updateReceive();
                    updatePaying();
                }
            }
        });
    }

    public void updatePaying() {
        RxUtil.execute(new IOTask<List<Order>>() {
            @Override
            public List<Order> run() {
                if (login.get()) {
                    return OrderRepository.geRepository().listOrderByStatus(UserRepository.getRepository().getCurrentUserToken(context).getValue(), OrderRepository.ORDER_STATUS_PAYING);
                }
                return null;
            }
        }, new UIAction<List<Order>>() {
            @Override
            public void onComplete(List<Order> orders) {
                if (orders != null && orders.size() > 0) {
                    payingNum.set(orders.size() + "");
                } else {
                    payingNum.set("");
                }
            }
        });
    }

    public void updateReceive() {
        RxUtil.execute(new IOTask<List<Order>>() {
            @Override
            public List<Order> run() {
                if (login.get()) {
                    return OrderRepository.geRepository().getReceiveing(UserRepository.getRepository().getCurrentUserToken(context).getValue());
                }
                return null;
            }
        }, new UIAction<List<Order>>() {
            @Override
            public void onComplete(List<Order> orders) {
                if (orders != null && orders.size() > 0) {
                    recevingNum.set(orders.size() + "");
                } else {
                    recevingNum.set("");
                }
            }
        });
    }

    @Override
    public void onBack() {

    }
}
