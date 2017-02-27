package com.myworld.onemall.order;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.address.AddressListActivity;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.databinding.ActivityConfirmOrderBinding;

public class ConfirmOrderActivity extends BaseActivity {
    public ActivityConfirmOrderBinding binding;
    public static final String ACTION_CHOOSE_ADDRESS = "action_choose_address";
    public static final String ACTION_SEND_ORDER_SUCCESS = "action_send_order_success";
    public static final String ACTION_CHOOSE_PAYWAY = "action_choose_pay_way";
    public static final String PAY_WAY = "pay_way";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_order);
        ConfirmOrderViewModel viewModel = new ConfirmOrderViewModel(this);
        binding.setConfirmViewModel(viewModel);
        registeEventAction(ACTION_CHOOSE_ADDRESS);
        registeEventAction(ACTION_CHOOSE_PAYWAY);
    }

    @Override
    protected void onEvent(String action) {
        super.onEvent(action);
        if (action.equals(ACTION_CHOOSE_ADDRESS)) {
            Address address = AddressRepository.getRepository().getSelectAddress();
            binding.getConfirmViewModel().onAddressSelect(address);
        }
    }

    @Override
    protected void onEvent(Intent intent) {
        if (intent.getAction().equals(ACTION_CHOOSE_PAYWAY)) {
            int payway = intent.getIntExtra(PAY_WAY, R.id.alipay);
            if (binding != null)
                binding.getConfirmViewModel().updatePayWay(payway);
        }
    }

    public void chooseAddress() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra(AddressListActivity.IS_FROM_ORDER, true);
        startActivity(intent);
    }

    public void sendOrderSuccess() {
        publishEvent(ACTION_SEND_ORDER_SUCCESS);
        finish();
    }
    @Override
    public void onBackPressed() {
        binding.getConfirmViewModel().onBack();
    }
}
