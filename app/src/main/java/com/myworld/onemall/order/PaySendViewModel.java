package com.myworld.onemall.order;

import android.databinding.Observable;
import android.databinding.ObservableInt;

import com.myworld.onemall.R;
import com.myworld.onemall.base.LoaddingViewModel;

/**
 * Created by jianglei on 2016/12/11.
 */

public class PaySendViewModel extends LoaddingViewModel{
    public ObservableInt checkedButton = new ObservableInt(R.id.alipay);
    private PaySendActivity activity;

    public PaySendViewModel(final PaySendActivity activity) {
        this.activity = activity;
        checkedButton.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                activity.updatePayWay(checkedButton.get());
            }
        });
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
