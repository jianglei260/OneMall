package com.myworld.onemall.login;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivityFounPdBinding;


public class FounPdActivity extends BaseActivity {
    ActivityFounPdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_foun_pd);
        binding.setRegistViewModel(new FoundViewModel(this));
    }

    public void showTimer() {
        final Button button = binding.e9;
        button.setText(65 + getString(R.string.timer_notify));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int time = 65;

            @Override
            public void run() {
                button.setText(time + getString(R.string.timer_notify));
                if (time > 0) {
                    handler.postDelayed(this, 1000);
                    button.setClickable(false);
                    binding.getRegistViewModel().getCodeClickable.set(false);
                } else {
                    button.setText(getString(R.string.get_sms_code));
                    button.setClickable(true);
                    binding.getRegistViewModel().getCodeClickable.set(true);
                }
                time--;
            }
        }, 0);
    }

    public void showPasswdTooShort() {
        binding.passwrod.setError(getString(R.string.error_passwd_too_short));
    }

    public void showWrongPhoneNumber() {
        binding.phoneNumber.setError(getString(R.string.error_phone_number));
    }

    public void onUserLogin() {
        publishEvent(LoginActivity.ACTION_USER_LOGIN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
