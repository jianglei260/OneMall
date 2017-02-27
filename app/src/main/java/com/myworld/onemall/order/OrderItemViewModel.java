package com.myworld.onemall.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.ItemViewModel;
import com.myworld.onemall.cart.CartActivity;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.PayResult;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.utils.UserUtil;
import com.myworld.onemall.widget.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/1.
 */

public class OrderItemViewModel extends ItemViewModel {
    public Order order;
    private OrderListActivity activity;
    public ObservableField<String> goodsImage = new ObservableField<>();
    public ObservableField<String> goodsName = new ObservableField<>();
    public ObservableField<String> orderNumber = new ObservableField<>();
    public ObservableField<String> orderPrice = new ObservableField<>();
    public ObservableField<String> orderState = new ObservableField<>();
    public ObservableBoolean finished = new ObservableBoolean(false);
    public ObservableBoolean deleteable = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableBoolean needPayBack = new ObservableBoolean(false);

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

    public ReplyCommand deleteClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            deleteOrder();
        }
    });

    public ReplyCommand buyAgainClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            addOrderToCart();
        }
    });

    public ReplyCommand confirmReceivedClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            cancelOrder();
        }
    });

    public ReplyCommand payClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            payOrder();
        }
    });

    public void payOrder() {
        RxUtil.execute(new IOTask<String>() {
            @Override
            public String run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return OrderRepository.geRepository().pay(token.getValue(), order.get_id());
                }
                return null;
            }
        }, new UIAction<String>() {
            @Override
            public void onComplete(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    int code = json.getInt("code");
                    boolean success = code == 200;
                    if (success) {
                        onAddOrderSuccess(result);
                    } else {
                        onAddOrderFailed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onAddOrderFailed();
                }
            }
        });

    }

    private void alipay(final String payInfo) {
        RxUtil.execute(new IOTask<String>() {
            @Override
            public String run() {
                PayTask alipay = new PayTask(activity);
                return alipay.pay(payInfo, true);
            }
        }, new UIAction<String>() {
            @Override
            public void onComplete(String s) {
                PayResult payResult = new PayResult(s);
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                    onSendOrderSuccess();
                    CustomToast.showSuccess(activity, activity.getString(R.string.send_order_finish));
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onAddOrderSuccess(String result) {
        try {
            JSONObject json = new JSONObject(result);
            JSONObject payObject = json.getJSONObject("data");
            String payInfo = payObject.getString("payInfo");
            if (!TextUtils.isEmpty(payInfo)) {
                alipay(payInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onAddOrderFailed() {
        CustomToast.showFailed(activity, activity.getString(R.string.send_order_failed));
    }

    public void onSendOrderSuccess() {
        status.set(OrderRepository.ORDER_STATUS_WILL_SEND);
        order.setStatus(OrderRepository.ORDER_STATUS_WILL_SEND);
        activity.sendOrderSuccess();
        initStatus();
    }

    public OrderItemViewModel(final OrderListActivity activity, final Order order) {
        this.order = order;
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

    public void deleteOrder() {
        activity.showLoadding();
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return OrderRepository.geRepository().deleteOrder(token.getValue(), order.get_id());
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                activity.hideLoadding();
                if (success) {
                    activity.binding.getOrderViewModel().viewModels.remove(OrderItemViewModel.this);
                } else {
                    CustomToast.showFailed(activity, activity.getString(R.string.order_delete_failed));
                }
            }
        });
    }

    private int addSuccessNum = 0;

    public void addOrderToCartFinish() {
        if (addSuccessNum > 0) {
            if (addSuccessNum < order.getGoodsList().size()) {
                CustomToast.showFailed(activity, activity.getString(R.string.order_add_cart_not_all));
            }
            Intent intent = new Intent(activity, CartActivity.class);
            activity.startActivity(intent);
        } else {
            CustomToast.showFailed(activity, activity.getString(R.string.order_add_cart_error));
        }

    }

    public void addOrderToCart() {
        if (!UserRepository.getRepository().isLogin(activity)) {
            UserUtil.login(activity);
            return ;
        }
        activity.showLoadding();
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                List<Goods> goodsList = order.getGoodsList();
                Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                addSuccessNum = 0;
                for (Goods goods : goodsList) {
                    int num = goods.getNumber();
                    boolean success = CartRepository.geRepository().add(token.getValue(), goods.get_id(), num > 0 ? num : 1);
                    if (success) {
                        addSuccessNum++;
                    }
                }
                return true;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                activity.hideLoadding();
                if (success) {
                    addOrderToCartFinish();
//                    addSuccessNum++;
//                    CustomToast.showSuccess(activity, activity.getString(R.string.add_cart_success));
                } else {
                    CustomToast.showFailed(activity, activity.getString(R.string.add_cart_failed));
                }
            }
        });
    }

    public void cancelOrder() {
        activity.showLoadding();
        needPayBack.set(order.getStatus() != OrderRepository.ORDER_STATUS_PAYING);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return OrderRepository.geRepository().cancelOrder(token.getValue(), order.get_id());
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                activity.hideLoadding();
                if (success) {
                    onCancelOrderSuccess();
                } else {
                    CustomToast.showFailed(activity, activity.getString(R.string.order_cancel_failed));
                }
            }
        });
    }

    public void onCancelOrderSuccess() {
        if (order.getType() == 0 && needPayBack.get()) {
            AlertDialog dialog = new AlertDialog.Builder(activity).setMessage(R.string.pay_back).setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else {
            CustomToast.showSuccess(activity, activity.getString(R.string.order_cancel_success));
        }
        order.setStatus(OrderRepository.ORDER_STATUS_CANCEL);
        initStatus();
        activity.sendOrderSuccess();
    }

    public void initOrder() {
        List<Goods> goodses = order.getGoodsList();
        int totalNum = 0;
        if (goodses.size() == 1) {
            float totalPrice = 0;
            Picture picture = GoodsRepository.getRepository().getFirstPicture(goodses.get(0));
            goodsImage.set(picture.getUrl());
            goodsName.set(goodses.get(0).getGoodsName());
            try {
                float price = Float.parseFloat(goodses.get(0).getGoodsPrice());
                int num = goodses.get(0).getNumber();
                totalPrice = price * num;
                totalNum += num;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            orderPrice.set(StringUtil.getRMBString(totalPrice));
        } else if (order.getGoodsList().size() > 1) {
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
        }
        orderId.set(order.getOrderId());
        orderNumber.set(activity.getString(R.string.order_num_prefix) + totalNum + activity.getString(R.string.order_num_tail));
        initStatus();
    }

    public void initStatus() {
        status.set(order.getStatus());
        switch (order.getStatus()) {
            case OrderRepository.ORDER_STATUS_CANCEL:
                deleteable.set(true);
                orderState.set(activity.getString(R.string.order_state_cancel));
                break;
            case OrderRepository.ORDER_STATUS_DELETE:
                break;
            case OrderRepository.ORDER_STATUS_FINISH:
                deleteable.set(true);
                finished.set(true);
                break;
            case OrderRepository.ORDER_STATUS_SENDING:
                orderState.set(activity.getString(R.string.order_state_sending));
                break;
            case OrderRepository.ORDER_STATUS_WILL_SEND:
                orderState.set(activity.getString(R.string.order_state_will_sending));
                break;
            case OrderRepository.ORDER_STATUS_PAYING:
                orderState.set(activity.getString(R.string.order_state_paying));
                break;
        }
    }
}
