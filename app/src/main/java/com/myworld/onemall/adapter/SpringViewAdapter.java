package com.myworld.onemall.adapter;

import android.databinding.BindingAdapter;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.myworld.onemall.base.Refreshable;
import com.myworld.onemall.R;


/**
 * Created by jianglei on 2016/12/10.
 */

public class SpringViewAdapter {
    @BindingAdapter({"refresh"})
    public static void setRefreshListener(final SpringView springView, final Refreshable refreshable) {
        springView.setType(SpringView.Type.FOLLOW);
        int[] res = new int[]{R.drawable.a20, R.drawable.a21, R.drawable.a22, R.drawable.a23};
        springView.setHeader(new MeituanHeader(springView.getContext(), res, res));
        springView.setFooter(new DefaultFooter(springView.getContext(),R.drawable.load_progress));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshable.onRefresh(new Refreshable.OnComplete() {
                    @Override
                    public void onComplete() {
                        springView.onFinishFreshAndLoad();
                    }
                });
            }

            @Override
            public void onLoadmore() {
                refreshable.onLoadMore(new Refreshable.OnComplete() {
                    @Override
                    public void onComplete() {
                        springView.onFinishFreshAndLoad();
                    }
                });
            }
        });
    }
}
