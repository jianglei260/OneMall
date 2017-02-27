package com.myworld.onemall.main;

import android.app.AlertDialog;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.myworld.onemall.R;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.databinding.CartEmptyLayoutBinding;
import com.myworld.onemall.databinding.CartFullLayoutBinding;
import com.myworld.onemall.databinding.DialogCartEditBinding;
import com.myworld.onemall.databinding.FragmentCartBinding;

/**
 * Created by jianglei on 2016/11/24.
 */

public class CartFragment extends Fragment {
    public CartEmptyLayoutBinding emptyLayoutBinding;
    public CartFullLayoutBinding fullLayoutBinding;
    public FragmentCartBinding cartBinding;
    DialogCartEditBinding pickBinding;
    private RelativeLayout root;
    private ViewDataBinding current;
    private static final String TAG = "CartFragment";
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        cartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        CartViewModel cartViewModel = new CartViewModel(this);
        cartBinding.setCartViewModel(cartViewModel);
        root = (RelativeLayout) cartBinding.getRoot();

        emptyLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.cart_empty_layout, cartBinding.container, false);
        fullLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.cart_full_layout, cartBinding.container, false);
        emptyLayoutBinding.setCartViewModel(new CartEmptyViewModel(this));
        fullLayoutBinding.setCartFullViewModel(new CartFullViewModel(this));

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        emptyLayoutBinding.recyclerView.setLayoutManager(layoutManager);
        try {
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ListItemViewModel itemViewModel = emptyLayoutBinding.getCartViewModel().viewModels.get(position);
                    if (itemViewModel.getViewType() == ListItemViewModel.VIEW_TYPE_NORMAL) {
                        return 1;
                    }
                    return 2;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    public void update() {
        if (cartBinding != null)
            cartBinding.getCartViewModel().initCart();
    }

    public void showEmptyLayout() {
        cartBinding.container.removeAllViews();
        cartBinding.container.addView(emptyLayoutBinding.getRoot());
        current = emptyLayoutBinding;
    }

    public void showFullLayout() {
        cartBinding.container.removeAllViews();
//        int topHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.title_bar_height);
//        int footHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.navigation_bar_height);
//        int height = getActivity().getResources().getDisplayMetrics().heightPixels - topHeight - footHeight;
        cartBinding.container.addView(fullLayoutBinding.getRoot());
        current = fullLayoutBinding;
    }

    public void hideNumberPicker() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void onUserLogout() {
        cartBinding.getCartViewModel().onUserLogout();
    }

    public void showNumberPicker(int num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        pickBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.dialog_cart_edit, null, false);
        pickBinding.setPickViewModel(new NumberPickerViewModel(this, num));
        builder.setView(pickBinding.getRoot());
        dialog = builder.show();
    }

    public void showLoadding() {
        if (cartBinding != null)
            cartBinding.getCartViewModel().loadding.set(true);
    }

    public void hideLoadding() {
        if (cartBinding != null)
            cartBinding.getCartViewModel().loadding.set(false);
    }
}
