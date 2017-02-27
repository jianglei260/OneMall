package com.myworld.onemall.settings;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity {
    ActivitySettingBinding binding;
    public static final String ACTION_USER_LOGOUT = "action_user_logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setSettingViewModel(new SettingViewModel(this));
    }

    public void onUserLogout() {
        publishEvent(ACTION_USER_LOGOUT);
    }
}
