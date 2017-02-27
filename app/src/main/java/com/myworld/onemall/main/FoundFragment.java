package com.myworld.onemall.main;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.FragmentMainBinding;

/**
 * Created by jianglei on 2016/11/24.
 */

public class FoundFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataBindingUtil.inflate(inflater,R.layout.fragment_found,container,false);
        View root = inflater.inflate(R.layout.fragment_found, null);
        return root;
    }
}
