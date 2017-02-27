package com.myworld.onemall.main;

import android.app.Fragment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.base.Config;
import com.myworld.onemall.data.repository.DynamicLoadRepository;
import com.myworld.onemall.detail.DetailActivity;
import com.myworld.onemall.login.LoginActivity;
import com.myworld.onemall.order.ConfirmOrderActivity;
import com.myworld.onemall.settings.SettingActivity;
import com.myworld.onemall.utils.EncryptUtil;

import java.util.ArrayList;
import java.util.HashMap;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.app.FragmentManager;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private MainFragment mainFragment;
    private FoundFragment foundFragment;
    private CartFragment cartFragment;
    private MeFragment meFragment;
    private FrameLayout container;
    private BottomNavigationBar bottomNavigationView;
    private Fragment selectd;
    private HashMap<Integer, Fragment> menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);
        bottomNavigationView = (BottomNavigationBar) findViewById(R.id.bottom_navigation);
        initFragment(savedInstanceState);
        menuMap = new HashMap<>();
        FragmentManager.enableDebugLogging(true);
        initBottomNavigation();
        registeEventAction(ConfirmOrderActivity.ACTION_SEND_ORDER_SUCCESS);
        registeEventAction(SettingActivity.ACTION_USER_LOGOUT);
        registeEventAction(LoginActivity.ACTION_USER_LOGIN);
        registeEventAction(DetailActivity.ACTION_CART_UPDATE);

    }

    public void initBottomNavigation() {
        bottomNavigationView.addItem(initItem(R.drawable.ic_main_bold, R.string.main, 0, mainFragment)).addItem(initItem(R.drawable.ic_cart, R.string.cart, 1, cartFragment)).addItem(initItem(R.drawable.ic_me, R.string.me, 2, meFragment))
                .addItem(initItem(R.drawable.ic_main_bold, R.string.cart, 3, getActiveFragment())).initialise();
        Log.d(TAG, "initBottomNavigation: "+getClassLoader().toString());
//        bottomNavigationView.setMode(BottomNavigationBar.MODE_DEFAULT);
//        bottomNavigationView.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
//        bottomNavigationView.setBarBackgroundColor(R.color.white);
        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                show(menuMap.get(position));
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    public Fragment getActiveFragment() {
        Class clazz = DynamicLoadRepository.getInstance().loadClass("com.myworld.active.ChristmasFragment");
        try {
            Fragment fragment = (Fragment) clazz.newInstance();
            getFragmentManager().beginTransaction().add(R.id.container,fragment,"active").commit();
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Fragment fragment=new Fragment();
        return fragment;
    }

    public BottomNavigationItem initItem(@DrawableRes int icon, @StringRes int title, int position, Fragment fragment) {
        BottomNavigationItem item = new BottomNavigationItem(icon, getString(title));
        item.setActiveColorResource(R.color.colorPrimary);
        item.setInActiveColorResource(R.color.deepGray);
        menuMap.put(position, fragment);
        return item;
    }

    //    public List<BottomNavigationItem> inflateMenu(@MenuRes int menuRes){
//        List<BottomNavigationItem> items=new ArrayList<>();
//        MenuInflater inflater=getMenuInflater();
//        Menu menu=new
//    }
    @Override
    protected void onEvent(String action) {
        super.onEvent(action);
        if (action.equals(ConfirmOrderActivity.ACTION_SEND_ORDER_SUCCESS)) {
            if (cartFragment != null)
                cartFragment.update();
            if (meFragment != null)
                meFragment.onReceiveChanged();
        } else if (action.equals(SettingActivity.ACTION_USER_LOGOUT)) {
            if (cartFragment != null)
                cartFragment.onUserLogout();
            if (meFragment != null)
                meFragment.onUserLogout();
        } else if (action.equals(LoginActivity.ACTION_USER_LOGIN)) {
            if (meFragment != null) {
                meFragment.onUserLogin();
            }
            if (cartFragment != null)
                cartFragment.update();
        } else if (action.equals(DetailActivity.ACTION_CART_UPDATE)) {
            if (cartFragment != null)
                cartFragment.update();
        }
    }

    public void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (!getFragmentManager().isDestroyed()) {
                MainFragment main = (MainFragment) getFragmentManager().findFragmentByTag("main");
                if (main != null)
                    mainFragment = main;
                else
                    initMain();
                CartFragment cart = (CartFragment) getFragmentManager().findFragmentByTag("cart");
                if (cart != null)
                    cartFragment = cart;
                else
                    initCart();
                MeFragment me = (MeFragment) getFragmentManager().findFragmentByTag("me");
                if (me != null)
                    meFragment = me;
                else
                    initMe();
            }
        } else {
            initMain();
            initCart();
            initMe();
        }
        getFragmentManager().beginTransaction().show(mainFragment).hide(cartFragment).hide(meFragment).commit();
        selectd = mainFragment;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void initMain() {
        mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().add(R.id.container, mainFragment, "main").commit();
    }

    public void initCart() {
        cartFragment = new CartFragment();
        getFragmentManager().beginTransaction().add(R.id.container, cartFragment, "cart").hide(cartFragment).commit();
    }

    public void initMe() {
        meFragment = new MeFragment();
        getFragmentManager().beginTransaction().add(R.id.container, meFragment, "me").hide(meFragment).commit();
    }

    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

        oks.disableSSOWhenAuthorize();
        oks.setTitle(getString(R.string.share_title));
        oks.setText(getString(R.string.slogan));
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://119.29.141.199/img/largeIcon.png");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Config.DOWNLOAD_URL);
        oks.setSiteUrl(Config.DOWNLOAD_URL);
        oks.setTitleUrl(Config.DOWNLOAD_URL);
        oks.show(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main:
                show(mainFragment);
                break;
//            case R.id.found:
//                show(foundFragment);
//                break;
            case R.id.cart:
                show(cartFragment);
                break;
            case R.id.me:
                show(meFragment);
                break;
        }
        return true;
    }

    private void show(Fragment fragment) {
        Log.d(TAG, "show: " + fragment.getClass().getName());
        if (fragment == selectd)
            return;
        if (selectd != null)
            getFragmentManager().beginTransaction().hide(selectd).commit();
        getFragmentManager().beginTransaction().show(fragment).commit();
        selectd = fragment;
    }
}
