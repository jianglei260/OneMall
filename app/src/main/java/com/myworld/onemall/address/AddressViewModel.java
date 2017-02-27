package com.myworld.onemall.address;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/4.
 */

public class AddressViewModel extends LoaddingViewModel {
    public ItemViewSelector<AddressItemViewModel> itemView = new ItemViewSelector<AddressItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, AddressItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_address);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ObservableList<AddressItemViewModel> viewModels = new ObservableArrayList<>();
    public ObservableBoolean emptyContent = new ObservableBoolean(false);
    private AddressListActivity activity;
    public ReplyCommand newAddressClcik = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, AddressEditActivity.class);
            activity.startActivity(intent);
        }
    });

    public AddressViewModel(AddressListActivity activity) {
        this.activity = activity;
        initAddress();
    }

    public void updateDefault(AddressItemViewModel viewModel) {
        for (AddressItemViewModel model : viewModels) {
            if (model == viewModel) {
                model.isDefault.set(true);
                activity.onDefaultChanged(model.getAddr());
            } else {
                model.isDefault.set(false);
            }
        }
    }

    public void initAddress() {
        loadding.set(true);
        RxUtil.execute(new IOTask<List<Address>>() {
            @Override
            public List<Address> run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    List<Address> results = AddressRepository.getRepository().listAddress(token.getValue());
                    return results;
                }
                return null;
            }
        }, new UIAction<List<Address>>() {
            @Override
            public void onComplete(List<Address> addresses) {
                loadding.set(false);
                if (addresses != null && addresses.size() > 0) {
                    emptyContent.set(false);
                    viewModels.clear();
                    for (Address address : addresses) {
                        AddressItemViewModel itemViewModel = new AddressItemViewModel(activity, address);
                        viewModels.add(itemViewModel);
                    }
                    activity.addNewAddress();
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
