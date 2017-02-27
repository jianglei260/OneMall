package com.myworld.onemall.main;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.onemall.R;
import com.myworld.onemall.base.ListItemViewModel;
import com.myworld.onemall.databinding.FragmentMainBinding;

/**
 * Created by jianglei on 2016/11/24.
 */

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private MainViewModel mainViewModel;
    public FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        View root = binding.getRoot();
        mainViewModel = new MainViewModel(this);
        binding.setMainViewModel(mainViewModel);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int verticalOffset = binding.recyclerView.computeVerticalScrollOffset();
                int backgroundColor = Color.TRANSPARENT;
                if (verticalOffset <= 255) {
                    backgroundColor = Color.argb(verticalOffset, verticalOffset, verticalOffset, verticalOffset);
                    binding.titleBar.setBackgroundColor(backgroundColor);
                } else {
                    binding.titleBar.setBackgroundColor(Color.WHITE);
                }
                if (verticalOffset <= 128) {
                    binding.getMainViewModel().white.set(true);
                } else {
                    binding.getMainViewModel().white.set(false);
                }
            }
        });
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recyclerView.setLayoutManager(layoutManager);
        try {
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ListItemViewModel itemViewModel = binding.getMainViewModel().viewModels.get(position);
                    if (itemViewModel.getViewType() == ListItemViewModel.VIEW_TYPE_NORMAL) {
                        return 1;
                    }
                    return 2;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    public void scrollToStart() {
        binding.recyclerView.scrollToPosition(0);
    }

    public void hideSearchBar() {
        binding.titleBar.setVisibility(View.GONE);
    }

    public void showSearchBar() {
        binding.titleBar.setVisibility(View.VISIBLE);
    }
}
