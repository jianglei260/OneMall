package com.myworld.onemall.detail;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.data.entity.Goods;
import com.myworld.onemall.data.repository.GoodsRepository;
import com.myworld.onemall.databinding.ActivityImageBinding;

public class ImageActivity extends BaseActivity {
    public static final String GOODS_ID = "goods_id";
    public static final String IMG_INDEX = "image_index";

    ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        String id = getIntent().getStringExtra(GOODS_ID);
        Goods goods = GoodsRepository.getRepository().getGoods(id);
        final int index = getIntent().getIntExtra(IMG_INDEX, 0);

        binding.switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(ImageActivity.this);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
                textView.setTextColor(Color.parseColor("#ff8b8b8b"));
                return textView;
            }
        });
        binding.setImageViewModel(new ImageViewModel(this, goods, index));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.viewpager.setCurrentItem(index, true);
            }
        }, 100);
    }

    public void showSelected(final int index) {
        binding.switcher.setText("" + (index + 1));
    }
}
