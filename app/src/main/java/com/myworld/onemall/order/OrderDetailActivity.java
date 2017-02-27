package com.myworld.onemall.order;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.ActivityOrderDetailBinding;
import com.myworld.onemall.detail.DetailViewModel;

public class OrderDetailActivity extends AppCompatActivity {
    private ActivityOrderDetailBinding binding;
    public static final String ORDER_ID = "order_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        String orderId = getIntent().getStringExtra(ORDER_ID);
        OrderDetailViewModel viewModel = new OrderDetailViewModel(this, orderId);
        binding.setDetailViewModel(viewModel);
    }
}
