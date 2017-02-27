package com.myworld.onemall.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.format.Formatter;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.about.AboutActivity;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.repository.UserRepository;
import com.myworld.onemall.utils.FileUtil;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;

import java.io.File;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/3.
 */

public class SettingViewModel extends LoaddingViewModel {
    public ObservableBoolean isLogin = new ObservableBoolean(false);
    public ObservableBoolean autoUpgrade = new ObservableBoolean(true);
    public ObservableField<String> cacheSize = new ObservableField<>();
    private SettingActivity activity;

    public ReplyCommand logoutClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            logout();
        }
    });

    public ReplyCommand clearCacheClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            deleteCache();
        }
    });

    public ReplyCommand aboutClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(activity, AboutActivity.class);
            activity.startActivity(intent);
        }
    });

    public void logout() {
        AlertDialog dialog = new AlertDialog.Builder(activity).setMessage(R.string.confirm_logout)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RxUtil.execute(new IOTask<Boolean>() {
                            @Override
                            public Boolean run() {
                                return UserRepository.getRepository().logout(activity);
                            }
                        }, new UIAction<Boolean>() {
                            @Override
                            public void onComplete(Boolean success) {
                                if (success) {
                                    isLogin.set(false);
                                    activity.onUserLogout();
                                    Toast.makeText(activity, activity.getString(R.string.user_logout), Toast.LENGTH_SHORT).show();
                                    activity.finish();
                                }
                            }
                        });
                    }
                }).show();
    }

    public SettingViewModel(SettingActivity activity) {
        this.activity = activity;
        initUser();
        initPreference();
        checkCacheSize();
        autoUpgrade.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                savePreference(autoUpgrade.get());
            }
        });
    }

    public void initUser() {
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                return UserRepository.getRepository().isLogin(activity);
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean login) {
                isLogin.set(login);
            }
        });
    }

    public void deleteCache() {
        loadding.set(true);
        RxUtil.execute(new IOTask<Boolean>() {
            @Override
            public Boolean run() {
                FileUtil.deleteFile(activity.getCacheDir());
                FileUtil.deleteFile(activity.getExternalCacheDir());
                return true;
            }
        }, new UIAction<Boolean>() {
            @Override
            public void onComplete(Boolean aBoolean) {
                loadding.set(false);
                checkCacheSize();
            }
        });
    }

    public void checkCacheSize() {

        RxUtil.execute(new IOTask<Long>() {
            @Override
            public Long run() {
                long size = 0;
                size = FileUtil.getFileSize(size, activity.getCacheDir());
                size = FileUtil.getFileSize(size, activity.getExternalCacheDir());
                size = FileUtil.getFileSize(size, activity.getFilesDir());
                return size;
            }
        }, new UIAction<Long>() {
            @Override
            public void onComplete(Long size) {
                cacheSize.set(Formatter.formatFileSize(activity, size));
            }
        });
    }

    public void savePreference(boolean auto) {
        SharedPreferences preferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("auto_upgrade", auto).commit();
    }

    public void initPreference() {
        SharedPreferences preferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean auto = preferences.getBoolean("auto_upgrade", true);
        autoUpgrade.set(auto);
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
