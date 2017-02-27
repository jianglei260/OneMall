package com.myworld.onemall.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.myworld.onemall.R;
import com.myworld.onemall.address.AddressListActivity;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.cart.CartActivity;
import com.myworld.onemall.data.entity.Address;
import com.myworld.onemall.data.entity.Cart;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.entity.Picture;
import com.myworld.onemall.data.repository.AddressRepository;
import com.myworld.onemall.data.repository.CartRepository;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.databinding.ActivityDetailBinding;
import com.myworld.onemall.databinding.ActivityDetailSelectBinding;
import com.myworld.onemall.order.ConfirmOrderActivity;

import java.lang.reflect.Field;
import java.util.List;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String ACTION_CART_UPDATE = "action_cart_update";
    public static final String GOODS_ID = "goodsId";
    private RelativeLayout container;
    private View selctView;
    private boolean selectViewShowing;
    private BottomNavigationBar bottomNavigationBar;
    ActivityDetailBinding binding;
    ActivityDetailSelectBinding selectBinding;
    private BadgeItem item;
    private Goods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        container = (RelativeLayout) findViewById(R.id.activity_detail);
        String id = getIntent().getStringExtra(GOODS_ID);
        final DetailViewModel detailViewModel = new DetailViewModel(id, this);
        binding.setDetailViewModel(detailViewModel);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        BottomNavigationItem cartItem = new BottomNavigationItem(R.drawable.ic_my_cart, "购物车").setActiveColor(Color.BLACK);
        item = new BadgeItem();

        cartItem.setBadgeItem(item);

        bottomNavigationBar.addItem(cartItem).initialise();
        initBottomBar(bottomNavigationBar);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        registeEventAction(ConfirmOrderActivity.ACTION_CHOOSE_ADDRESS);

    }

    @Override
    protected void onEvent(String action) {
        super.onEvent(action);
        if (action.equals(ConfirmOrderActivity.ACTION_CHOOSE_ADDRESS)) {
            Address address = AddressRepository.getRepository().getSelectAddress();
            binding.getDetailViewModel().onAddressSelect(address);
        }
    }

    public void chooseAddress() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra(AddressListActivity.IS_FROM_ORDER, true);
        startActivity(intent);
    }

    public Goods getGoods() {
        return goods;
    }

    public void updateCartNum(int num) {
        item.setText(String.valueOf(num));
        publishEvent(ACTION_CART_UPDATE);
    }

    private void initBottomBar(BottomNavigationBar navigationBar) {
        try {
            Field field = BottomNavigationBar.class.getDeclaredField("mTabContainer");
            field.setAccessible(true);
            LinearLayout tabContainer = (LinearLayout) field.get(navigationBar);
            TextView addText = new TextView(this);
            addText.setText(getString(R.string.add_cart));
            addText.setGravity(Gravity.CENTER);
            addText.setTextColor(getResources().getColor(R.color.white));
            addText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            tabContainer.addView(addText, 1, params);
            addText.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initSelectViewModel(Goods goods) {
        this.goods = goods;
        selectBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_detail_select, container, false);
        selctView = selectBinding.getRoot();
        SelectViewModel selectViewModel = new SelectViewModel(goods, this);
        selectBinding.setSelectlViewModel(selectViewModel);
    }

    public void upateGoods(Goods goods) {
        this.goods = goods;
        if (selectBinding != null)
            selectBinding.getSelectlViewModel().updateGoods(goods);
        binding.getDetailViewModel().upateGoods(goods);
    }

    public void showView() {
        container.addView(selctView);
        selectViewShowing = true;
        bottomNavigationBar.setVisibility(View.INVISIBLE);
    }

    public void hideView() {
        container.removeView(selctView);
        selectViewShowing = false;
        bottomNavigationBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartNum(CartRepository.geRepository().getGoodsNum());
    }

    @Override
    public void onBackPressed() {
        if (selectViewShowing)
            hideView();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        binding.getDetailViewModel().addCartClick.execute();
    }
}
