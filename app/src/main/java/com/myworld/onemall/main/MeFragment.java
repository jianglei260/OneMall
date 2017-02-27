package com.myworld.onemall.main;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.onemall.R;
import com.myworld.onemall.databinding.FragmentMeBinding;


/**
 * Created by jianglei on 2016/11/24.
 */

public class MeFragment extends Fragment {
    FragmentMeBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        MeViewModel meViewModel = new MeViewModel(getActivity());
        binding.setMeViewModel(meViewModel);
        View root = binding.getRoot();
        return root;
    }

    private static final String TAG = "MeFragment";

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
//        if (binding != null)
//            binding.getMeViewModel().updateReceive();
    }

    public void onReceiveChanged(){
        if (binding!=null){
            binding.getMeViewModel().onReceiveChanged();
            binding.getMeViewModel().updatePaying();
        }
    }

    public void onUserLogin() {
        if (binding != null)
            binding.getMeViewModel().onUserLogin();
    }

    public void onUserLogout() {
        if (binding != null)
            binding.getMeViewModel().onUserLogout();
    }
}
