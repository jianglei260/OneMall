package com.myworld.onemall.about;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.base.Config;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.upgrade.AppUpdateTool;
import com.myworld.onemall.upgrade.VersionInfo;
import com.myworld.onemall.utils.IOTask;
import com.myworld.onemall.utils.RxUtil;
import com.myworld.onemall.utils.UIAction;
import com.myworld.onemall.widget.CustomToast;

import rx.functions.Action0;

/**
 * Created by jianglei on 2016/12/12.
 */

public class AboutViewModel extends LoaddingViewModel {
    private AboutActivity activity;

    public ObservableField<String> versionName = new ObservableField<>();
    public ObservableField<String> versionCode = new ObservableField<>();

    public ReplyCommand updateClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            checkUpdate();
        }
    });

    public AboutViewModel(AboutActivity activity) {
        this.activity = activity;
        initVersion();
    }

    public void checkUpdate() {
        loadding.set(true);
        final AppUpdateTool tool = new AppUpdateTool.Builder(activity).setAutoUpdate(isAuto()).setRequestUrl(Config.UPDATE_URL).build();
        tool.checkUpdate(new AppUpdateTool.UpdateCallback() {
            @Override
            public void needUpdate(final boolean needUpdate, final VersionInfo versionInfo) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadding.set(false);
                        if (needUpdate) {
                            showNeedUpdate(tool, versionInfo);
                        } else {
                            showNewest();
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                loadding.set(false);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        CustomToast.showFailed(activity, activity.getString(R.string.check_update_failed));
                        showNewest();
                    }
                });
            }
        });

    }

    public void showNeedUpdate(final AppUpdateTool tool, final VersionInfo versionInfo) {
        AlertDialog dialog = new AlertDialog.Builder(activity).setMessage(versionInfo.getFeature()).setTitle(R.string.update_new_version).setPositiveButton(R.string.update_download, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tool.doUpdate(versionInfo, false);
            }
        }).setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void showNewest() {
        AlertDialog dialog = new AlertDialog.Builder(activity).setMessage(R.string.update_newest_version).setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public boolean isAuto() {
        SharedPreferences preferences = activity.getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean auto = preferences.getBoolean("auto_upgrade", true);
        return auto;
    }

    public void initVersion() {
        PackageManager pm = activity.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), 0);
            versionName.set(pi.versionName + " ");
            versionCode.set(pi.versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBack() {
        activity.finish();
    }
}
