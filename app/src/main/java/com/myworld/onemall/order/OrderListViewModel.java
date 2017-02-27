package com.myworld.onemall.order;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Order;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.data.repository.OrderRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.utils.UserUtil;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 2016/12/1.
 */

public class OrderListViewModel extends LoaddingViewModel {
    private OrderListActivity activity;
    private int status;
    public ItemViewSelector<OrderItemViewModel> itemView = new ItemViewSelector<OrderItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, OrderItemViewModel item) {
            if (item.order.getGoodsList().size() == 1) {
                itemView.set(BR.itemViewModel, R.layout.list_order_one);
            } else if (item.order.getGoodsList().size() > 1) {
                itemView.set(BR.itemViewModel, R.layout.list_order_muilt);
            }
        }

        @Override
        public int viewTypeCount() {
            return 2;
        }
    };

    public ObservableList<OrderItemViewModel> viewModels = new ObservableArrayList<>();
    public ObservableField<String> title = new ObservableField<>();

    public OrderListViewModel(OrderListActivity activity, int status) {
        this.activity = activity;
        this.status = status;
        initTitle();
        initOrderList();
    }

    public void initTitle() {
        switch (status) {
            case OrderRepository.ORDER_STATUS_ALL:
                title.set(activity.getString(R.string.order_list_title_all));
                break;
            case OrderRepository.ORDER_STATUS_RECEIVEING:
                title.set(activity.getString(R.string.order_list_title_receiving));
                break;
            case OrderRepository.ORDER_STATUS_PAYING:
                title.set(activity.getString(R.string.order_state_paying));
                break;
        }
    }

    public void initOrderList() {
        loadding.set(true);
        RxUtil.execute(new IOTask<List<Order>>() {
            @Override
            public List<Order> run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    List<Order> orders = OrderRepository.geRepository().listOrderByStatus(token.getValue(), status);
                    return orders;
                } else {
                    UserUtil.login(activity);
                }
                return null;
            }
        }, new UIAction<List<Order>>() {
            @Override
            public void onComplete(List<Order> orders) {
                loadding.set(false);
                if (orders != null && orders.size() > 0) {
                    for (Order order : orders) {
                        try {
                            OrderItemViewModel itemViewModel = new OrderItemViewModel(activity, order);
                            viewModels.add(itemViewModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    emptyContent.set(true);
                }
            }
        });
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
