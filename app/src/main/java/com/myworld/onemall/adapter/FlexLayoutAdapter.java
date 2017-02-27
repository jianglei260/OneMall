package com.myworld.onemall.adapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewArg;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 2016/12/8.
 */

public class FlexLayoutAdapter {
    private static final String TAG = "FlexLayoutAdapter";

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemView", "items"}, requireAll = false)
    public static <T> void setItems(final FlexboxLayout flexboxLayout, final ItemViewSelector<T> selector, List<T> items) {
        final ItemViewArg<T> arg=ItemViewArg.of(selector);
        final LayoutInflater inflater = LayoutInflater.from(flexboxLayout.getContext());
        for (int i = 0; i < items.size(); i++) {
            arg.select(i, items.get(i));
            int layoutRes = arg.layoutRes();
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes, flexboxLayout, false);
            binding.setVariable(arg.bindingVariable(), items.get(i));
            flexboxLayout.addView(binding.getRoot());
        }
        ((ObservableList<T>) items).addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> items) {
                Log.d(TAG, "onChanged: ");
                flexboxLayout.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    arg.select(i, items.get(i));
                    int layoutRes = arg.layoutRes();
                    ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes, flexboxLayout, false);
                    binding.setVariable(arg.bindingVariable(), items.get(i));
                    flexboxLayout.addView(binding.getRoot());
                }
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> items, int i2, int i1) {
                Log.d(TAG, "onItemRangeChanged: ");
                flexboxLayout.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    arg.select(i, items.get(i));
                    int layoutRes = arg.layoutRes();
                    ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes, flexboxLayout, false);
                    binding.setVariable(arg.bindingVariable(), items.get(i));
                    flexboxLayout.addView(binding.getRoot());
                }
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> items, int i2, int i1) {
                Log.d(TAG, "onItemRangeInserted: ");
                flexboxLayout.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    arg.select(i, items.get(i));
                    int layoutRes = arg.layoutRes();
                    ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes, flexboxLayout, false);
                    binding.setVariable(arg.bindingVariable(), items.get(i));
                    flexboxLayout.addView(binding.getRoot());
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> items, int i3, int i1, int i2) {
                Log.d(TAG, "onItemRangeMoved: ");
                flexboxLayout.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    arg.select(i, items.get(i));
                    int layoutRes = arg.layoutRes();
                    ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes, flexboxLayout, false);
                    binding.setVariable(arg.bindingVariable(), items.get(i));
                    flexboxLayout.addView(binding.getRoot());
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> items, int i2, int i1) {
                Log.d(TAG, "onItemRangeRemoved: ");
                flexboxLayout.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    arg.select(i, items.get(i));
                    int layoutRes = arg.layoutRes();
                    ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes, flexboxLayout, false);
                    binding.setVariable(arg.bindingVariable(), items.get(i));
                    flexboxLayout.addView(binding.getRoot());
                }
            }
        });
    }
}
