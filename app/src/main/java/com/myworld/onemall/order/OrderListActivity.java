package com.myworld.onemall.order;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivityOrderListBinding;

public class OrderListActivity extends BaseActivity {

    public static final String ORDER_STATUS = "order_status";
    public ActivityOrderListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int status = getIntent().getIntExtra(ORDER_STATUS, 0);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_list);
        OrderListViewModel viewModel = new OrderListViewModel(this, status);
        binding.setOrderViewModel(viewModel);
    }

    public void sendOrderSuccess() {
        publishEvent(ConfirmOrderActivity.ACTION_SEND_ORDER_SUCCESS);
    }

    public void showLoadding() {
        if (binding != null)
            binding.getOrderViewModel().loadding.set(true);
    }

    public void hideLoadding() {
        if (binding != null)
            binding.getOrderViewModel().loadding.set(false);
    }
}
