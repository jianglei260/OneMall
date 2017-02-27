package com.myworld.onemall.back;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.ItemViewModel;
import com.myworld.onemall.cart.CartActivity;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.entity.Return;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.ReturnRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.order.OrderDetailActivity;
import com.myworld.onemall.order.OrderItemImageViewModel;
import com.myworld.onemall.order.OrderItemViewModel;
import com.myworld.onemall.order.OrderListActivity;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.utils.UserUtil;
import com.myworld.onemall.widget.CustomToast;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/6.
 */

public class BackItemViewModel extends ItemViewModel{
    public Order order;
    private BackListActivity activity;
    private Return back;
    public ObservableField<String> goodsImage = new ObservableField<>();
    public ObservableField<String> goodsName = new ObservableField<>();
    public ObservableField<String> orderNumber = new ObservableField<>();
    public ObservableField<String> orderPrice = new ObservableField<>();
    public ObservableField<String> orderState = new ObservableField<>();
    public ObservableBoolean finished = new ObservableBoolean(false);
    public ObservableBoolean deleteable = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<String> orderId = new ObservableField<>();

    public final ItemViewSelector<OrderItemImageViewModel> itemView = new ItemViewSelector<OrderItemImageViewModel>() {
        @Override
        public void select(ItemView itemView, int position, OrderItemImageViewModel item) {
            itemView.set(BR.imageViewModel, R.layout.list_order_muilt_image);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ObservableList<OrderItemImageViewModel> images = new ObservableArrayList<>();




    public ReplyCommand cancelApplyClcik = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            cancelApply();
        }
    });

    public void cancelApply() {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return ReturnRepository.geRepository().cancelReturn(token.getValue(), back.get_id());
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (success) {
                    CustomToast.showSuccess(activity, activity.getString(R.string.return_state_cancel));
                    back.setStatus(ReturnRepository.RETURN_STATUS_CANCEL);
                    initStatus();
                } else {
                    CustomToast.showSuccess(activity, activity.getString(R.string.return_cancel_failed));
                }
            }
        });

    }

    public BackItemViewModel(final BackListActivity activity, Return back) {
        this.back = back;
        this.order = back.getOrder();
        this.activity = activity;
        initOrder();
        itemClick = new ReplyCommand(new Action0() {
            @Override
            public void call() {
                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_ID, order.get_id());
                activity.startActivity(intent);
            }
        });
    }

    public void initOrder() {
        List<Goods> goodses = order.getGoodsList();
        int totalNum = 0;
        float totalPrice = 0;
        for (Goods goodse : goodses) {
            OrderItemImageViewModel imageViewModel = new OrderItemImageViewModel(this);
            imageViewModel.imageUrl.set(GoodsRepository.getRepository().getFirstPicture(goodse).getUrl());
            images.add(imageViewModel);
            try {
                float price = Float.parseFloat(goodse.getGoodsPrice());
                int num = goodse.getNumber();
                totalPrice += price * num;
                totalNum += num;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        orderPrice.set(StringUtil.getRMBString(totalPrice));
        orderId.set(order.getOrderId());
        orderNumber.set(activity.getString(R.string.order_num_prefix) + totalNum + activity.getString(R.string.order_num_tail));
        initStatus();
    }

    public void initStatus() {
        status.set(back.getStatus());
        switch (status.get()) {
            case ReturnRepository.RETURN_STATUS_FINISH:
                orderState.set(activity.getString(R.string.return_state_finish));
                break;
            case ReturnRepository.RETURN_STATUS_APPLY:
                orderState.set(activity.getString(R.string.return_state_apply));
                break;
            case ReturnRepository.RETURN_STATUS_CANCEL:
                orderState.set(activity.getString(R.string.return_state_cancel));
                break;
        }
    }


}
