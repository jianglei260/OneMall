package com.myworld.onemall.login;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;
import com.myworld.onemall.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {
    private EditText passwd;
    private LinearLayout errorLayout;
    public static final String ACTION_USER_LOGIN = "action_user_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel loginViewModel = new LoginViewModel(this);
        binding.setLoginViewModel(loginViewModel);
        passwd = binding.d5;
        errorLayout = binding.d4w;
        registeEventAction(ACTION_USER_LOGIN);
    }

    @Override
    protected void onEvent(String action) {
        super.onEvent(action);
        finish();
    }

    public void setPasswdVisible(boolean visible) {
        if (visible)
            passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        else
            passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void showError() {
        errorLayout.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorLayout.setVisibility(View.GONE);
            }
        }, 1000);
    }

    public void onUserLogin(){
        publishEvent(ACTION_USER_LOGIN);
    }
}
