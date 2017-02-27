package com.myworld.onemall.order;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivityOrderReturnBinding;

public class OrderReturnActivity extends BaseActivity {
    private ActivityOrderReturnBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String orderId = getIntent().getStringExtra(OrderDetailActivity.ORDER_ID);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_return);
        OrderReturnViewModel viewModel = new OrderReturnViewModel(this, orderId);
        binding.setReturnViewModel(viewModel);
    }
}
