package com.myworld.onemall.order;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.net.Uri;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/1.
 */

public class OrderDetailViewModel extends LoaddingViewModel {
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableList<DetailGoodsItemViewModel> viewModels = new ObservableArrayList<>();
    public ObservableField<String> payWay = new ObservableField<>();
    public ObservableField<String> sendWay = new ObservableField<>();
    public ObservableFloat goodsPrice = new ObservableFloat();
    public ObservableFloat sendPrice = new ObservableFloat();
    public ObservableFloat preferPrice = new ObservableFloat();
    public ObservableFloat totalPrice = new ObservableFloat();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> sendOrderTime = new ObservableField<>();
    public ObservableField<String> receiveTime = new ObservableField<>();
    public ObservableField<String> orderState = new ObservableField<>();
    public ObservableField<String> message = new ObservableField<>();
    public ObservableInt status = new ObservableInt();
    private OrderDetailActivity activity;
    private Order order;


    public ItemViewSelector<DetailGoodsItemViewModel> itemView = new ItemViewSelector<DetailGoodsItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, DetailGoodsItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_order_detail_goods);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ReplyCommand backClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, OrderReturnActivity.class);
            intent.putExtra(OrderDetailActivity.ORDER_ID, order.get_id());
            activity.startActivity(intent);
        }
    });

    public ReplyCommand callClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:18408251157"));
            activity.startActivity(intent);
        }
    });

    public OrderDetailViewModel(OrderDetailActivity activity, String orderId) {
        this.activity = activity;
        initOrder(orderId);
    }

    public void initTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String sendTime = format.parse(order.getCreateAt()).toLocaleString();
            sendOrderTime.set(sendTime);
            String receiveTime = format.parse(order.getUpdateAt()).toLocaleString();
            OrderDetailViewModel.this.receiveTime.set(receiveTime);
        } catch (ParseException e) {
            e.printStackTrace();
            sendOrderTime.set(order.getCreateAt());
            receiveTime.set(order.getUpdateAt());
        }
    }

    public void initGoodslist() {
        List<Goods> goodsList = order.getGoodsList();
        for (Goods goods : goodsList) {
            DetailGoodsItemViewModel itemViewModel = new DetailGoodsItemViewModel(goods, activity);
            viewModels.add(itemViewModel);
        }
        initPrice(goodsList);
    }

    public void initAddress() {
        Address address = order.getAddressInfo();
        if (address != null)
            bindAddress(address);
    }

    public void initOrder(final String oderId) {
        loadding.set(true);
        RxUtil.execute(new IOTask<Order>() {
            @Override
            public Order run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return OrderRepository.geRepository().orderDetail(token.getValue(), oderId);
                }
                return null;
            }
        }, new UIAction<Order>() {
            @Override
            public void onComplete(Order order) {
                loadding.set(false);
                if (order == null) {
                    CustomToast.showFailed(activity, activity.getString(R.string.order_get_failed));
                } else {
                    OrderDetailViewModel.this.order = order;
                    OrderDetailViewModel.this.orderId.set(order.getOrderId());
                    message.set(order.getMessage());
                    initGoodslist();
                    initPaySend();
                    initAddress();
                    initStatus();
                    initTime();
                }
            }
        });
    }

    public void initStatus() {
        status.set(order.getStatus());
        switch (order.getStatus()) {
            case OrderRepository.ORDER_STATUS_CANCEL:
                orderState.set(activity.getString(R.string.order_state_cancel));
                break;
            case OrderRepository.ORDER_STATUS_DELETE:
                break;
            case OrderRepository.ORDER_STATUS_FINISH:
                orderState.set(activity.getString(R.string.order_state_finish));
                break;
            case OrderRepository.ORDER_STATUS_SENDING:
                orderState.set(activity.getString(R.string.order_state_sending));
                break;
            case OrderRepository.ORDER_STATUS_WILL_SEND:
                orderState.set(activity.getString(R.string.order_state_will_sending));
                break;
        }
    }

    public void initPrice(List<Goods> goodsList) {
        float totalPrice = 0;
        for (Goods goods : goodsList) {
            try {
                float price = Float.parseFloat(goods.getGoodsPrice());
                int num = goods.getNumber();
                totalPrice += price * num;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        goodsPrice.set(totalPrice);
        sendPrice.set(0.0f);
        preferPrice.set(0.0f);
        OrderDetailViewModel.this.totalPrice.set(totalPrice);
    }

    public void initPaySend() {
        payWay.set(order.getType() == 0 ? activity.getString(R.string.pay_online) : activity.getString(R.string.pay_offline));
        sendWay.set(activity.getString(R.string.send_way_default));
    }

    public void bindAddress(Address addr) {
        name.set(addr.getName());
        phone.set(StringUtil.playMosaic(addr.getContact(), 3, 4));
        address.set(addr.getAddress());
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
