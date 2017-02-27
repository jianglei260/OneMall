package com.myworld.onemall.back;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.myworld.onemall.BR;
import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Return;
import com.myworld.onemall.data.entity.Token;
import com.myworld.onemall.data.repository.ReturnRepository;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.order.OrderItemViewModel;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 2016/12/6.
 */

public class BackListViewModel extends LoaddingViewModel {
    public ItemViewSelector<BackItemViewModel> itemView = new ItemViewSelector<BackItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, BackItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.list_back);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public ObservableList<BackItemViewModel> viewModels = new ObservableArrayList<>();
    private BackListActivity activity;

    public BackListViewModel(BackListActivity activity) {
        this.activity = activity;
        initBackList();
    }

    public void initBackList() {
        loadding.set(true);
        RxUtil.execute(new IOTask<List<Return>>() {
            @Override
            public List<Return> run() {
                boolean login = UserRepository.getRepository().isLogin(activity);
                if (login) {
                    Token token = UserRepository.getRepository().getCurrentUserToken(activity);
                    return ReturnRepository.geRepository().listReturn(token.getValue());
                }
                return null;
            }
        }, new UIAction<List<Return>>() {
            @Override
            public void onComplete(List<Return> returns) {
                loadding.set(false);
                if (returns != null && returns.size() > 0) {
                    for (Return aReturn : returns) {
                        BackItemViewModel itemViewModel = new BackItemViewModel(activity, aReturn);
                        viewModels.add(itemViewModel);
                    }
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
