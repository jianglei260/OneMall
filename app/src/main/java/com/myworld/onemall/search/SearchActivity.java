package com.myworld.onemall.search;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity {
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setSearchViewModel(new SearchViewModel(this));
        binding.wp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    binding.getSearchViewModel().searchClick.execute();
                    return true;
                }
                return false;
            }
        });
    }
}
