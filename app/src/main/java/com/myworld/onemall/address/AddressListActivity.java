package com.myworld.onemall.address;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.databinding.ActivityAddressListBinding;
import com.myworld.onemall.order.ConfirmOrderActivity;

public class AddressListActivity extends BaseActivity {
    public static final String ACTION_LIST_CHANGED = "action_list_changed";
    public ActivityAddressListBinding binding;
    private boolean isFromOrder = false;
    public static final String IS_FROM_ORDER = "is_from_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_list);
        AddressViewModel viewModel = new AddressViewModel(this);
        binding.setAddressViewModel(viewModel);
        isFromOrder = getIntent().getBooleanExtra(IS_FROM_ORDER, false);
        registeEventAction(ACTION_LIST_CHANGED);
    }

    @Override
    protected void onEvent(String action) {
        super.onEvent(action);
        if (action.equals(ACTION_LIST_CHANGED)) {
            binding.getAddressViewModel().initAddress();
        }
    }

    public void addNewAddress() {
        if (isFromOrder) {
            if (binding.getAddressViewModel().viewModels.size() == 1) {
                Address address = binding.getAddressViewModel().viewModels.get(0).getAddr();
                AddressRepository.getRepository().setSelectAddress(address);
                publishEvent(ConfirmOrderActivity.ACTION_CHOOSE_ADDRESS);
            }
        }
    }

    public void onDefaultChanged(Address address) {
        if (isFromOrder) {
            Address selected = AddressRepository.getRepository().getSelectAddress();
            try {
                if (!selected.equals(address))
                    onAddressSelect(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddressSelect(Address address) {
        if (isFromOrder) {
            AddressRepository.getRepository().setSelectAddress(address);
            publishEvent(ConfirmOrderActivity.ACTION_CHOOSE_ADDRESS);
            finish();
        }
    }
}
