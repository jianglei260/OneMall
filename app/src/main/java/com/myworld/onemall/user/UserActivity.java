package com.myworld.onemall.user;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        UserViewModel userViewModel = new UserViewModel(this);
        binding.setUserViewModel(userViewModel);

    }
}
