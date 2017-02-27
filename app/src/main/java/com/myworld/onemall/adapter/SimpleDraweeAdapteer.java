package com.myworld.onemall.adapter;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.onemall.R;
import com.myworld.onemall.widget.CustomDraweeView;
import com.myworld.onemall.widget.TouchImageView;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by jianglei on 2016/11/26.
 */

public class SimpleDraweeAdapteer {
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"imageUri", "loadding", "loadfailed"}, requireAll = false)
    public static void setImageUri(final SimpleDraweeView simpleDraweeView, final String uri, final boolean loadding, final boolean failed) {

        if (simpleDraweeView.getContext() instanceof Activity) {
            ((Activity) simpleDraweeView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(uri)) {
                        simpleDraweeView.setImageURI(Uri.parse(uri));
                    }
                }
            });
        }
    }

    @BindingAdapter(value = {"imageUri", "loadding", "loadfailed"}, requireAll = false)
    public static void setImageUri(final CustomDraweeView simpleDraweeView, final String uri, final boolean loadding, final boolean failed) {

        if (simpleDraweeView.getContext() instanceof Activity) {
            ((Activity) simpleDraweeView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(uri)) {
                        simpleDraweeView.setImageURI(Uri.parse(uri));
                    }
                }
            });
        }
    }

    @BindingAdapter(value = {"uri", "placeholderImageRes", "request_width", "request_height", "onSuccessCommand", "onFailureCommand"}, requireAll = false)
    public static void loadImage(final PhotoDraweeView imageView, final String uri,
                                 @DrawableRes int placeholderImageRes,
                                 int width, int height,
                                 final ReplyCommand onSuccessCommand,
                                 final ReplyCommand<String> onFailureCommand) {
        if (!TextUtils.isEmpty(uri)) {
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.parse(uri));
            controller.setOldController(imageView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFailure(String id, Throwable throwable) {
                    if (onFailureCommand != null)
                        onFailureCommand.execute(uri);
                }

                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    if (imageInfo == null || imageView == null) {
                        if (onFailureCommand != null)
                            onFailureCommand.execute(uri);
                        return;
                    }
                    imageView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            imageView.setController(controller.build());
        }
    }

}
