package com.myworld.onemall.address;

import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/4.
 */

public class AddressItemViewModel {
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableBoolean isDefault = new ObservableBoolean();
    private AddressListActivity activity;
    private boolean setting = false;
    private Address addr;

    public ReplyCommand editClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, AddressEditActivity.class);
            intent.putExtra(AddressEditActivity.EDIT, true);
            intent.putExtra(AddressEditActivity.ADDRESS_ID, addr.get_id());
            activity.startActivity(intent);
        }
    });

    public ReplyCommand deleteClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            deleteAddress();
        }
    });

    public ReplyCommand itemClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            activity.onAddressSelect(addr);
        }
    });

    public ReplyCommand setDefaultClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (addr.getStatus() == 0 && (!setting)) {
                setting = true;
                setDefault();
            }
        }
    });

    public void setDefault() {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    boolean result = AddressRepository.getRepository().setDefaultAddress(token.getValue(), addr.get_id());
                    return result;
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                setting = false;
                if (!success) {
                    CustomToast.showFailed(activity, activity.getString(R.string.address_set_default_failed));
                } else {
                    CustomToast.showSuccess(activity, activity.getString(R.string.address_set_default_finish));
                    activity.binding.getAddressViewModel().updateDefault(AddressItemViewModel.this);
                }
            }
        });
    }

    public Address getAddr() {
        return addr;
    }

    public void deleteAddress() {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    boolean result = AddressRepository.getRepository().deleteAddress(token.getValue(), addr.get_id());
                    return result;
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (!success) {
                    CustomToast.showFailed(activity, activity.getString(R.string.address_delete_failed));
                } else {
                    activity.binding.getAddressViewModel().viewModels.remove(AddressItemViewModel.this);
                    if (isDefault.get()) {
                        activity.binding.getAddressViewModel().updateDefault(activity.binding.getAddressViewModel().viewModels.get(0));
                    }
                }
            }
        });
    }

    public AddressItemViewModel(AddressListActivity activity, Address addr) {
        this.activity = activity;
        this.addr = addr;
        name.set(addr.getName());
        address.set(addr.getAddress());
        phone.set(addr.getContact());
        isDefault.set(addr.getStatus() == 1 ? true : false);
        isDefault.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setDefaultClick.execute();
            }
        });
    }
}
