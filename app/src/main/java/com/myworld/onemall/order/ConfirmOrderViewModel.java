package com.myworld.onemall.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.widget.Toast;


import com.alipay.sdk.app.PayTask;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.address.AddressListActivity;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.PayInfo;
import com.myworld.onemall.data.entity.PayResult;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.data.source.remote.RetrofitProvider;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.StringUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

import com.myworld.onemall.BR;
import com.myworld.onemall.widget.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jianglei on 2016/12/5.
 */

public class ConfirmOrderViewModel extends LoaddingViewModel {
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableList<OrderGoodsItemViewModel> viewModels = new ObservableArrayList<>();
    public ObservableField<String> payWay = new ObservableField<>();
    public ObservableField<String> sendWay = new ObservableField<>();
    public ObservableFloat goodsPrice = new ObservableFloat();
    public ObservableFloat sendPrice = new ObservableFloat();
    public ObservableFloat preferPrice = new ObservableFloat();
    public ObservableFloat totalPrice = new ObservableFloat();
    public ObservableField<String> message = new ObservableField<>();
    private int payId = R.id.alipay;

    private ConfirmOrderActivity activity;
    private Address addr;
    public ItemViewSelector<OrderGoodsItemViewModel> itemView = new ItemViewSelector<OrderGoodsItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, OrderGoodsItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_order_goods);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public ReplyCommand addressClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            activity.chooseAddress();
        }
    });

    public ReplyCommand paySendClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, PaySendActivity.class);
            intent.putExtra(ConfirmOrderActivity.PAY_WAY, payId);
            activity.startActivity(intent);
        }
    });

    public ReplyCommand sendOrderClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            sendOrder();
        }
    });

    public ConfirmOrderViewModel(ConfirmOrderActivity activity) {
        this.activity = activity;
        initAddress();
        initGoodslist();
        initPaySend();
    }

    public void onAddressSelect(Address addr) {
        if (addr != null)
            bindAddress(addr);
    }

    public void sendOrder() {
        boolean haveEmpty = TextUtils.isEmpty(name.get()) || TextUtils.isEmpty(address.get()) || TextUtils.isEmpty(phone.get());
        if (haveEmpty) {
            CustomToast.showFailed(activity, activity.getString(R.string.send_order_check_addres));
            return;
        }
        loadding.set(true);
        RxUtil.execute(new IOTask<String>() {
            @Override
            public String run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    int type = payId == R.id.alipay ? 0 : 1;
                    return OrderRepository.geRepository().addOrder(token.getValue(), addr.getName(), addr.getContact(), addr.getAddress(), message.get(), type);
                }
                return null;
            }
        }, new UIAction<String>() {
            @Override
            public void onComplete(String result) {
                loadding.set(false);
                try {
                    JSONObject json = new JSONObject(result);
                    int code = json.getInt("code");
                    boolean success = code == 200;
                    if (success) {
                        onAddOrderSuccess(result);
                    } else {
                        RetrofitProvider.handler.handleCode(code, "");
                        onAddOrderFailed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onAddOrderFailed();
                }
            }
        });
    }

    public void onAddOrderSuccess(String result) {
        if (isOnlinePay()) {
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
        } else {
            onSendOrderSuccess();
            CustomToast.showSuccess(activity, activity.getString(R.string.send_order_finish));
        }
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
                        Intent intent = new Intent(activity, OrderListActivity.class);
                        activity.startActivity(intent);
                        activity.sendOrderSuccess();
                    }
                }
            }
        });
    }

    public void onAddOrderFailed() {
        CustomToast.showFailed(activity, activity.getString(R.string.send_order_failed));
    }

    public boolean isOnlinePay() {
        return payId == R.id.alipay;
    }

    public void onSendOrderSuccess() {
        CartRepository.geRepository().removeChecked();
        activity.sendOrderSuccess();
    }

    public void initAddress() {
        RxUtil.execute(new IOTask<Address>() {
            @Override
            public Address run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    Address address = AddressRepository.getRepository().getSelectAddress();
                    if (address == null)
                        return AddressRepository.getRepository().getDefaultAddress(token.getValue());
                    else
                        return address;
                }
                return null;
            }
        }, new UIAction<Address>() {
            @Override
            public void onComplete(Address address) {
                if (address != null) {
                    bindAddress(address);
                } else {
                    bindEmptyAddress();
                }
            }
        });
    }

    public void updatePayWay(int payway) {
        switch (payway) {
            case R.id.alipay:
                payId = payway;
                payWay.set(activity.getString(R.string.pay_online));
                break;
            case R.id.offline:
                payId = payway;
                payWay.set(activity.getString(R.string.pay_offline));
                break;
        }
    }

    public void initGoodslist() {
        loadding.set(true);
        RxUtil.execute(new IOTask<List<Cart>>() {
            @Override
            public List<Cart> run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return CartRepository.geRepository().getCheckedCarts(token.getValue());
                }
                return new ArrayList<Cart>();
            }
        }, new UIAction<List<Cart>>() {
            @Override
            public void onComplete(List<Cart> carts) {
                loadding.set(false);
                for (Cart cart : carts) {
                    OrderGoodsItemViewModel itemViewModel = new OrderGoodsItemViewModel(activity, cart);
                    viewModels.add(itemViewModel);
                }
                initPrice();
            }
        });
    }

    public void initPaySend() {
        payWay.set(activity.getString(R.string.pay_online));
        sendWay.set(activity.getString(R.string.send_way_default));
    }

    public void initPrice() {
        float totalPrice = 0;
        for (OrderGoodsItemViewModel viewModel : viewModels) {
            try {
                float price = Float.parseFloat(viewModel.cart.getGoods().getGoodsPrice());
                int num = Integer.parseInt(viewModel.number.get());
                totalPrice += price * num;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        goodsPrice.set(totalPrice);
        sendPrice.set(0.0f);
        preferPrice.set(0.0f);
        ConfirmOrderViewModel.this.totalPrice.set(totalPrice);
    }

    public void bindEmptyAddress() {
        name.set(activity.getString(R.string.address_not_add));
    }

    public void bindAddress(Address addr) {
        this.addr = addr;
        name.set(addr.getName());
        phone.set(StringUtil.playMosaic(addr.getContact(), 3, 4));
        address.set(addr.getAddress());
    }

    @Override
    public void onBack() {
        AlertDialog dialog = new AlertDialog.Builder(activity).setMessage(activity.getString(R.string.confirm_giveup_order))
                .setPositiveButton(R.string.confirm_giveup_order_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                }).setNegativeButton(R.string.confirm_giveup_order_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
