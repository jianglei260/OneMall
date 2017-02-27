package com.myworld.onemall.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivityPaySendBinding;

public class PaySendActivity extends BaseActivity {
    ActivityPaySendBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_send);
        binding.setPayViewModel(new PaySendViewModel(this));
        int payway = getIntent().getIntExtra(ConfirmOrderActivity.PAY_WAY, R.id.alipay);
        binding.getPayViewModel().checkedButton.set(payway);
    }

    public void updatePayWay(int payway) {
        Intent intent = new Intent(ConfirmOrderActivity.ACTION_CHOOSE_PAYWAY);
        intent.putExtra(ConfirmOrderActivity.PAY_WAY, payway);
        publishEvent(intent);
    }
}
