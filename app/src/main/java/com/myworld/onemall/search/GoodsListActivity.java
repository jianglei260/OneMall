package com.myworld.onemall.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.ActivityGoodsListBinding;

public class GoodsListActivity extends AppCompatActivity {
    ActivityGoodsListBinding binding;
    public static final String KEY_WORDS = "key_word";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String keywords = getIntent().getStringExtra(KEY_WORDS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_list);
        GoodsListViewModel viewModel = new GoodsListViewModel(this, keywords);
        binding.setListViewModel(viewModel);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String keywords = intent.getStringExtra(KEY_WORDS);
        binding.getListViewModel().updateSearch(keywords);
    }

    public void changeLayout(boolean linear) {
        if (linear) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.recyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            binding.recyclerView.setLayoutManager(layoutManager);
        }
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }
}
