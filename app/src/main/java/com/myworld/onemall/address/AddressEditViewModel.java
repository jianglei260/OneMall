package com.myworld.onemall.address;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/4.
 */

public class AddressEditViewModel extends LoaddingViewModel{
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> area = new ObservableField<>();
    public ObservableBoolean saveble = new ObservableBoolean();
    private Address addr;
    private boolean editMode;

    private AddressEditActivity activity;

    public ReplyCommand saveClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            saveAddress();
        }
    });

    public void saveAddress() {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    if (editMode) {
                        boolean result = AddressRepository.getRepository().editAddress(token.getValue(), phone.get(),
                                area.get() + address.get(), name.get(), activity.getAddress().getStatus(), activity.getAddress().get_id());
                        return result;
                    } else {
                        boolean result = AddressRepository.getRepository().addAddress(token.getValue(), phone.get(),
                                area.get() + address.get(), name.get(), 0);
                        return result;
                    }
                }
                return false;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean success) {
                if (!success) {
                    if (editMode)
                        CustomToast.showFailed(activity, activity.getString(R.string.address_edit_failed));
                    else
                        CustomToast.showFailed(activity, activity.getString(R.string.address_add_failed));
                } else {
                    if (editMode)
                        Toast.makeText(activity, activity.getString(R.string.address_edit_finish), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(activity, activity.getString(R.string.address_add_finish), Toast.LENGTH_SHORT).show();
                    activity.updateAddressList();
                }
            }
        });
    }

    private void updateStatus() {
        boolean haveEmpty = TextUtils.isEmpty(name.get()) || TextUtils.isEmpty(address.get()) || TextUtils.isEmpty(phone.get())||phone.get().length()<11;
        saveble.set(!haveEmpty);
    }

    private void bindValueChange() {
        name.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                updateStatus();
            }
        });
        address.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                updateStatus();
            }
        });
        phone.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                updateStatus();
            }
        });
    }

    public AddressEditViewModel(AddressEditActivity activity, boolean edit) {
        this.activity = activity;
        editMode = edit;
        bindValueChange();
        area.set(activity.getString(R.string.address_area_default));
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
