package com.myworld.onemall.main;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.data.entity.BannerItem;
import com.myworld.onemall.web.WebActivity;
import com.youth.banner.listener.OnBannerClickListener;

/**
 * Created by jianglei on 2016/12/6.
 */

public class BannerItemViewModel extends ListItemViewModel {
    public ObservableList<BannerItem> items = new ObservableArrayList<>();
    private Context context;

    public OnBannerClickListener listener = new OnBannerClickListener() {
        @Override
        public void OnBannerClick(int position) {
            BannerItem item = items.get(position - 1);
           WebActivity.startWebActivity(context,item.getUrl(),item.getName());
        }
    };

    public BannerItemViewModel(Context context) {
        this.context = context;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE_BANNER;
    }
}
