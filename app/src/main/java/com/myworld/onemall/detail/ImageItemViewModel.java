package com.myworld.onemall.detail;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.base.LoaddingViewModel;
import com.myworld.onemall.data.entity.Picture;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by jianglei on 2016/12/14.
 */

public class ImageItemViewModel extends LoaddingViewModel {
    private Picture picture;
    private ImageActivity activity;
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableBoolean loadfailed = new ObservableBoolean();

    public ImageItemViewModel(ImageActivity activity, Picture picture) {
        this.activity = activity;
        this.picture = picture;

        imageUrl.set(picture.getUrl());
    }

    public ReplyCommand reload = new ReplyCommand(new Action0() {
        @Override
        public void call() {

        }
    });

    public ReplyCommand onSuccessCommand = new ReplyCommand<Bitmap>(new Action1<Bitmap>() {
        @Override
        public void call(Bitmap bitmap) {

        }
    });

    public ReplyCommand<String> onFailureCommand = new ReplyCommand<String>(new Action1<String>() {
        @Override
        public void call(String s) {

        }
    });

    @Override
    public void onBack() {

    }
}
