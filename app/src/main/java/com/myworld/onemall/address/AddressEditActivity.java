package com.myworld.onemall.address;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.databinding.ActivityAddressEditBinding;

public class AddressEditActivity extends BaseActivity {
    public static final String EDIT = "is_edit";
    public static final String ADDRESS_ID = "address_id";
    private ActivityAddressEditBinding binding;
    private Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean editMode = getIntent().getBooleanExtra(EDIT, false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_edit);
        AddressEditViewModel viewModel = new AddressEditViewModel(this, editMode);
        binding.setEditViewModel(viewModel);
        if (editMode) {
            String id = getIntent().getStringExtra(ADDRESS_ID);
            address = AddressRepository.getRepository().getAddress(id);
            initViewModel(address);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void initViewModel(Address address) {
        AddressEditViewModel viewModel = binding.getEditViewModel();
        viewModel.phone.set(address.getContact());
        viewModel.name.set(address.getName());
        int index = address.getAddress().indexOf(viewModel.area.get());
        if (index > -1) {
            String addr = address.getAddress().substring(viewModel.area.get().length());
            viewModel.address.set(addr);
        } else {
            viewModel.address.set(address.getAddress());
        }
    }

    public void updateAddressList() {
        publishEvent(AddressListActivity.ACTION_LIST_CHANGED);
        finish();
    }
}
