package com.myworld.onemall.user;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.ActivityUserInfoEditBinding;

public class UserInfoEditActivity extends AppCompatActivity {
    private ActivityUserInfoEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info_edit);
        UserInfoEditViewModel viewModel = new UserInfoEditViewModel(this);
        binding.setEditViewModel(viewModel);
    }
}
