package com.myworld.onemall.cart;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.FragmentCartBinding;
import com.myworld.onemall.main.CartEmptyViewModel;
import com.myworld.onemall.main.CartFragment;
import com.myworld.onemall.main.CartFullViewModel;
import com.myworld.onemall.main.CartViewModel;
import com.myworld.onemall.order.ConfirmOrderActivity;

public class CartActivity extends BaseActivity {


    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartFragment = new CartFragment();
        getFragmentManager().beginTransaction().add(R.id.activity_cart, cartFragment).show(cartFragment).commit();
        registeEventAction(ConfirmOrderActivity.ACTION_SEND_ORDER_SUCCESS);
    }

    @Override
    protected void onEvent(String action) {
        super.onEvent(action);
        if (action.equals(ConfirmOrderActivity.ACTION_SEND_ORDER_SUCCESS)) {
            if (cartFragment != null)
                cartFragment.update();
        }
    }
}
