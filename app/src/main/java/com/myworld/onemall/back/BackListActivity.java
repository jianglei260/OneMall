package com.myworld.onemall.back;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.ActivityBackListBinding;

public class BackListActivity extends AppCompatActivity {
    private ActivityBackListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_back_list);
        BackListViewModel viewModel = new BackListViewModel(this);
        binding.setBackViewModel(viewModel);
    }
}
